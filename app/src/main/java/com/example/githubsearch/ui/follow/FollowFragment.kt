package com.example.githubsearch.ui.follow


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearch.R
import com.example.githubsearch.adapter.UserListAdapter
import com.example.githubsearch.util.Util.showView
import kotlinx.android.synthetic.main.fragment_follow.*

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
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // views to show when request data
        val viewsBeforeData: ArrayList<View> = arrayListOf(
            progress_bar
        )
        // views to show when data received but empty
        val viewsEmptyData: ArrayList<View> = arrayListOf(
            tv_follow_empty
        )
        // views to show when data received and not empty
        val viewsExistData: ArrayList<View> = arrayListOf(
            rv_users
        )
        // views to hide when request data
        val viewsAfterData = ArrayList<View>().apply {
            addAll(viewsEmptyData)
            addAll(viewsExistData)
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


        // customize based on type
        fun setUsers() = when (type) {
            TYPE_FOLLOWERS -> viewModel.setUserFollower(username.toString())
            TYPE_FOLLOWING -> viewModel.setUserFollowing(username.toString())
            else -> null
        }

        fun getUsers() = when (type) {
            TYPE_FOLLOWERS -> viewModel.getUserFollowers()
            TYPE_FOLLOWING -> viewModel.getUserFollowing()
            else -> null
        }
        tv_follow_empty.text = when (type) {
            TYPE_FOLLOWERS -> getString(R.string.follower_zero)
            TYPE_FOLLOWING -> getString(R.string.following_zero)
            else -> null
        }.toString()


        // run function
        setUsers()
        showView(viewsBeforeData)
        showView(viewsAfterData, false)
        getUsers()?.observe(this, Observer {
            it?.let {
                if (it.size == 0) {
                    showView(viewsEmptyData)
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
                Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
                showView(viewsAfterData, false)
            }
        })
    }

}
