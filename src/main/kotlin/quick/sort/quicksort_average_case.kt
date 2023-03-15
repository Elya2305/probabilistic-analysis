package quick.sort

import jetbrains.datalore.base.random.RandomString.randomString
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.ggplot
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.scale.scaleXContinuous
import jetbrains.letsPlot.scale.scaleYContinuous
import kotlin.math.log2
import kotlin.random.Random

/**
 * Given a random array of size n.
 * The code shows that the worst case of quicksort with random pivot is O(n*log(n))
 * Also a plot "Comparison Counter vs. Expected Counter" is generated (can be found in /lets-plot-images/quicksort_average_<rand>.png)
 * */

fun main() {
    val sizes = doubleArrayOf(10.0, 100.0, 200.0, 300.0, 400.0, 500.0, 1000.0, 2000.0, 3000.0)
    val actualCounters: ArrayList<Double> = ArrayList()

    for (n in sizes) {
        var totalIterations = 0L
        val numTests = 10
        for (i in 1 until numTests + 1) {
            val arr = IntArray(n.toInt()) { Random.nextInt(100) }
            val (_, comparisons) = quickSort(arr, 0, n.toInt() - 1)
            totalIterations += comparisons
        }
        val avgIterations = totalIterations.toDouble() / numTests
        actualCounters.add(avgIterations)

        println("Average number of comparisons for input size $n: $avgIterations")
        println("Expected number of comparisons for input size $n: ${n * log2(n)}")
    }

    plotCounters(sizes, actualCounters.toDoubleArray())
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
    val pivotIndex = Random.nextInt(low, high + 1)
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

private fun plotCounters(sizes: DoubleArray, actualCounters: DoubleArray) {
    val expectedCounters = DoubleArray(sizes.size) { i -> (sizes[i] * log2(sizes[i])) }

    val data = mapOf<String, Any>(
        "x_expected" to sizes,
        "y_expected" to expectedCounters,
        "x_actual" to sizes,
        "y_actual" to actualCounters,
    )

    val plot = ggplot(data.toMap()) +
            geomLine(color = "green") { x = "x_expected"; y = "y_expected" } +
            geomLine(color = "red") { x = "x_actual"; y = "y_actual" } +
            scaleXContinuous(name = "Array Size") +
            scaleYContinuous(name = "Comparison Counter") +
            ggtitle("Comparison Counter (green) vs. Expected Counter (red) for QuickSort")

    ggsave(plot, "quicksort_average_${randomString(10)}.png")
}