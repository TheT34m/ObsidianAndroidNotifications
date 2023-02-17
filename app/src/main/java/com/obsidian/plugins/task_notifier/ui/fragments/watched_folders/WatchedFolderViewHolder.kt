package com.obsidian.plugins.task_notifier.ui.fragments.watched_folders

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.obsidian.plugins.task_notifier.R
import org.w3c.dom.Text

class WatchedFolderViewHolder(item: View) : RecyclerView.ViewHolder(item) {
  val folderPathTextView: TextView  = item.findViewById(R.id.watched_folder_item_folder_path_textview)
  val removeButton: Button = item.findViewById(R.id.watched_folder_item_delete_button)
}
