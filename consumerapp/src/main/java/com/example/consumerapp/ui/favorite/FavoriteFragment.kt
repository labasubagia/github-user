package com.example.consumerapp.ui.favorite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.adapter.FavoriteListAdapter
import com.example.consumerapp.model.UserDetail
import com.example.consumerapp.util.UtilSharedPreference.loadPreferenceSettings
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
        activity?.title = getString(R.string.page_favorite_list)
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        loadPreferenceSettings(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                findNavController().navigate(R.id.action_favoriteFragment_to_preferenceFragment)
                return true
            }
            else -> true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initViewModel()

        // OnRequest
        showView(progress_bar)
    }


    /*
    * Init ViewModel
    * Request -> favoriteUser
    * Observe -> favoriteUser
    * */
    private fun initViewModel() {

        // ViewModel with Factory
        val factory = FavoriteViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)


        // Request Favorite User
        viewModel.setUser()


        // Observe
        viewModel.apply {

            // Observe Favorite User
            users.observe(viewLifecycleOwner, Observer {

                // When Empty
                // Show InfoView
                if (it.isEmpty()) {
                    setInfoView(
                        info_view,
                        R.drawable.ic_undraw_no_data,
                        R.string.user_not_found
                    )
                    showView(info_view)

                    showView(rv_users, false)
                    listAdapter.setUsers(ArrayList())
                }
                // When Not Empty
                // Show RecyclerView
                else {
                    listAdapter.setUsers(it)
                    showView(rv_users)
                    showView(info_view, false)
                }

                showView(progress_bar, false)
            })
        }
    }


    /*
    * Init RecyclerView
    * OnClick -> Navigate to FavoriteDetail
    * */
    private fun initRecyclerView() {

        // Adapter
        listAdapter = FavoriteListAdapter().apply {
            notifyDataSetChanged()

            // On Item Click
            // Navigate to FavoriteDetailFragment
            // Param username
            setOnItemClickCallback(object : FavoriteListAdapter.OnItemClickCallback {

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
}
