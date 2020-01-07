package iv.nakonechnyi.gituser.ui.users

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import iv.nakonechnyi.gituser.R
import iv.nakonechnyi.gituser.di.GitViewModelFactory
import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos
import iv.nakonechnyi.gituser.ui.GitItemTouchHelperCallback
import iv.nakonechnyi.gituser.ui.GitViewModel
import iv.nakonechnyi.gituser.ui.ItemTouchHelperAdapter
import iv.nakonechnyi.gituser.ui.ToolbarViewStatus
import iv.nakonechnyi.gituser.ui.adapters.GitUsersAdapter
import kotlinx.android.synthetic.main.fragment_list.*
import okhttp3.internal.userAgent

class ListFragment : Fragment(), ItemTouchHelperAdapter {

    companion object {

        fun newInstance() = ListFragment()

    }

    private val model by lazy {
        activity?.let {
            ViewModelProvider(it, GitViewModelFactory.factory(it.applicationContext))
                .get(GitViewModel::class.java)
        }
    }

    private var mData: List<GitUserWithRepos>? = null

    private lateinit var mAdapter: GitUsersAdapter

    private lateinit var listener: GitUsersAdapter.OnClickListener



    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as GitUsersAdapter.OnClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = GitUsersAdapter(mutableListOf()).apply {
            setClickListener(this@ListFragment.listener)
        }
        val swiper = GitItemTouchHelperCallback(this)
        val touchHelper = ItemTouchHelper(swiper)

        with(recycler_user) {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
            touchHelper.attachToRecyclerView(this)
        }



        model?.success?.observe(this, Observer {
            mData = it
            mAdapter.setItems(it as MutableList<GitUserWithRepos>)
        })
    }

    override fun onResume() {
        super.onResume()
        model?.setToolbarStatus(ToolbarViewStatus.WITH_INPUT)
    }

    override fun onPause() {
        super.onPause()
        model?.setToolbarStatus(ToolbarViewStatus.WITHOUT_INPUT)
    }

    override fun onItemDelete(position: Int) {
        mData?.get(position)?.user?.login?.let { model?.removeItem(it) }
        mAdapter.removeItem(position)
    }
}