package com.example.githubsearch.ui.home

import android.os.Bundle
import android.view.*
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
import com.example.githubsearch.util.UtilSharedPreference.loadPreferenceSettings
import com.example.githubsearch.util.UtilView.setInfoView
import com.example.githubsearch.util.UtilView.setInfoViewAsErrorView
import com.example.githubsearch.util.UtilView.showView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.app_name)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        loadPreferenceSettings(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initSearchView()
        initViewModel()

        // On App Open
        showInfoView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
                return true
            }
            R.id.menu_setting -> {
                findNavController().navigate(R.id.action_homeFragment_to_preferenceFragment)
                return true
            }
            else -> true
        }
    }

    private fun showInfoView() {
        setInfoView(info_view, R.drawable.ic_undraw_people_search, R.string.search_user_info)
        showView(info_view)
    }


    /*
    * User Search List
    * Init RecyclerView
    * OnItemClick -> Navigation to DetailFragment
    * */
    private fun initRecyclerView() {

        // Adapter global variable
        userListAdapter = UserListAdapter().apply {
            notifyDataSetChanged()

            // OnItemClick -> Navigation to DetailFragment
            // With param username
            setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {

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

        // Settings
        rv_users.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
    }


    /*
    * Init SearchView
    * OnSubmit -> ViewModel Search User
    * CloseBtn Clicked -> RecyclerView.Adapter Clear
    * */
    private fun initSearchView() {

        // Listener
        sv_user.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // OnSubmit -> ViewModel send search user request
            override fun onQueryTextSubmit(query: String?): Boolean {
                showView(progress_bar)
                showView(arrayListOf(info_view, rv_users), false)
                viewModel.search(query as String)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })

        // Find CloseBtn in SearchView
        val searchCloseButtonId =
            sv_user.resources.getIdentifier("android:id/search_close_btn", null, null)
        val searchCloseButton: ImageView = sv_user.findViewById(searchCloseButtonId)

        // CloseBtn Customize
        searchCloseButton.apply {

            // Remove wave effect OnClick
            background = null

            // Onclick CloseBtn -> RecyclerView.Adapter Clear
            setOnClickListener {

                // Remove Query
                sv_user.setQuery("", false)

                // Clear only when RecyclerView data not empty
                if (!info_view.isVisible) {

                    // show info view
                    showInfoView()

                    // Clear and Hide RecyclerView
                    userListAdapter.setUsers(arrayListOf())
                    showView(rv_users, false)
                }
            }
        }
    }

    /*
    * Init ViewModel
    * Observe -> found, error
    * */
    private fun initViewModel() {

        // Init
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)


        // Observe
        viewModel.apply {

            // Observe Found User
            found.observe(viewLifecycleOwner, Observer { users ->
                users?.let {
                    // When Empty
                    // Show InfoView
                    if (it.total_count == 0) {
                        setInfoView(
                            info_view,
                            R.drawable.ic_undraw_no_data,
                            R.string.user_not_found
                        )
                        showView(info_view)
                    }
                    // When Not Empty
                    // Show RecyclerView
                    else {
                        userListAdapter.setUsers(it.items)
                        showView(rv_users)
                        showView(info_view, false)
                    }
                    showView(progress_bar, false)
                }
            })

            // Observe error
            error.observe(viewLifecycleOwner, Observer {

                // OnError Show ErrorInfoView
                it?.let {
                    setInfoViewAsErrorView(info_view, it)
                    showView(info_view)
                    showView(progress_bar, false)
                }
            })
        }
    }

}
