package com.obsidian.plugins.task_notifier.ui.fragments.active_reminders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.obsidian.plugins.task_notifier.R

class ActiveReminderViewHolder(item: View) : RecyclerView.ViewHolder(item) {
  val text: TextView  = item.findViewById(R.id.active_reminder_item_time_textview)
}
