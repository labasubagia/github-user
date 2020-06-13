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
import com.example.githubsearch.adapter.FollowPagerAdapter
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.util.Util.numberFormat
import com.example.githubsearch.util.UtilFragment.showBackToHomeOptionMenu
import com.example.githubsearch.util.UtilView.setInfoViewAsErrorView
import com.example.githubsearch.util.UtilView.showView
import com.example.githubsearch.widget.FavoriteUserWidget
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_detail.*

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var username: String
    private var detail: UserDetail? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.page_detail)
        showBackToHomeOptionMenu(this)
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onDestroy() {
        showBackToHomeOptionMenu(this, false)
        super.onDestroy()
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

        // Navigation Args
        username = DetailFragmentArgs.fromBundle(arguments as Bundle).username

        initTabsView()
        initViewModel()
        initFab()

        // OnRequest
        showView(progress_bar)
        showView(arrayListOf(fab_add, scroll_view, info_view), false)
    }

    /*
    * Init TabView with TabPager
    * */
    private fun initTabsView() {
        // Set TabPager Adapter
        tab_follow_pager.adapter =
            FollowPagerAdapter(context as Context, childFragmentManager).apply {
                setUsername(username)
            }
        // Add TabPager to TabView
        tab_follow.setupWithViewPager(tab_follow_pager)
    }

    /*
    * Init User Information to UI
    * Modify Information -> username */
    private fun initUserInformation(user: UserDetail?) {
        user?.let {

            // Modify Some Information
            val login = "@${it.login}"
            val notApplicable = getString(R.string.not_applicable)

            // Set UI Information
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
        }
    }

    /*
    * Init FAB
    * OnClick -> ViewModel Add Favorite
    * */
    private fun initFab() {
        fab_add.setOnClickListener {
            detail?.let {
                viewModel.addLocalFavorite(it)
            }
        }
    }

    /*
    * Init ViewModel
    * Observe -> getRemoteDetail, error, userFavorite, isInserted
    * */
    private fun initViewModel() {

        // Init
        viewModel = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)

        // Observe
        viewModel.apply {

            // Remote User Detail (API)
            // If user is favorite Fab Hide
            getRemoteDetail(username).observe(viewLifecycleOwner, Observer { user ->
                user?.let {

                    detail = user

                    // Set User To UI
                    initUserInformation(detail)

                    // Show User Detail UI
                    showView(scroll_view)
                    showView(arrayListOf(progress_bar, info_view), false)

                    // Check User is Favorite
                    if (progress_bar.isGone || progress_bar.isInvisible) {
                        viewModel.checkLocalFavorite(username)
                    }
                }
            })

            // Observe Error
            error.observe(viewLifecycleOwner, Observer {

                // OnError Show ErrorInfoView
                it?.let {
                    setInfoViewAsErrorView(info_view, it)
                    showView(info_view)
                    showView(progress_bar, false)
                }
            })

            // Observe isFavorite
            // Hide FAB when user is Favorite
            userFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
                if (isFavorite != null || progress_bar.isVisible) {
                    showView(fab_add, false)
                } else {
                    showView(fab_add)
                }
            })

            // Observe isInserted
            // OnSuccess -> ViewModel checkLocalFavorite, Update Widget
            isInserted.observe(viewLifecycleOwner, Observer {
                it?.let {
                    val message: String

                    if (it) {
                        message = getString(R.string.message_add_favorite_success)
                        viewModel.checkLocalFavorite(detail?.login as String)

                        // Update Widget
                        FavoriteUserWidget.sendRefreshBroadcast(requireContext())
                    } else
                        message = getString(R.string.message_add_favorite_failed)

                    Snackbar.make(view as View, message, Snackbar.LENGTH_LONG).show()

                    // reset status
                    viewModel.isInserted.postValue(null)
                }
            })
        }
    }
}
