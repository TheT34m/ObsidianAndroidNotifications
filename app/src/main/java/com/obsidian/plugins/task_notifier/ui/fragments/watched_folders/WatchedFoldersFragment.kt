package com.obsidian.plugins.task_notifier.ui.fragments.watched_folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.obsidian.plugins.task_notifier.databinding.WatchedFoldersFragmentBinding
import com.obsidian.plugins.task_notifier.os.PermissionManager
import com.obsidian.plugins.task_notifier.os.PersistenceManager

class WatchedFoldersFragment : Fragment() {
  private var _binding: WatchedFoldersFragmentBinding? = null
  private val binding get() = _binding!!
  private var addFolderButton: FloatingActionButton? = null
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
    //val folders = PersistenceManager.getWatchedFolders(this.context!!)
    PersistenceManager.getWatchedFolders().subscribe {
      folders.clear()
      folders.addAll((it))
      if(adapter !== null){
        adapter!!.notifyDataSetChanged()
      }
    }
    adapter = WatchedFoldersListAdapter(this.context!!, folders)
    binding.watchedFoldersRecyclerView.layoutManager = LinearLayoutManager(this.activity)
    binding.watchedFoldersRecyclerView.adapter = adapter

    binding.watchedFoldersAddButton.setOnClickListener {
      PermissionManager.askPermissionForFolder(this.activity!!)
    }
  }
}
