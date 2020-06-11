package com.example.githubsearch.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearch.R
import com.example.githubsearch.activity.main.MainActivity
import com.example.githubsearch.adapter.FavoriteListAdapter
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.util.UtilView.setInfoView
import com.example.githubsearch.util.UtilView.showView
import kotlinx.android.synthetic.main.fragment_follow.*

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var listAdapter: FavoriteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }


    override fun onResume() {
        super.onResume()
        val activity = activity as? MainActivity

        // change action bar title
        activity?.title = getString(R.string.page_favorite_list)

        // show back to home
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        val activity = activity as? MainActivity
        // remove back to home
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(false)

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
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        listSetting()

        showView(progress_bar)

        viewModel.apply {
            favorites.observe(viewLifecycleOwner, Observer {
                it.let { list ->
                    if (list.isEmpty()) {
                        setInfoView(
                            info_view,
                            R.drawable.ic_undraw_no_data,
                            R.string.user_not_found
                        )
                        showView(info_view)

                        showView(rv_users, false)
                        listAdapter.setUsers(ArrayList())
                    } else {
                        val arrayList = arrayListOf<UserDetail>()
                        arrayList.addAll(list)
                        listAdapter.setUsers(arrayList)
                        showView(rv_users)

                        showView(info_view, false)
                    }
                    showView(progress_bar, false)
                }
            })
        }
    }

    private fun listSetting() {

        // recycler view adapter
        listAdapter = FavoriteListAdapter().apply {
            notifyDataSetChanged()
            // item view on click listener
            setOnItemClickCallback(object : FavoriteListAdapter.OnItemClickCallback {
                // on click: move to detail fragment and send username
                override fun onItemClicked(user: UserDetail) =
                    FavoriteFragmentDirections.actionFavoriteFragmentToFavoriteDetailFragment()
                        .apply {
                            username = user.login
                        }.let {
                            findNavController().navigate(it)
                        }
            })
        }

        // recycler view settings
        rv_users.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }
}
