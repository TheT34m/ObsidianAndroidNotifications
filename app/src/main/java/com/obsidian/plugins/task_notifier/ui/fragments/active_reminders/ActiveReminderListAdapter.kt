package com.obsidian.plugins.task_notifier.ui.fragments.active_reminders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.obsidian.plugins.task_notifier.R

class ActiveReminderListAdapter(private val activeReminders: ArrayList<String>) :
  RecyclerView.Adapter<ActiveReminderViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveReminderViewHolder {
    val viewLayout = LayoutInflater.from(parent.context).inflate(
      R.layout.active_reminder_item,
      parent, false
    )
    return ActiveReminderViewHolder(viewLayout)
  }

  override fun onBindViewHolder(holder: ActiveReminderViewHolder, position: Int) {
    val item = activeReminders[position]
    holder.text.setText(item)
  }

  override fun getItemCount(): Int {
    return activeReminders.size
  }
}
