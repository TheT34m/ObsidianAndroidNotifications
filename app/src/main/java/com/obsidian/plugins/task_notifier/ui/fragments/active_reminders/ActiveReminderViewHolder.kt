package com.obsidian.plugins.task_notifier.ui.fragments.active_reminders

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.obsidian.plugins.task_notifier.R

class ActiveReminderViewHolder(item: View) : RecyclerView.ViewHolder(item) {
  val timeTextView: TextView  = item.findViewById(R.id.active_reminder_item_time_textview)
  val titleTextView: TextView  = item.findViewById(R.id.active_reminder_item_title_textview)

  val removeButton: ImageButton = item.findViewById(R.id.active_reminder_item_remove_button)

}
