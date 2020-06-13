package com.example.githubsearch.ui.favoriteDetail

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.githubsearch.R
import com.example.githubsearch.adapter.FollowPagerAdapter
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.util.Util
import com.example.githubsearch.util.UtilFragment.showBackToHomeOptionMenu
import com.example.githubsearch.util.UtilView.showView
import com.example.githubsearch.widget.FavoriteUserWidget
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_detail.*

class FavoriteDetailFragment : Fragment() {

    private lateinit var viewModel: FavoriteDetailViewModel
    private lateinit var username: String
    private var detail: UserDetail? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.page_favorite_detail)
        showBackToHomeOptionMenu(this)
        return inflater.inflate(R.layout.fragment_favorite_detail, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // back to home
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            R.id.menu_delete -> {
                showDeleteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // navigation arguments
        username = FavoriteDetailFragmentArgs.fromBundle(arguments as Bundle).username

        initTabsView()
        initViewModel()

        // OnRequest
        showView(arrayListOf(fab_add, info_view, scroll_view), false)
        showView(progress_bar)
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
            Glide.with(this@FavoriteDetailFragment)
                .load(it.avatar_url)
                .placeholder(R.drawable.ic_undraw_profile_pic)
                .error(R.drawable.ic_undraw_profile_pic)
                .into(img_avatar)
            tv_name.text = it.name
            tv_username.text = login
            tv_repositories.text = Util.numberFormat(it.public_repos)
            tv_followers.text = Util.numberFormat(it.followers)
            tv_following.text = Util.numberFormat(it.following)
            tv_location.text = it.location ?: notApplicable
            tv_company.text = it.company ?: notApplicable
        }
    }

    /*
    * Init TabView with TabPager*/
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
    * Init ViewModel
    * Request -> searchDetail (by username)
    * Observe -> userDetail, isDeleted
    * */
    private fun initViewModel() {

        // Init
        viewModel = ViewModelProvider(this).get(FavoriteDetailViewModel::class.java)

        // Request
        viewModel.getDetail(username)

        // Observe
        viewModel.apply {

            // Observe User
            userDetail.observe(viewLifecycleOwner, Observer { user ->
                user?.let {
                    detail = user
                    initUserInformation(detail)
                    showView(scroll_view)
                    showView(progress_bar, false)
                }
            })

            // Observe Delete Success
            isDeleted.observe(viewLifecycleOwner, Observer {
                it?.let { status ->

                    // Show SnackBar with Message based on Status
                    val message =
                        if (status)
                            getString(R.string.message_delete_favorite_success)
                        else
                            getString(R.string.message_delete_favorite_failed)
                    Snackbar.make(view as View, message, Snackbar.LENGTH_LONG).show()

                    // ViewModel nullify isDeleteSuccess
                    viewModel.isDeleted.postValue(null)

                    // When Success
                    // UpdateWidget & Back to Previous Fragment
                    if (status) {
                        FavoriteUserWidget.sendRefreshBroadcast(requireContext())
                        findNavController().navigateUp()
                    }
                }
            })
        }
    }

    /*
    * Delete Dialog
    * OnDelete -> ViewMode deleteFavorite
    * */
    private fun showDeleteDialog() {
        AlertDialog.Builder(context).apply {
            setTitle(getString(R.string.dialog_delete_title))
            setMessage(getString(R.string.dialog_delete_message))
            setPositiveButton(getString(R.string.dialog_yes)) { _: DialogInterface?, _: Int ->
                detail?.let {
                    viewModel.deleteFavorite(it.login)
                }
            }
            setNegativeButton(getString(R.string.dialog_no)) { dialog: DialogInterface?, _: Int ->
                dialog?.cancel()
            }
            create()
            show()
        }
    }
}
