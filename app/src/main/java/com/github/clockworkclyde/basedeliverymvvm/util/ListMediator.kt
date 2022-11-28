package com.github.clockworkclyde.basedeliverymvvm.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class ListMediator(
    private val recyclerView: RecyclerView,
    private val tabLayout: TabLayout,
    private var anchors: List<Int> = emptyList(),
) {

    private var isAttached: Boolean = false
    private var isTabClicked: Boolean = false
    private var recyclerState = RecyclerView.SCROLL_STATE_IDLE

    private var mTabClickFlag: Boolean = false

    private val smoothScroller: RecyclerView.SmoothScroller =
        object : LinearSmoothScroller(recyclerView.context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

    var tabViewClickListener = TabViewClickListener(tabLayout)

    fun attach() {
        recyclerView.adapter ?: throw RuntimeException("No adapter provided to recycler view")
        if (tabLayout.tabCount == 0) throw RuntimeException("You need to have at least one tab in tabLayout")
        if (anchors.size > tabLayout.tabCount) throw RuntimeException("You have more anchors than tabs in TabLayout")
        notifyAnchorsChanged()
        isAttached = true
    }

    fun detach() {
        clearListeners()
        isAttached = false
    }

    private fun reattach() {
        detach()
        attach()
    }

    fun updateWithAnchors(anchors: List<Int>): ListMediator {
        this.anchors = anchors
        if (isAttached) reattach()
        return this
    }

    private fun clearListeners() {
        recyclerView.clearOnScrollListeners()
        for (i in 0 until tabLayout.tabCount) {
            tabLayout.getTabAt(i)!!.view.setOnClickListener(null)
        }
        for (i in tabViewClickListener.getListeners().indices) {
            tabViewClickListener.getListeners().toMutableList().removeAt(i)
        }
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener)
        recyclerView.removeOnScrollListener(onScrollListener)
    }

    private fun notifyAnchorsChanged() {
        tabViewClickListener.addListener { _, _ -> mTabClickFlag = true }
        tabViewClickListener.build()
        tabLayout.addOnTabSelectedListener(onTabSelectedListener)
        recyclerView.addOnScrollListener(onScrollListener)
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val tabPosition = tab?.position

            if (tabPosition != null) {
                smoothScroller.targetPosition = anchors[tabPosition]
                recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            recyclerState = newState
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                mTabClickFlag = false
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (mTabClickFlag) {
                return
            }

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                ?: throw RuntimeException("No layoutManager attached to this Recycler View")

            var itemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()

            if (itemPosition == -1) {
                itemPosition = layoutManager.findFirstVisibleItemPosition()
            }

            if (recyclerState == RecyclerView.SCROLL_STATE_DRAGGING || recyclerState == RecyclerView.SCROLL_STATE_SETTLING) {
                for (i in anchors.indices) {
                    if (itemPosition == anchors[i]) {
                        if (!tabLayout.getTabAt(i)!!.isSelected) {
                            tabLayout.getTabAt(i)!!.select()
                        }
                        if (layoutManager.findLastCompletelyVisibleItemPosition() == anchors[anchors.size - 1]) {
                            if (!tabLayout.getTabAt(anchors.size - 1)!!.isSelected) {
                                tabLayout.getTabAt(anchors.size - 1)!!.select()
                            }
                            return
                        }
                    }
                }
            }
        }
    }
}