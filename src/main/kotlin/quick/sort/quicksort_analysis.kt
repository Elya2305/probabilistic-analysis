package quick.sort

import jetbrains.datalore.base.random.RandomString
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.ggplot
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.scale.scaleXContinuous
import jetbrains.letsPlot.scale.scaleYContinuous
import kotlin.math.log2
import kotlin.random.Random


fun main() {
    worstPerformanceAnalysis()
    averagePerformanceAnalysis()
}

private fun quickSort(arr: IntArray, low: Int, high: Int): Pair<IntArray, Long> {
    if (low < high) {
        var (iterations, pIndex) = partition(arr, low, high)

        val (_, leftIterations) = quickSort(arr, low, pIndex - 1)
        val (_, rightIterations) = quickSort(arr, pIndex + 1, high)
        iterations += leftIterations + rightIterations
        return Pair(arr, iterations)
    }
    if (low == high) { // to cover last element from right tree
        return Pair(arr, 1)
    }
    return Pair(arr, 0)
}

// -> pair<iterations, index>
private fun partition(arr: IntArray, low: Int, high: Int): Pair<Long, Int> {
    var iterations = 1L
    val pivotIndex = high
    val pivot = arr[pivotIndex]

    var i = low - 1
    for (j in low until high) {
        iterations++
        if (arr[j] <= pivot) {
            i++
            swap(arr, i, j)
        }
    }
    swap(arr, i + 1, pivotIndex)
    return Pair(iterations, i + 1)
}

private fun swap(arr: IntArray, i: Int, j: Int) {
    val temp = arr[i]
    arr[i] = arr[j]
    arr[j] = temp
}

private fun worstPerformanceAnalysis() {
    val sizes = DoubleArray(1000) { it.toDouble() + 1 }
    val expectedIterations = DoubleArray(sizes.size) {
        val n = it + 1
        ((n * (n + 1)) / 2).toDouble()
    }
    val actualIterations: ArrayList<Double> = ArrayList()

    for (n in sizes) {
        var totalIterations = 0L
        val numTests = 10
        for (i in 1 until numTests + 1) {
            val arr = IntArray(n.toInt()) { Random.nextInt(100) }.sortedArray()
            val (_, comparisons) = quickSort(arr, 0, n.toInt() - 1)
            totalIterations += comparisons
        }
        val avgIterations = totalIterations.toDouble() / numTests
        actualIterations.add(avgIterations)
        println("Average number of comparisons for input size $n: $avgIterations")
        println("Expected number of comparisons for input size $n: ${(n * (n + 1)) / 2} \n")
    }

    plotIterations(
        sizes,
        expectedIterations,
        actualIterations.toDoubleArray(),
        "quicksort_worst_${RandomString.randomString(10)}"
    )
}

private fun averagePerformanceAnalysis() {
    val sizes = DoubleArray(3000) { it.toDouble() + 1 }
    val actualIterations: ArrayList<Double> = ArrayList()
    val expectedIterations = DoubleArray(sizes.size) { i -> (sizes[i] * log2(sizes[i])) }

    for (n in sizes) {
        var totalIterations = 0L
        val numTests = 10
        for (i in 1 until numTests + 1) {
            val array = generateRandomArray(n.toInt())
            val (_, comparisons) = quickSort(array, 0, n.toInt() - 1)
            totalIterations += comparisons
        }
        val avgIterations = totalIterations.toDouble() / numTests
        actualIterations.add(avgIterations)

        println("Average number of comparisons for input size $n: $avgIterations")
        println("Expected number of comparisons for input size $n: ${n * log2(n)}")
    }

    plotIterations(
        sizes,
        expectedIterations,
        actualIterations.toDoubleArray(),
        "quicksort_average_${RandomString.randomString(10)}"
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
            scaleXContinuous(name = "Array size") +
            scaleYContinuous(name = "Iterations") +
            ggtitle("Actual iterations (green) vs. Expected iterations (red) for quicksort")

    ggsave(plot, "${filename}.png")
}

