package com.obsidian.plugins.task_notifier.ui.fragments.watched_folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.databinding.EmptyDatasetBinding
import com.obsidian.plugins.task_notifier.databinding.WatchedFoldersFragmentBinding
import com.obsidian.plugins.task_notifier.os.PermissionManager
import com.obsidian.plugins.task_notifier.os.PersistenceManager
import com.obsidian.plugins.task_notifier.ui.fragments.RecyclerViewEmptyObserver

class WatchedFoldersFragment : Fragment() {
  private var _binding: WatchedFoldersFragmentBinding? = null
  private val binding get() = _binding!!
  private var folders = arrayListOf<String>()
  private var adapter: WatchedFoldersListAdapter? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = WatchedFoldersFragmentBinding.inflate(inflater, container, false)
    val view = binding.root
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    PersistenceManager.getWatchedFolders().subscribe {
      folders.clear()
      folders.addAll((it))
      if (adapter !== null) {
        adapter!!.notifyDataSetChanged()
      }
    }
    adapter = WatchedFoldersListAdapter(this.context!!, folders)
    binding.watchedFoldersRecyclerView.layoutManager = LinearLayoutManager(this.activity)
    binding.watchedFoldersRecyclerView.adapter = adapter
    binding.watchedFoldersAddButton.setOnClickListener {
      PermissionManager.askPermissionForFolder(this.activity!!)
    }

    val emptyDataObserver = RecyclerViewEmptyObserver(binding.watchedFoldersRecyclerView, binding.emptyDataParent.root, R.mipmap.no_folder_foreground, "Add your obsidian vault reminder plugin config file!", "Tap on the add icon on the bottom and find YOUR_VAULT/.obisidan/plugins/obisidian-reminder-plugin/data.json")
    binding.watchedFoldersRecyclerView.adapter?.registerAdapterDataObserver(emptyDataObserver)
  }
}
