package me.joaovictorsl.pokeopeninfo.recyclerview.scrolllistener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationScrollListener(
    private val fetchNext: () -> Unit,
    private val fetchPrev: () -> Unit
) : RecyclerView.OnScrollListener() {
    private val TAG = "PaginationScrollListener"

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        val startOfThirdPart = totalItemCount / 3 * 2
        val thirdPart = startOfThirdPart..totalItemCount
        val shouldFetchNext = firstVisibleItemPosition in thirdPart && dy > 0

        val endOfFirstPart = totalItemCount / 3
        val firstPart = 0..endOfFirstPart
        val shouldFetchPrev = lastVisibleItemPosition in firstPart && dy < 0

        if (shouldFetchNext) fetchNext()
        else if (shouldFetchPrev) fetchPrev()
    }
}