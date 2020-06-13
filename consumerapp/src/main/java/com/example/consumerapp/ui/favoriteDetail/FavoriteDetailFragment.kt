package com.example.consumerapp.ui.favoriteDetail

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
import com.example.consumerapp.R
import com.example.consumerapp.adapter.FollowPagerAdapter
import com.example.consumerapp.model.UserDetail
import com.example.consumerapp.util.Util.numberFormat
import com.example.consumerapp.util.UtilFragment.showBackToHomeOptionMenu
import com.example.consumerapp.util.UtilView.showView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_detail.*

class FavoriteDetailFragment : Fragment() {

    private lateinit var viewModel: FavoriteDetailViewModel
    private lateinit var username: String
    private lateinit var detail: UserDetail

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.page_favorite_detail)
        return inflater.inflate(R.layout.fragment_favorite_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBackToHomeOptionMenu(this)
    }

    override fun onDestroy() {
        showBackToHomeOptionMenu(this, false)
        super.onDestroy()
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

        // Navigation args
        username = FavoriteDetailFragmentArgs.fromBundle(arguments as Bundle).username

        initTabsView()
        initViewModel()

        // OnRequest
        showView(arrayListOf(fab_add, info_view, scroll_view), false)
        showView(progress_bar)
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
    * Request -> searchByUsername
    * Observe -> userDetail,
    * */
    private fun initViewModel() {

        // ViewModel with Factory
        val factory = FavoriteDetailViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(FavoriteDetailViewModel::class.java)

        // Request
        viewModel.search(username)

        // Observe
        viewModel.apply {

            // Observe Detail
            userDetail.observe(viewLifecycleOwner, Observer { user ->
                user?.let {

                    detail = user
                    initUserInformation(detail)

                    // Show UI Information
                    showView(scroll_view)
                    showView(progress_bar, false)
                }
            })

            // Observe deleted
            isDeleted.observe(viewLifecycleOwner, Observer {
                it?.let { status ->

                    // Show SnackBar
                    val message = getString(
                        if (status)
                            R.string.message_delete_favorite_success
                        else
                            R.string.message_delete_favorite_failed

                    )
                    Snackbar.make(view as View, message, Snackbar.LENGTH_LONG).show()

                    // isDeleted nullify value
                    viewModel.isDeleted.postValue(null)

                    // When Success, Back to previous fragment
                    if (status) {
                        findNavController().navigateUp()
                    }
                }
            })
        }
    }


    /*
    * Init User Detail Information to UI
    * */
    private fun initUserInformation(user: UserDetail?) {
        user?.let {

            // Modify Some data
            val login = "@${it.login}"
            val notApplicable = getString(R.string.not_applicable)

            // Set UI data
            Glide.with(this@FavoriteDetailFragment)
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
    * Show Delete Dialog
    * OnDelete -> ViewModel Delete
    * */
    private fun showDeleteDialog() {
        AlertDialog.Builder(context).apply {
            setTitle(getString(R.string.dialog_delete_title))
            setMessage(getString(R.string.dialog_delete_message))
            setPositiveButton(getString(R.string.dialog_yes)) { _: DialogInterface?, _: Int ->
                viewModel.delete(detail)
            }
            setNegativeButton(getString(R.string.dialog_no)) { dialog: DialogInterface?, _: Int ->
                dialog?.cancel()
            }
            create()
            show()
        }
    }
}
