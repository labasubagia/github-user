package com.example.githubsearch.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearch.R
import com.example.githubsearch.adapter.UserListAdapter
import com.example.githubsearch.model.User
import com.example.githubsearch.util.UtilView.setInfoView
import com.example.githubsearch.util.UtilView.setInfoViewAsErrorView
import com.example.githubsearch.util.UtilView.showView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.app_name)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


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
            rv_users
        )
        // views to hide when request data
        val viewsAfterData = ArrayList<View>().apply {
            addAll(viewsExistData)
            addAll(viewsInfo)
        }


        // recycler view adapter
        val userListAdapter = UserListAdapter().apply {
            notifyDataSetChanged()
            // item view on click listener
            setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
                // on click: move to detail fragment and send username
                override fun onItemClicked(user: User) {
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                        .apply {
                            username = user.login.toString()
                        }.let {
                            findNavController().navigate(it)
                        }
                }
            })
        }
        // recycler view
        rv_users.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }


        // search view
        sv_user.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // on submit: send search user request
            override fun onQueryTextSubmit(query: String?): Boolean {
                showView(viewsBeforeData)
                showView(viewsAfterData, false)
                viewModel.searchUsers(query as String)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })

        // get close btn from search view
        val searchCloseButtonId =
            sv_user.resources.getIdentifier("android:id/search_close_btn", null, null)
        val searchCloseButton: ImageView = sv_user.findViewById(searchCloseButtonId)

        // customize search view close button behaviour
        // to remove data when data exist
        searchCloseButton.apply {
            // remove wave effect when btn onclick
            background = null

            // modify close btn onclick
            setOnClickListener {

                // set query manually, because this ability gone
                sv_user.setQuery("", false)

                // only when data exist
                if (!info_view.isVisible) {

                    // show info view
                    showInfoView()

                    // clear list user, remove recycler view
                    userListAdapter.setUsers(arrayListOf())
                    showView(viewsExistData, false)
                }
            }
        }


        showInfoView()


        // home view model
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)


        // get view model data
        viewModel.apply {

            // get users that found from search
            getFoundUsers().observe(viewLifecycleOwner, Observer { foundUsers ->
                foundUsers?.let {
                    if (it.total_count == 0) {
                        setInfoView(
                            info_view,
                            R.drawable.ic_undraw_no_data,
                            R.string.user_not_found
                        )
                        showView(viewsInfo)
                    } else {
                        userListAdapter.setUsers(it.items)
                        showView(viewsExistData)
                        showView(viewsInfo, false)
                    }
                    showView(viewsBeforeData, false)
                }
            })

            // get error
            getError().observe(viewLifecycleOwner, Observer {
                it?.let {
                    setInfoViewAsErrorView(info_view, it)
                    showView(viewsInfo)
                    showView(viewsBeforeData, false)
                }
            })
        }
    }

    // show info view for search
    private fun showInfoView() {
        setInfoView(info_view, R.drawable.ic_undraw_people_search, R.string.search_user_info)
        showView(info_view)
    }
}
