package binary.search.tree

import jetbrains.datalore.base.random.RandomString
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.ggplot
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.scale.scaleXContinuous
import jetbrains.letsPlot.scale.scaleYContinuous
import kotlin.math.log2
import kotlin.math.max

class BinarySearchTree {

    inner class Node(var key: Int) {
        var left: Node? = null
        var right: Node? = null
    }

    var root: Node? = null
    var totalIterationsOfLastInsert = 0

    fun insert(key: Int) {
        totalIterationsOfLastInsert = 0 // reset the iteration count
        root = insertRec(root, key)
    }

    fun insertRec(root: Node?, key: Int): Node {
        var _root = root
        totalIterationsOfLastInsert++ // increment iteration count

        if (_root == null) {
            _root = Node(key)
            return _root
        } else if (key < _root.key) {
            _root.left = insertRec(_root.left, key)
        } else if (key > _root.key) {
            _root.right = insertRec(_root.right, key)
        }

        return _root
    }

}

fun main() {
    worstInsertionPerformanceAnalysis()
    averageInsertionPerformanceAnalysis()
}

fun averageInsertionPerformanceAnalysis() {
    val sizes = DoubleArray(3000) { it.toDouble() + 1 }
    val actualIterations: ArrayList<Double> = ArrayList()
    val expectedIterations = DoubleArray(sizes.size) { i -> (log2(sizes[i])) }

    for (size in sizes) {
        val array = generateRandomArray(size.toInt())

        val bst = BinarySearchTree()
        var totalComparisons = 0

        for (key in array) {
            bst.insert(key)
            totalComparisons += bst.totalIterationsOfLastInsert
        }

        val averageComparisons = totalComparisons.toDouble() / size
        val expectedComparisons = log2(size)

        println("Average number of comparisons for input size $size: $averageComparisons")
        println("Expected number of comparisons for input size $size: $expectedComparisons")

        actualIterations.add(averageComparisons)
    }

    plotIterations(
        sizes,
        expectedIterations,
        actualIterations.toDoubleArray(),
        "bts_insertion_average_${RandomString.randomString(10)}",
        "Average iterations (green) vs. Expected iterations (red) for BST insertion"
    )
}

fun worstInsertionPerformanceAnalysis() {
    val sizes = DoubleArray(1000) { it.toDouble() + 1 }
    val actualIterations: ArrayList<Double> = ArrayList()
    val expectedIterations = DoubleArray(sizes.size) { it.toDouble() + 1 }

    for (size in sizes) {
        val bst = BinarySearchTree()

        var worstComparisons = 0
        (1..size.toInt()).forEach {
            bst.insert(it)
            worstComparisons = max(bst.totalIterationsOfLastInsert, worstComparisons)
        }

        val expectedComparisons = size

        println("Maximum number of comparisons for input size $size: $worstComparisons")
        println("Expected number of comparisons for input size $size: $expectedComparisons")

        actualIterations.add(worstComparisons.toDouble())
    }

    plotIterations(
        sizes,
        expectedIterations,
        actualIterations.toDoubleArray(),
        "bts_insertion_worst_${RandomString.randomString(10)}",
        "Maximum iterations (green) vs. Expected iterations (red) for BST insertion"
    )
}

fun generateRandomArray(size: Int): IntArray {
    val array = IntArray(size) { it + 1 }
    array.shuffle()
    return array
}

private fun plotIterations(
    sizes: DoubleArray,
    expectedIterations: DoubleArray,
    actualIterations: DoubleArray,
    filename: String,
    title: String,
) {

    val data = mapOf<String, Any>(
        "x_expected" to sizes,
        "y_expected" to expectedIterations,
        "x_actual" to sizes,
        "y_actual" to actualIterations,
    )

    val plot = ggplot(data.toMap()) +
            geomLine(color = "green") { x = "x_expected"; y = "y_expected" } +
            geomLine(color = "red") { x = "x_actual"; y = "y_actual" } +
            scaleXContinuous(name = "BTS Size") +
            scaleYContinuous(name = "Iterations") +
            ggtitle(title)

    ggsave(plot, "${filename}.png")
}
