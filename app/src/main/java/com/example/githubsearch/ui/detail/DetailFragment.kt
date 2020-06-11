package com.example.githubsearch.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.githubsearch.R
import com.example.githubsearch.activity.main.MainActivity
import com.example.githubsearch.adapter.FollowPagerAdapter
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.util.Util.numberFormat
import com.example.githubsearch.util.UtilView.setInfoViewAsErrorView
import com.example.githubsearch.util.UtilView.showView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_detail.*

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    // user to add to favorite
    private var detail: UserDetail? = null

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
        val viewsInfo = arrayListOf<View>(
            info_view
        )
        // views to show when request data
        val viewsBeforeData = arrayListOf<View>(
            progress_bar
        )
        // views to show after data received and data exist
        val viewsExistData = arrayListOf<View>(
            scroll_view
        )
        // views to hide when request data
        val viewsAfterData = arrayListOf<View>().apply {
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
        showView(fab_add, false)
        showView(viewsAfterData, false)


        // detail view model
        viewModel = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)

        // get view model data
        viewModel.apply {

            // user's detail
            getRemoteDetail(username).observe(viewLifecycleOwner, Observer { user ->
                user?.let {

                    // add user local data, for add to favorite
                    detail = user

                    // modify some data just for view
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

                    if (progress_bar.isGone || progress_bar.isInvisible) {
                        // check user is favorite user or not
                        viewModel.checkLocalFavorite(username)
                    }
                }
            })

            // error
            error.observe(viewLifecycleOwner, Observer {
                it?.let {
                    setInfoViewAsErrorView(info_view, it)
                    showView(viewsInfo)
                    showView(viewsBeforeData, false)
                }
            })

            // check user is favorite
            foundUserFavorite.observe(viewLifecycleOwner, Observer {
                if (it != null || progress_bar.isVisible) {
                    showView(fab_add, false)
                } else {
                    showView(fab_add)
                }
            })

            // check add favorite success
            isInsertSuccess.observe(viewLifecycleOwner, Observer {
                it?.let {
                    val message: String

                    if (it) {
                        message = getString(R.string.message_add_favorite_success)
                        showView(fab_add, false)
                    } else message = getString(R.string.message_add_favorite_failed)

                    Snackbar.make(view as View, message, Snackbar.LENGTH_LONG)
                        .show()

                    // reset status
                    viewModel.isInsertSuccess.postValue(null)
                }
            })
        }

        fab_add.setOnClickListener {
            detail?.let {
                viewModel.addLocalFavorite(it)
            }
        }
    }
}
