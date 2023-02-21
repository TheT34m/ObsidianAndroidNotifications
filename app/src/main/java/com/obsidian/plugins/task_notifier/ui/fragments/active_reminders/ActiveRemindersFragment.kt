package com.obsidian.plugins.task_notifier.ui.fragments.active_reminders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.obsidian.plugins.task_notifier.databinding.ActiveRemindersFragmentBinding

class ActiveRemindersFragment : Fragment() {
  private var _binding: ActiveRemindersFragmentBinding? = null
  private val binding get() = _binding!!

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
    binding.activeRemindersRecyclerView.layoutManager = LinearLayoutManager(this.activity)
    binding.activeRemindersRecyclerView.adapter =  ActiveReminderListAdapter(arrayListOf("January", "February", "March"))
  }
}
