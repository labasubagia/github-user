package com.example.consumerapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.adapter.FavoriteListAdapter
import com.example.consumerapp.model.UserDetail
import com.example.consumerapp.ui.FavoriteViewModelFactory
import com.example.consumerapp.util.UtilView.setInfoView
import com.example.consumerapp.util.UtilView.showView
import kotlinx.android.synthetic.main.fragment_favorite.*

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

        // change action bar title
        activity?.title = getString(R.string.page_favorite_list)

        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = FavoriteViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)

        listSetting()

        showView(progress_bar)

        viewModel.setUser()
        viewModel.apply {
            users.observe(viewLifecycleOwner, Observer {
                if (it.isEmpty()) {
                    setInfoView(
                        info_view,
                        R.drawable.ic_undraw_no_data,
                        R.string.user_not_found
                    )
                    showView(info_view)

                    showView(rv_users, false)
                    listAdapter.setUsers(ArrayList())
                } else {
                    listAdapter.setUsers(it)
                    showView(rv_users)
                    showView(info_view, false)
                }
                showView(progress_bar, false)
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
