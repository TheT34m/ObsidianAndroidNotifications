package com.obsidian.plugins.task_notifier.ui.fragments.watched_folders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore

class WatchedFoldersListAdapter(
  private val context: Context,
  private val watchedFolders: List<String>
) :
  RecyclerView.Adapter<WatchedFolderViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchedFolderViewHolder {
    val viewLayout = LayoutInflater.from(parent.context).inflate(
      R.layout.watched_folder_item,
      parent, false
    )
    return WatchedFolderViewHolder(viewLayout)
  }

  override fun onBindViewHolder(holder: WatchedFolderViewHolder, position: Int) {
    val item = watchedFolders[position]
    holder.folderPathTextView.text = item
    holder.removeButton.setOnClickListener {
      ObsidianTaskReminderCore.removeFolder(context, item)
    }
  }

  override fun getItemCount(): Int {
    return watchedFolders.size
  }
}
