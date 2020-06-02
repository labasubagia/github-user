package com.example.githubsearch.ui.follow


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearch.R
import com.example.githubsearch.adapter.UserListAdapter
import com.example.githubsearch.util.UtilView.setInfoView
import com.example.githubsearch.util.UtilView.showView
import kotlinx.android.synthetic.main.follow_fragment.*

class FollowFragment : Fragment() {

    companion object {
        const val TYPE_FOLLOWERS = "type_follower"
        const val TYPE_FOLLOWING = "type_following"

        fun newInstance(type: String, username: String): FollowFragment {
            return FollowFragment().apply {
                this.type = type
                this.username = username
            }
        }
    }

    private var type: String? = null
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.follow_fragment, container, false)
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
        // views to show when data received and not empty
        val viewsExistData: ArrayList<View> = arrayListOf(
            rv_users
        )
        // views to hide when request data
        val viewsAfterData = ArrayList<View>().apply {
            addAll(viewsExistData)
            addAll(viewsInfo)
        }


        // set users adapter
        val adapter = UserListAdapter().apply {
            notifyDataSetChanged()
        }
        rv_users.layoutManager = LinearLayoutManager(context)
        rv_users.adapter = adapter


        // use view model
        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)


        // set users request type
        fun setUsers() = when (type) {
            TYPE_FOLLOWERS -> viewModel.setUserFollower(username.toString())
            TYPE_FOLLOWING -> viewModel.setUserFollowing(username.toString())
            else -> null
        }

        // set get users type
        fun getUsers() = when (type) {
            TYPE_FOLLOWERS -> viewModel.getUserFollowers()
            TYPE_FOLLOWING -> viewModel.getUserFollowing()
            else -> null
        }

        // set info view resources
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


        // run function
        setUsers()
        showView(viewsBeforeData)
        showView(viewsAfterData, false)
        getUsers()?.observe(this, Observer {
            it?.let {
                if (it.size == 0) {
                    setInfoView(info_view, imgInfoResource, textInfoResource)
                    showView(viewsInfo)
                } else {
                    adapter.setUsers(it)
                    showView(viewsExistData)
                }
            }
            showView(viewsBeforeData, false)
        })

        // get error
        viewModel.getErrorMessageInt().observe(this, Observer {
            it?.let {
                setInfoView(info_view, R.drawable.ic_undraw_warning, it)
                showView(viewsInfo)
                showView(viewsBeforeData, false)
            }
        })
    }
}
