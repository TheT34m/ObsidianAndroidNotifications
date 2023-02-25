package com.obsidian.plugins.task_notifier.ui.fragments.active_reminders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore
import com.obsidian.plugins.task_notifier.core.bo.ObsidianActiveReminderBO
import com.obsidian.plugins.task_notifier.utils.LocalDateTimeDeserializer

class ActiveReminderListAdapter(
  private val context: Context, private val activeReminders: ArrayList<ObsidianActiveReminderBO>
) : RecyclerView.Adapter<ActiveReminderViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveReminderViewHolder {
    val viewLayout = LayoutInflater.from(parent.context).inflate(
      R.layout.active_reminder_item, parent, false
    )
    return ActiveReminderViewHolder(viewLayout)
  }

  override fun onBindViewHolder(holder: ActiveReminderViewHolder, position: Int) {
    val item = activeReminders[position]
    holder.timeTextView.text = item.dateTime.format(LocalDateTimeDeserializer.FORMATTER)
    holder.titleTextView.text = item.title
    holder.removeButton.setOnClickListener {
      ObsidianTaskReminderCore.removeActiveReminder(context, item)
    }
  }

  override fun getItemCount(): Int {
    return activeReminders.size
  }
}
