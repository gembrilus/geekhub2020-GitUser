package iv.nakonechnyi.gituser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import iv.nakonechnyi.gituser.R
import iv.nakonechnyi.gituser.di.GitViewModelFactory
import iv.nakonechnyi.gituser.common.showErrorMessage
import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.ui.users.DetailedFragment
import kotlinx.android.synthetic.main.activity_main.*
import iv.nakonechnyi.gituser.ui.users.ListFragment
import iv.nakonechnyi.gituser.ui.adapters.GitUsersAdapter
import iv.nakonechnyi.gituser.ui.repos.ReposFragment
import iv.nakonechnyi.gituser.ui.users.EmptyFragment

class MainActivity : AppCompatActivity(),
    GitUsersAdapter.OnClickListener,
    DetailedFragment.OnReposShowClickListener {

    private var mPosition: Int? = null
        get() = model.position.value

    private val dualPane get() = fragment_detailed_container != null

    private val model by lazy {
        ViewModelProvider(this, GitViewModelFactory.factory(applicationContext))
            .get(GitViewModel::class.java)
    }

    private val fm by lazy { supportFragmentManager }

    private val listFragment by lazy {
        ListFragment.newInstance()
    }

    private val detailedFragment
        get() = mPosition?.let { DetailedFragment.newInstance(it) } ?: EmptyFragment()

    private lateinit var reposFragment: ReposFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        model.refresh(null)

        login?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val login = if (v.text.isEmpty()) null else v.text.toString()
                model.refresh(login)
                true
            } else false
        }

        model.position.observe(this, Observer {
            mPosition = it
        })

        model.failed.observe(this, Observer {
            showErrorMessage(this, it)
        })

        model.noNetwork.observe(this, Observer { isAvailable ->
            tw?.let { view ->
                if (isAvailable) {
                    view.visibility = View.VISIBLE
                } else view.visibility = View.GONE
            }
        })

        val transaction = fm.beginTransaction()
        with(transaction) {
            if (savedInstanceState == null) {
                replace(R.id.fragment_main_container, listFragment)
            }

            if (dualPane) {
                if (fm.backStackEntryCount > 0) fm.popBackStack()
                replace(R.id.fragment_detailed_container, detailedFragment)
            }
            commit()
        }
    }

    override fun onClick(position: Int) {
        model.setPosition(position)
        replaceFragment(detailedFragment)
    }

    override fun onReposShow(repos: List<GitRepo>) {
        reposFragment = ReposFragment.newInstance(repos)
        replaceFragment(reposFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fm.beginTransaction()
        if (fm.backStackEntryCount > 0) fm.popBackStack()
        with(transaction) {
            if (dualPane) {
                replace(R.id.fragment_detailed_container, fragment)
                addToBackStack(null)
            } else {
                replace(R.id.fragment_main_container, fragment)
                addToBackStack(null)
            }
            commit()
        }
    }
}
