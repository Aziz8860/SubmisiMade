package com.cakrapand.submisimade.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class DetailAdapter(
    activity: FragmentActivity,
    private var username: String
) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.POSITION, position + 1)
            putString(FollowFragment.USERNAME, username)
        }
        return fragment
    }

}