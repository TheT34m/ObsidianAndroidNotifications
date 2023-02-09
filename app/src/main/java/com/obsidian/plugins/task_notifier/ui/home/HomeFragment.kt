package com.obsidian.plugins.task_notifier.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.obsidian.plugins.task_notifier.MainActivity
import com.obsidian.plugins.task_notifier.PersistenceManager
import com.obsidian.plugins.task_notifier.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var _attachedContext: Context? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val attachedContext get() = _attachedContext!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val statusValue: TextView = binding.statusValue
        homeViewModel.text.observe(viewLifecycleOwner) {
            statusValue.text = "miafasz"
        }
        _binding?.watchedFoldersValue?.text =
            PersistenceManager.getWatchedFolders(this.attachedContext).joinToString()
        binding.setWatchedFoldersButton.setOnClickListener({
            (activity as MainActivity).getWatchedFolder()
        })
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this._attachedContext = context
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}