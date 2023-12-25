package com.cakrapand.favorite.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakrapand.core.ui.UserAdapter
import com.cakrapand.favorite.databinding.ActivityFavoriteBinding
import com.cakrapand.favorite.di.favoriteModule
import com.cakrapand.submisimade.ui.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter

    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadKoinModules(favoriteModule)

        setupAdapter()
        setupFavoriteData()
    }

    private fun setupAdapter() {
        adapter = UserAdapter()

        binding.apply {
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.setHasFixedSize(true)
            rvFavorite.adapter = adapter
        }
    }

    private fun setupFavoriteData() {
        viewModel.getAllUserFavorite().observe(this) { users ->
            if (users.isNullOrEmpty()) {
                adapter.submitList(null)
                stateEmpty(true)
                adapter.submitList(null)
            } else {
                stateEmpty(false)
                adapter.submitList(users)
                adapter.onItemClick = { selectedData ->
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.USER, selectedData.login)
                    startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun stateEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.tvState.visibility = View.VISIBLE
        } else {
            binding.tvState.visibility = View.GONE
        }

        binding.tvState.text = "Anda belum memiliki favorite"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}