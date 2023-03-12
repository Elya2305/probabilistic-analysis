/*
* pivot = last element
* */

fun main() {
    val arr = intArrayOf(21, 90, 61)
    quickSort(arr, 0, arr.size - 1)
    println(arr.contentToString())
}

/* The main function that implements QuickSort
              arr[] --> Array to be sorted,
              low --> Starting index,
              high --> Ending index
     */
private fun quickSort(arr: IntArray, low: Int, high: Int) {
    if (low < high) {

        // pi is partitioning index, arr[p]
        // is now at right place
        val pi = partition(arr, low, high)

        // Separately sort elements before
        // partition and after partition
        quickSort(arr, low, pi - 1)
        quickSort(arr, pi + 1, high)
    }
}

/* This function takes last element as pivot, places
       the pivot element at its correct position in sorted
       array, and places all smaller (smaller than pivot)
       to left of pivot and all greater elements to right
       of pivot */
fun partition(arr: IntArray, low: Int, high: Int): Int {

    // pivot
    val pivotIndex = high
    val pivot = arr[pivotIndex]

    // Index of smaller element and
    // indicates the right position
    // of pivot found so far
    var i = low - 1
    for (j in low until high) {

        // If current element is smaller
        // than the pivot
        if (arr[j] < pivot) {

            // Increment index of
            // smaller element
            i++
            swap(arr, i, j)
        }
    }
    swap(arr, i + 1, pivotIndex)
    return i + 1
}

// A utility function to swap two elements
fun swap(arr: IntArray, i: Int, j: Int) {
    val temp = arr[i]
    arr[i] = arr[j]
    arr[j] = temp
}
