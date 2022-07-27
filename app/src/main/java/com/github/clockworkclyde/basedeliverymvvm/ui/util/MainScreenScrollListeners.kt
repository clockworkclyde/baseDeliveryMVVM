package com.github.clockworkclyde.basedeliverymvvm.ui.util

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class MainScreenScrollListener(
    private val manager: GridLayoutManager,
    private val tabLayout: TabLayout,
    private val contentListSize: Int,
    private val anchors: ArrayList<Int>,
    private val context: Context,
) {

    private var isScrolled = false

    inner class RecyclerScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (isScrolled) {
                val firstVisible = manager.findFirstVisibleItemPosition()
                val lastVisible = manager.findLastVisibleItemPosition()

                var pos = 0

                if (lastVisible == contentListSize - 1) pos = anchors.size - 1
                else if (firstVisible == anchors[anchors.size - 1]) pos =
                    anchors[anchors.size - 1]
                else {
                    for (position in anchors.indices) {
                        if (firstVisible == anchors[position]) {
                            pos = position
                        } else if (firstVisible > anchors[position] && firstVisible < anchors[position + 1]) pos =
                            position
                    }
                }

                tabLayout.setScrollPosition(pos, 0f, true)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            isScrolled = newState != 0
        }
    }

    inner class TabLayoutTabListener :
        TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab?) {
            val tabPosition = tab?.position

            if (!isScrolled && tabPosition != null) {
                //
                val smoothScroller: RecyclerView.SmoothScroller =
                    object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_START
                        }
                    }
                smoothScroller.targetPosition = anchors[tabPosition]
                manager.startSmoothScroll(smoothScroller)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
    }
}