package com.example.githubsearch.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.githubsearch.R
import com.example.githubsearch.activity.main.MainActivity
import com.example.githubsearch.adapter.FollowPagerAdapter
import com.example.githubsearch.util.Util.numberFormat
import com.example.githubsearch.util.UtilView.setInfoViewAsErrorView
import com.example.githubsearch.util.UtilView.showView
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val activity = activity as? MainActivity

        // change action bar title
        activity?.title = getString(R.string.page_detail)

        // show back to home
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onDestroyView() {

        val activity = activity as? MainActivity

        // remove back to home
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(false)

        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // back to home
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // navigation arguments
        val username = DetailFragmentArgs.fromBundle(arguments as Bundle).username

        // views that can show before or after data
        val viewsInfo: ArrayList<View> = arrayListOf(
            info_view
        )
        // views to show when request data
        val viewsBeforeData: ArrayList<View> = arrayListOf(
            progress_bar
        )
        // views to show after data received and data exist
        val viewsExistData: ArrayList<View> = arrayListOf(
            scroll_view
        )
        // views to hide when request data
        val viewsAfterData = ArrayList<View>().apply {
            addAll(viewsExistData)
            addAll(viewsInfo)
        }

        // set follow tabs
        val tabFollowPagerAdapter =
            FollowPagerAdapter(context as Context, childFragmentManager).apply {
                setUsername(username)
            }
        tab_follow_pager.adapter = tabFollowPagerAdapter
        tab_follow.setupWithViewPager(tab_follow_pager)


        // set view visibility when request data
        showView(viewsBeforeData)
        showView(viewsAfterData, false)


        // detail view model
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        // request user detail
        viewModel.setUserDetail(username)


        // get view model data
        viewModel.apply {

            // user's detail
            getUserDetail().observe(viewLifecycleOwner, Observer { user ->
                user?.let {

                    val login = "@${it.login}"
                    val notApplicable = getString(R.string.not_applicable)

                    // set data to views
                    Glide.with(this@DetailFragment)
                        .load(it.avatar_url)
                        .placeholder(R.drawable.ic_undraw_profile_pic)
                        .error(R.drawable.ic_undraw_profile_pic)
                        .into(img_avatar)
                    tv_name.text = it.name
                    tv_username.text = login
                    tv_repositories.text = numberFormat(it.public_repos)
                    tv_followers.text = numberFormat(it.followers)
                    tv_following.text = numberFormat(it.following)
                    tv_location.text = it.location ?: notApplicable
                    tv_company.text = it.company ?: notApplicable

                    // set views visibility after data received
                    showView(viewsBeforeData, false)
                    showView(viewsExistData)
                }
            })

            // error
            getError().observe(viewLifecycleOwner, Observer {
                it?.let {
                    setInfoViewAsErrorView(info_view, it)
                    showView(viewsInfo)
                    showView(viewsBeforeData, false)
                }
            })
        }
    }
}
