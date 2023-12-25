package com.cakrapand.submisimade.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakrapand.core.data.Resource
import com.cakrapand.core.domain.model.Users
import com.cakrapand.core.ui.UserAdapter
import com.cakrapand.submisimade.R
import com.cakrapand.submisimade.databinding.FragmentFollowsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowsBinding
    private lateinit var adapter: UserAdapter
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAdapter()

        var position = 0
        var username = ""

        arguments?.let {
            position = it.getInt(POSITION, 0)
            username = it.getString(USERNAME).toString()
        }

        if (position == 1) {
            viewModel.getUserFollowers(username).observe(viewLifecycleOwner) {
                showData(it)
            }
        } else {
            viewModel.getUserFollowing(username).observe(viewLifecycleOwner) {
                showData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvFollow.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onPause() {
        super.onPause()
        binding.rvFollow.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0
        )
    }

    private fun setupAdapter() {
        adapter = UserAdapter()
        binding.apply {
            rvFollow.layoutManager = LinearLayoutManager(requireActivity())
            rvFollow.adapter = adapter
            rvFollow.setHasFixedSize(false)
        }
    }

    private fun showData(data: Resource<List<Users>>?) {
        if (data != null) {
            when (data) {
                is Resource.Loading -> {
                    stateEmpty(false)
                    stateError(false)
                }

                is Resource.Success -> {
                    if (data.data.isNullOrEmpty()) {
                        stateEmpty(true)
                    } else {
                        stateEmpty(false)
                        adapter.submitList(data.data)
                        adapter.onItemClick = { selectedData ->
                            val intent = Intent(requireActivity(), DetailActivity::class.java)
                            intent.putExtra(DetailActivity.USER, selectedData.login)
                            startActivity(intent)
                        }
                    }
                    stateError(false)
                }

                is Resource.Error -> {
                    stateEmpty(false)
                    stateError(true)
                }
            }
        }
    }


    private fun stateEmpty(isEmpty: Boolean) {
        if(isEmpty) {
            binding.tvState.visibility = View.VISIBLE
        } else {
            binding.tvState.visibility = View.GONE
        }

        binding.tvState.text = getString(R.string.empty_search)
    }

    private fun stateError(isError: Boolean) {
        if(isError) {
            binding.tvState.visibility = View.VISIBLE
        } else {
            binding.tvState.visibility = View.GONE
        }

        binding.tvState.text = getString(R.string.error_state)
    }

    companion object {
        const val POSITION = "position_number"
        const val USERNAME = "username"
    }
}