package com.cakrapand.submisimade.ui.main

import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakrapand.core.data.Resource
import com.cakrapand.core.ui.UserAdapter
import com.cakrapand.submisimade.R
import com.cakrapand.submisimade.databinding.ActivityMainBinding
import com.cakrapand.submisimade.ui.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val viewModel: MainViewModel by viewModel()
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        getInitialUser("cakra")
    }

    fun getInitialUser(query: String) {
        viewModel.getUsersByUsername(query).observe(this) { users ->
            when (users) {
                is Resource.Loading -> {
                    stateLoading(true)
                    stateEmpty(false)
                    stateError(false)
                }

                is Resource.Success -> {
                    stateLoading(false)
                    adapter.submitList(users.data)
                    adapter.onItemClick = {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.USER, it.login)
                        startActivity(intent)
                    }
                    stateError(false)
                }

                is Resource.Error -> {
                    stateLoading(false)
                    stateEmpty(false)
                    stateError(true)
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = UserAdapter()
        binding.apply {
            rvFollow.layoutManager = LinearLayoutManager(this@MainActivity)
            rvFollow.adapter = adapter
            rvFollow.setHasFixedSize(true)
        }
    }

    private fun registerBroadCastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_POWER_CONNECTED -> {
                        binding.tvPowerStatus.text = getString(R.string.power_connected)
                    }
                    Intent.ACTION_POWER_DISCONNECTED -> {
                        binding.tvPowerStatus.text = getString(R.string.power_disconnected)
                    }
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        registerBroadCastReceiver()
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(
                    this@MainActivity,
                    Class.forName("com.cakrapand.favorite.ui.FavoriteActivity")
                )
                startActivity(intent)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.queryHint = resources.getString(R.string.search_placeholder)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getInitialUser(query.toString())
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

    private fun stateLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun stateEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.tvState.visibility = View.VISIBLE
        } else {
            binding.tvState.visibility = View.GONE
        }
        binding.tvState.text = getString(R.string.empty_search)
    }

    private fun stateError(isError: Boolean) {
        if (isError) {
            binding.tvState.visibility = View.VISIBLE
        } else {
            binding.tvState.visibility = View.GONE
        }
        binding.tvState.text = getString(R.string.error_state)
    }
}