package com.example.consumerapp.ui.follow


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.adapter.UserListAdapter
import com.example.consumerapp.util.UtilView.setInfoView
import com.example.consumerapp.util.UtilView.setInfoViewAsErrorView
import com.example.consumerapp.util.UtilView.showView
import kotlinx.android.synthetic.main.fragment_follow.*

class FollowFragment : Fragment() {

    private lateinit var listAdapter: UserListAdapter
    private var typeFragment: String? = null
    private var username: String? = null

    companion object {
        const val TYPE_FOLLOWERS = "type_follower"
        const val TYPE_FOLLOWING = "type_following"

        fun newInstance(type: String, username: String): FollowFragment {
            return FollowFragment().apply {
                this.typeFragment = type
                this.username = username
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initViewModel()

        // OnRequest
        showView(progress_bar)
        showView(arrayListOf(rv_users, info_view), false)
    }


    /*
    * Init RecyclerView -> Followers/Following
    * */
    private fun initRecyclerView() {
        // Adapter
        listAdapter = UserListAdapter().apply {
            notifyDataSetChanged()
        }

        // Settings
        rv_users.apply {
            setHasFixedSize(true)
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    /*
    * Init ViewModel
    * Observe -> follow, error
    * */
    private fun initViewModel() {

        // Init
        val viewModel = ViewModelProvider(this).get(FollowViewModel::class.java)

        // Observe
        viewModel.apply {

            // Observe Follow Based On Type of Fragment
            follow(username as String, typeFragment as String).observe(
                viewLifecycleOwner,
                Observer {

                    it?.let {

                        // When Empty
                        // Show InfoView
                        if (it.size == 0) {
                            showEmptyInfoView(typeFragment)
                        }
                        // When Not Empty
                        // Show RecyclerView
                        else {
                            listAdapter.setUsers(it)
                            showView(rv_users)
                        }

                        showView(progress_bar, false)
                    }
                })

            // Observe Error
            error(typeFragment as String).observe(viewLifecycleOwner, Observer {

                // OnError Show ErrorInfoView
                it?.let {
                    setInfoViewAsErrorView(info_view, it)
                    showView(info_view)
                    showView(progress_bar, false)
                }
            })
        }
    }


    /*
    * Show Empty Info based on Type of Fragment
    * */
    private fun showEmptyInfoView(type: String?) {
        type?.let {
            var imgInfoResource = 0
            var textInfoResource = 0
            when (type) {
                TYPE_FOLLOWERS -> {
                    imgInfoResource = R.drawable.ic_undraw_followers
                    textInfoResource = R.string.follower_zero
                }
                TYPE_FOLLOWING -> {
                    imgInfoResource = R.drawable.ic_undraw_follow_me_drone
                    textInfoResource = R.string.following_zero
                }
            }
            setInfoView(info_view, imgInfoResource, textInfoResource)
            showView(info_view)
        }
    }
}
