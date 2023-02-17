package com.obsidian.plugins.task_notifier.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore
import com.obsidian.plugins.task_notifier.databinding.ActivityMainBinding
import com.obsidian.plugins.task_notifier.os.PermissionManager
import com.obsidian.plugins.task_notifier.os.PersistenceManager
import com.obsidian.plugins.task_notifier.ui.fragments.ViewPagerAdapter
import com.obsidian.plugins.task_notifier.utils.Logger


class MainActivity : AppCompatActivity() {

  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    PermissionManager.ensureStoragePermission(this)
    ObsidianTaskReminderCore.init(this)

    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val viewPager = binding.viewPager
    val tabLayout = binding.tabLayout

    val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
    viewPager.adapter = adapter

    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
      tab.text = adapter.getTabs()[position]
    }.attach()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    PermissionManager.onActivityResult(this, requestCode, resultCode, data)
  }
}
