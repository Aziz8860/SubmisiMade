package com.cakrapand.submisimade.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.cakrapand.core.data.Resource
import com.cakrapand.core.domain.model.Users
import com.cakrapand.submisimade.R
import com.cakrapand.submisimade.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(USER)

        if (username != null) {
            supportActionBar?.title = username
            setupPager(username)
            setupDetailContent(username)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    private fun setupDetailContent(username: String) {
        detailViewModel.getUserDetail(username).observe(this) { user ->
            if (user != null) {
                when (user) {
                    is Resource.Loading -> {
                        stateLoading(true)
                        stateEmpty(false)
                        stateError(false)
                    }

                    is Resource.Success -> {
                        if (user.data != null) {
                            stateLoading(false)
                            stateEmpty(false)

                            binding.apply {
                                Glide.with(this@DetailActivity)
                                    .load(user.data!!.avatarUrl)
                                    .into(icUser)

                                tvNameGithub.text = user.data!!.name
                                tvUsernameGithub.text = user.data!!.login
                                tabs.getTabAt(0)?.text = "${user.data!!.followers} Followers"
                                tabs.getTabAt(1)?.text = "${user.data!!.following} Following"
                                setStatusFavorite(user.data!!)
                            }
                            stateError(false)
                        } else {
                            stateEmpty(true)
                        }
                    }

                    is Resource.Error -> {
                        stateError(true)
                        stateLoading(false)
                        stateEmpty(false)
                    }
                }
            }
        }
    }

    private fun setStatusFavorite(user: Users) {
        detailViewModel.getFavoriteIsExists(user.login!!).observe(this) { isFavorite ->
            if (isFavorite) {
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_favorite
                    )
                )
            } else {
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_favorite_border
                    )
                )
            }

            binding.fab.setOnClickListener {
                if (isFavorite) {
                    showToast("${user.login} dihapus dari favorite")
                    detailViewModel.deleteUserFavorite(user)
                } else {
                    showToast("${user.login} ditambahkan ke favorite")
                    detailViewModel.insertUserFavorite(user)
                }
            }

        }
    }

    private fun setupPager(username: String) {
        val sectionPagerAdapter = DetailAdapter(this@DetailActivity, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showToast(message: String) {
        Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
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

    companion object {
        const val USER = "user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers_number, R.string.following_number
        )
    }

}
