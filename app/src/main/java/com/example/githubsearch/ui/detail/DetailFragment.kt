package com.example.githubsearch.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubsearch.R
import com.example.githubsearch.util.Util.showView
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // views to show when request data
        val viewsBeforeData: ArrayList<View> = arrayListOf(
            progress_bar
        )
        // views to show after data received and data exist
        val viewsExistData: ArrayList<View> = arrayListOf(
            img_avatar,
            tv_username,
            tv_username,
            tv_repositories,
            tv_label_repositories,
            tv_followers,
            tv_label_followers,
            tv_following,
            tv_label_following
        )
        // views to show after data received but data is empty
        val viewsEmptyData: ArrayList<View> = arrayListOf()
        // views to hide when request data
        val viewsAfterData = ArrayList<View>().apply {
            addAll(viewsExistData)
            addAll(viewsEmptyData)
        }


        // set view visibility when request data
        showView(viewsBeforeData)
        showView(viewsAfterData, false)


        // detail view model
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)


        // navigation arguments
        val username = DetailFragmentArgs.fromBundle(arguments as Bundle).username

        // request user, followers and following
        viewModel.setUser(username)


        // get view model data
        viewModel.apply {

            // user's detail
            getUserDetail().observe(viewLifecycleOwner, Observer { user ->
                user?.let {

                    val login = "@${it.login}"

                    // set data to views
                    Glide.with(this@DetailFragment)
                        .load(it.avatar_url)
                        .into(img_avatar)
                    tv_name.text = it.name
                    tv_username.text = login
                    tv_repositories.text = it.public_repos.toString()
                    tv_followers.text = it.followers.toString()
                    tv_following.text = it.following.toString()

                    // set views visibility after data received
                    showView(viewsBeforeData, false)
                    showView(viewsExistData)
                }
            })

            // user's follower
            getUserFollowers().observe(viewLifecycleOwner, Observer { followers ->
                followers?.let {
                    Log.d("Followers", it.toString())
                }
            })

            // user's following
            getUserFollowing().observe(viewLifecycleOwner, Observer { following ->
                following.let {
                    Log.d("Followers", it.toString())
                }
            })

            // error
            getErrorMessageInt().observe(viewLifecycleOwner, Observer { messageInt ->
                messageInt?.let {
                    Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
