package com.github.hongbeomi.flickrcodelab.utils

class Pager {

    var currentPage = START_PAGE_INDEX
        private set

    suspend fun load(predicate: suspend (Int) -> Boolean?) {
        val isSuccess = predicate(currentPage)
        if (isSuccess == true) {
            increment()
        }
    }

    private fun increment() {
        currentPage++
    }

    fun reset() {
        currentPage = START_PAGE_INDEX
    }

    companion object {
        private const val START_PAGE_INDEX = 1
    }

}