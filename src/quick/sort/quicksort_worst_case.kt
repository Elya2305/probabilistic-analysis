package quick.sort

import kotlin.random.Random

/**
 * Given a sorted array of size n.
 * The code shows that the worst case of quicksort is O(n^2) with pivot == maximum in array
 * */

fun main() {
    val sizes = intArrayOf(10, 100, 1000)
    for (n in sizes) {
        var totalIterations = 0L
        val numTests = 10
        for (i in 1 until numTests + 1) {
            val arr = IntArray(n) { Random.nextInt(100) }.sortedArray()
            val (_, comparisons) = quickSort(arr, 0, n - 1)
            totalIterations += comparisons
        }
        val avgIterations = totalIterations.toDouble() / numTests
        println("Average number of comparisons for input size $n: $avgIterations")
        println("Expected number of comparisons for input size $n: ${(n * (n + 1)) / 2} \n")
    }
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
