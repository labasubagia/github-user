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
import com.example.githubsearch.util.UtilView.setInfoViewAsErrorView
import com.example.githubsearch.util.UtilView.showView
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

        // views that can show before or after data
        val viewsInfo = arrayListOf<View>(
            info_view
        )
        // views to show when request data
        val viewsBeforeData = arrayListOf<View>(
            progress_bar
        )
        // views to show when data received and not empty
        val viewsExistData = arrayListOf<View>(
            rv_users
        )
        // views to hide when request data
        val viewsAfterData = arrayListOf<View>().apply {
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
        showView(viewsBeforeData)
        showView(viewsAfterData, false)

        viewModel.apply {
            follow(username as String, type as String).observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it.size == 0) {
                        setInfoView(info_view, imgInfoResource, textInfoResource)
                        showView(viewsInfo)
                    } else {
                        adapter.setUsers(it)
                        showView(viewsExistData)
                    }
                    showView(viewsBeforeData, false)
                }
            })
            error(type as String).observe(viewLifecycleOwner, Observer {
                it?.let {
                    setInfoViewAsErrorView(info_view, it)
                    showView(viewsInfo)
                    showView(viewsBeforeData, false)
                }
            })
        }
    }
}
