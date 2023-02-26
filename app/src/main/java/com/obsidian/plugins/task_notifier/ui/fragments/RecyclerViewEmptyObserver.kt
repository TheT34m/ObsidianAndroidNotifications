package com.obsidian.plugins.task_notifier.ui.fragments

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.utils.Logger

class RecyclerViewEmptyObserver constructor(
  rv: RecyclerView?,
  ev: View?,
  icon: Int,
  title: String,
  subtitle: String
) :
  RecyclerView.AdapterDataObserver() {

  private var emptyView: View? = null
  private var recyclerView: RecyclerView? = null
  private var imageView: ImageView? = null
  private var titleTextView: TextView? = null
  private var subtitleTextView: TextView? = null

  init {
    recyclerView = rv
    emptyView = ev
    emptyView!!.visibility = View.GONE
    recyclerView!!.visibility = View.VISIBLE
    imageView = emptyView!!.findViewById<ImageView>(R.id.empty_data_image)
    titleTextView =  emptyView!!.findViewById<TextView>(R.id.empty_data_title)
    subtitleTextView = emptyView!!.findViewById<TextView>(R.id.empty_data_subtitle)

    imageView!!.setImageResource(icon)
    titleTextView!!.text = title
    subtitleTextView!!.text = subtitle

    syncViews()
  }

  private fun syncViews() {
    //Logger.info("RecyclerViewEmptyObserver.Sync views")
    if (emptyView == null) return
    if (recyclerView == null) return
    if (recyclerView!!.adapter == null) return

    val isEmpty = recyclerView!!.adapter!!.itemCount == 0
    //Logger.info("RecyclerViewEmptyObserver.Sync isEmpty $isEmpty")

    if (isEmpty) {
      emptyView!!.visibility = View.VISIBLE
      recyclerView!!.visibility = View.GONE
    } else {
      emptyView!!.visibility = View.GONE
      recyclerView!!.visibility = View.VISIBLE
    }
  }

  override fun onChanged() {
    super.onChanged()
    syncViews()
  }

  override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
    super.onItemRangeChanged(positionStart, itemCount)
    syncViews()
  }
}
