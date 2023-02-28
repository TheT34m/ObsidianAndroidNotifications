package com.obsidian.plugins.task_notifier.ui.fragments.active_reminders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.core.bo.ObsidianActiveReminderBO
import com.obsidian.plugins.task_notifier.databinding.ActiveRemindersFragmentBinding
import com.obsidian.plugins.task_notifier.os.PermissionManager
import com.obsidian.plugins.task_notifier.os.PersistenceManager
import com.obsidian.plugins.task_notifier.ui.fragments.RecyclerViewEmptyObserver

class ActiveRemindersFragment : Fragment() {
  private var _binding: ActiveRemindersFragmentBinding? = null
  private val binding get() = _binding!!
  private var activeReminders = arrayListOf<ObsidianActiveReminderBO>()
  private var adapter: ActiveReminderListAdapter? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = ActiveRemindersFragmentBinding.inflate(inflater, container, false)
    val view = binding.root
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    PersistenceManager.getActiveReminders().subscribe {
      activeReminders.clear()
      activeReminders.addAll((it))
      if(adapter != null){
        adapter!!.notifyDataSetChanged()
      }
    }
    adapter = ActiveReminderListAdapter(this.context!!, activeReminders)
    binding.activeRemindersRecyclerView.layoutManager = LinearLayoutManager(this.activity)
    binding.activeRemindersRecyclerView.adapter = adapter

    binding.activeRemindersRecyclerView.setOnClickListener {
      PermissionManager.askPermissionForFolder(this.activity!!)
    }

    val emptyDataObserver = RecyclerViewEmptyObserver(binding.activeRemindersRecyclerView, binding.emptyDataParent.root, R.mipmap.no_alarm_foreground, "No task alarm scheduled yet", "Create tasks with date in your obisidian vault (e.g.: `- [ ] my task (@2022-02-22 19:00)`!")
    binding.activeRemindersRecyclerView.adapter?.registerAdapterDataObserver(emptyDataObserver)
  }
}
