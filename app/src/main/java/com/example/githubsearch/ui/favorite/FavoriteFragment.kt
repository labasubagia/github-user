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
import com.example.githubsearch.adapter.FavoriteListAdapter
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.util.UtilFragment.showBackToHomeOptionMenu
import com.example.githubsearch.util.UtilView.setInfoView
import com.example.githubsearch.util.UtilView.showView
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var listAdapter: FavoriteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.page_favorite_list)
        showBackToHomeOptionMenu(this)
        return inflater.inflate(R.layout.fragment_favorite, container, false)
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
        initRecyclerView()
        initViewModel()

        showView(progress_bar)
    }


    /*
    * Init RecyclerView
    * OnItemClick -> Navigation to FavoriteDetailFragment
    * */
    private fun initRecyclerView() {

        // Adapter
        listAdapter = FavoriteListAdapter().apply {
            notifyDataSetChanged()

            // OnItemClick
            setOnItemClickCallback(object : FavoriteListAdapter.OnItemClickCallback {

                // Move to FavoriteDetailFragment
                // Param username
                override fun onItemClicked(user: UserDetail) =
                    FavoriteFragmentDirections.actionFavoriteFragmentToFavoriteDetailFragment()
                        .apply {
                            username = user.login
                        }.let {
                            findNavController().navigate(it)
                        }
            })
        }

        // Settings
        rv_users.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }

    /*
    * Init ViewModel
    * Observe -> favorites*/
    private fun initViewModel() {

        // Init
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        // Observe
        viewModel.apply {

            // Observe Favorites User List
            favorites.observe(viewLifecycleOwner, Observer {
                it.let { list ->

                    // When List Empty
                    // Show InfoView
                    if (list.isEmpty()) {
                        setInfoView(
                            info_view,
                            R.drawable.ic_undraw_no_data,
                            R.string.user_not_found
                        )
                        showView(info_view)

                        showView(rv_users, false)
                        listAdapter.setUsers(ArrayList())

                    }

                    // When List NotEmpty
                    // Show List
                    else {
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
}
