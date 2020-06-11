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
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearch.R
import com.example.githubsearch.adapter.UserListAdapter
import com.example.githubsearch.model.User
import com.example.githubsearch.util.Util.getDefaultLanguage
import com.example.githubsearch.util.Util.setLanguage
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        settingLanguage()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // views that can show before or after data
        val viewsInfo = arrayListOf<View>(
            info_view
        )
        // views to show when request data
        val viewsBeforeData = arrayListOf<View>(
            progress_bar
        )
        // views to show after data received and data exist
        val viewsExistData = arrayListOf<View>(
            rv_users
        )
        // views to hide when request data
        val viewsAfterData = arrayListOf<View>().apply {
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
                viewModel.search(query as String)
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
            found.observe(viewLifecycleOwner, Observer { foundUsers ->
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
            error.observe(viewLifecycleOwner, Observer {
                it?.let {
                    setInfoViewAsErrorView(info_view, it)
                    showView(viewsInfo)
                    showView(viewsBeforeData, false)
                }
            })
        }
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

    // show info view for search
    private fun showInfoView() {
        setInfoView(info_view, R.drawable.ic_undraw_people_search, R.string.search_user_info)
        showView(info_view)
    }

    private fun settingLanguage() {
        // read from shared preference
        // if not set, use system language (default english)
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        val language = preference.getString(
            getString(R.string.preference_language_key),
            getDefaultLanguage(requireContext())
        )
        setLanguage(requireContext(), language as String)
    }
}
