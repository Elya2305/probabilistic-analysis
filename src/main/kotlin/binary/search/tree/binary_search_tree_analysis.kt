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

fun main() {
    worstInsertionPerformanceAnalysis()
    averageInsertionPerformanceAnalysis()
    averageSearchPerformanceAnalysis()
    averageDeletePerformanceAnalysis()
}

class BinarySearchTree {

    inner class Node(var key: Int) {
        var left: Node? = null
        var right: Node? = null
    }

    var root: Node? = null
    var totalIterationsOfLastInsert = 0
    var totalIterationsOfLastSearch = 0
    var totalIterationsOfLastDelete = 0

    fun insert(key: Int) {
        totalIterationsOfLastInsert = 0
        root = insertRec(root, key)
    }

    fun search(key: Int): Boolean {
        totalIterationsOfLastSearch = 0
        val value = searchRec(root, key)
        return value != null
    }

    fun delete(key: Int) {
        totalIterationsOfLastDelete = 0
        root = deleteRec(root, key)
    }

    private fun insertRec(root: Node?, key: Int): Node {
        var _root = root
        totalIterationsOfLastInsert++

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

    private fun searchRec(root: Node?, key: Int): Node? {
        totalIterationsOfLastSearch++
        if (root == null || root.key == key) return root
        return if (root.key > key) {
            searchRec(root.left, key)
        } else {
            searchRec(root.right, key)
        }
    }

    private fun deleteRec(root: Node?, key: Int): Node? {
        totalIterationsOfLastDelete++
        if (root == null) return root

        if (key < root.key) {
            root.left = deleteRec(root.left, key)
        } else if (key > root.key) {
            root.right = deleteRec(root.right, key)
        } else {
            if (root.left == null) {
                return root.right
            } else if (root.right == null) {
                return root.left
            }
            root.key = minValue(root.right)
            root.right = deleteRec(root.right, root.key)
        }
        return root
    }

    private fun minValue(root: Node?): Int {
        var minval = root
        while (minval!!.left != null) {
            minval = minval.left!!
        }
        return minval.key
    }
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

        val averageIterations = totalComparisons.toDouble() / size

        println("Average number of iterations for input size $size: $averageIterations")
        println("Expected number of iterations for input size $size: ${log2(size)}")

        actualIterations.add(averageIterations)
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

        var worstIterations = 0
        (1..size.toInt()).forEach {
            bst.insert(it)
            worstIterations = max(bst.totalIterationsOfLastInsert, worstIterations)
        }

        val expectedComparisons = size

        println("Maximum number of comparisons for input size $size: $worstIterations")
        println("Expected number of comparisons for input size $size: $expectedComparisons")

        actualIterations.add(worstIterations.toDouble())
    }

    plotIterations(
        sizes,
        expectedIterations,
        actualIterations.toDoubleArray(),
        "bts_insertion_worst_${RandomString.randomString(10)}",
        "Maximum iterations (green) vs. Expected iterations (red) for BST insertion"
    )
}

fun averageSearchPerformanceAnalysis() {
    val sizes = DoubleArray(3000) { it.toDouble() + 1 }
    val actualIterations: ArrayList<Double> = ArrayList()
    val expectedIterations = DoubleArray(sizes.size) { i -> (log2(sizes[i])) }

    for (size in sizes) {
        val array = generateRandomArray(size.toInt())

        val bst = BinarySearchTree()
        for (key in array) {
            bst.insert(key)
        }

        var totalComparisons = 0
        for (key in array) {
            bst.search(key)
            totalComparisons += bst.totalIterationsOfLastSearch
        }

        val averageIterations = totalComparisons.toDouble() / size

        println("Average number of iterations for input size $size: $averageIterations")
        println("Expected number of iterations for input size $size: ${log2(size)}")

        actualIterations.add(averageIterations)
    }

    plotIterations(
        sizes,
        expectedIterations,
        actualIterations.toDoubleArray(),
        "bts_search_average_${RandomString.randomString(10)}",
        "Average iterations (green) vs. Expected iterations (red) for BST search"
    )
}

fun averageDeletePerformanceAnalysis() {
    val sizes = DoubleArray(3000) { it.toDouble() + 1 }
    val actualIterations: ArrayList<Double> = ArrayList()
    val expectedIterations = DoubleArray(sizes.size) { i -> (log2(sizes[i])) }

    for (size in sizes) {
        val array = generateRandomArray(size.toInt())

        val bst = BinarySearchTree()
        for (key in array) {
            bst.insert(key)
        }

        var totalComparisons = 0
        for (key in array) {
            bst.delete(key)
            totalComparisons += bst.totalIterationsOfLastDelete
        }

        val averageIterations = totalComparisons.toDouble() / size

        println("Average number of iterations for input size $size: $averageIterations")
        println("Expected number of iterations for input size $size: ${log2(size)}")

        actualIterations.add(averageIterations)
    }

    plotIterations(
        sizes,
        expectedIterations,
        actualIterations.toDoubleArray(),
        "bts_delete_average_${RandomString.randomString(10)}",
        "Average iterations (green) vs. Expected iterations (red) for BST delete"
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
