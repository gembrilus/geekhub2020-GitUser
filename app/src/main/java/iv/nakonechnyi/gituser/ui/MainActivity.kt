package iv.nakonechnyi.gituser.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import iv.nakonechnyi.gituser.R
import iv.nakonechnyi.gituser.common.showErrorMessage
import iv.nakonechnyi.gituser.di.GitViewModelFactory
import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.ui.adapters.GitUsersAdapter
import iv.nakonechnyi.gituser.ui.repos.ReposFragment
import iv.nakonechnyi.gituser.ui.users.DetailedFragment
import iv.nakonechnyi.gituser.ui.users.EmptyFragment
import iv.nakonechnyi.gituser.ui.users.ListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(),
    GitUsersAdapter.OnClickListener,
    DetailedFragment.OnReposShowClickListener {

    private val model by lazy {
        ViewModelProvider(this, GitViewModelFactory.factory(applicationContext))
            .get(GitViewModel::class.java)
    }

    private var mPosition: Int? = null
        get() = model.position.value

    private val dualPane get() = fragment_detailed_container != null

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

        model.refresh(model.login.value)

        login?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val userName = if (v.text.isEmpty()) null else v.text.toString()
                model.setLogin(userName)

                hideSoftKeyboard()

                true
            } else false
        }

        model.position.observe(this, Observer {
            mPosition = it
        })

        model.toolbarViewStatus.observe(this, Observer {
            it ?: return@Observer
            when (it) {
                ToolbarViewStatus.WITH_INPUT -> login?.visibility = View.VISIBLE
                ToolbarViewStatus.WITHOUT_INPUT -> login?.visibility = View.GONE
            }
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

        showContent(savedInstanceState)

    }

    override fun onClick(position: Int) {
        model.setPosition(position)
        replaceFragment(detailedFragment)
    }

    override fun onReposShow(repos: List<GitRepo>) {
        reposFragment = ReposFragment.newInstance(repos)
        replaceFragment(reposFragment)
    }

    private fun showContent(savedInstanceState: Bundle?){
        val transaction = fm.beginTransaction()
        with(transaction) {
            if (savedInstanceState == null) {
                replace(R.id.fragment_main_container, listFragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }

            if (dualPane) {
                if (fm.backStackEntryCount > 0) fm.popBackStack()
                replace(R.id.fragment_detailed_container, detailedFragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
            commit()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fm.beginTransaction()
        with(transaction) {
            if (dualPane) {
                if (fm.backStackEntryCount > 0) fm.popBackStack()
                replace(R.id.fragment_detailed_container, fragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                addToBackStack(null)
            } else {
                replace(R.id.fragment_main_container, fragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                addToBackStack(null)
            }
            commit()
        }
    }

    private fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
