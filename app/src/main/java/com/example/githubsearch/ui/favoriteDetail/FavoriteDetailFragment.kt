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
import com.example.githubsearch.activity.main.MainActivity
import com.example.githubsearch.adapter.FollowPagerAdapter
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.ui.detail.DetailFragmentArgs
import com.example.githubsearch.util.Util
import com.example.githubsearch.util.UtilView.showView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_detail.*

class FavoriteDetailFragment : Fragment() {

    private lateinit var viewModel: FavoriteDetailViewModel
    private var userToDelete: UserDetail? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOptionMenu()
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

        showView(arrayListOf(fab_add, info_view, scroll_view), false)
        showView(progress_bar)

        // navigation arguments
        val username = DetailFragmentArgs.fromBundle(arguments as Bundle).username

        // set follow tabs
        val tabFollowPagerAdapter =
            FollowPagerAdapter(context as Context, childFragmentManager).apply {
                setUsername(username)
            }
        tab_follow_pager.adapter = tabFollowPagerAdapter
        tab_follow.setupWithViewPager(tab_follow_pager)

        // view model
        viewModel = ViewModelProvider(this).get(FavoriteDetailViewModel::class.java)

        // get user detail
        viewModel.searchDetail(username)

        viewModel.apply {

            userDetail.observe(viewLifecycleOwner, Observer { user ->
                user?.let {

                    userToDelete = user

                    // modify some data just for view
                    val login = "@${it.login}"
                    val notApplicable = getString(R.string.not_applicable)

                    // set data to views
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

                    showView(scroll_view)
                    showView(progress_bar, false)
                }
            })

            isDeleteSuccess.observe(viewLifecycleOwner, Observer {
                it?.let { status ->

                    // set message
                    var message = getString(R.string.message_delete_favorite_failed)
                    if (status) {
                        message = getString(R.string.message_delete_favorite_success)
                    }

                    // message
                    Snackbar.make(view as View, message, Snackbar.LENGTH_LONG).show()

                    // remove status
                    viewModel.isDeleteSuccess.postValue(null)

                    // back
                    if (status) {
                        findNavController().navigateUp()
                    }
                }
            })
        }

    }


    private fun setOptionMenu() {
        val activity = activity as? MainActivity
        activity?.title = getString(R.string.page_favorite_detail)
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(context).apply {
            setTitle(getString(R.string.dialog_delete_title))
            setMessage(getString(R.string.dialog_delete_message))
            setPositiveButton(getString(R.string.dialog_yes)) { _: DialogInterface?, _: Int ->
                userToDelete?.let {
                    viewModel.deleteFavorite(it)
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
