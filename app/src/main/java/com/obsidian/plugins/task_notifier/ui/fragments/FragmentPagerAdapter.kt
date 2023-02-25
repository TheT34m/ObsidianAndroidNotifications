package com.obsidian.plugins.task_notifier.ui.fragments


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.obsidian.plugins.task_notifier.ui.fragments.active_reminders.ActiveRemindersFragment
import com.obsidian.plugins.task_notifier.ui.fragments.watched_folders.WatchedFoldersFragment

val TABS = arrayOf(
  "⏰ Active Reminders",
  "🔍 Watched Folders"
)

public class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
  FragmentStateAdapter(fragmentManager, lifecycle) {

  fun getTabs(): Array<String> {
    return TABS
  }

  override fun getItemCount(): Int {
    return TABS.size
  }

  override fun createFragment(position: Int): Fragment {
    when (position) {
      0 -> return ActiveRemindersFragment()
      1 -> return WatchedFoldersFragment()
    }
    return ActiveRemindersFragment()
  }
}
