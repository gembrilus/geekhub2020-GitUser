package iv.nakonechnyi.gituser.ui.users

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import iv.nakonechnyi.gituser.R
import iv.nakonechnyi.gituser.ui.repos.ReposFragment
import iv.nakonechnyi.gituser.common.toHumanReadableDateString
import iv.nakonechnyi.gituser.common.underline
import iv.nakonechnyi.gituser.di.GitViewModelFactory
import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.ui.GitViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.fragment_details.view.login

class DetailedFragment : Fragment() {

    companion object {

        private val DetailsFragmentKey = "iv.nakonechnyi.gituser_arguments_key"

        fun newInstance(position: Int?) = DetailedFragment().apply {
            arguments = Bundle().apply {
                position?.let {
                    putInt(DetailsFragmentKey, it)
                }
            }
        }
    }

    private val position get() = arguments?.getInt(DetailsFragmentKey, 0) ?: 0

    private var listener: OnReposShowClickListener? = null

    private val model by lazy {
        activity?.let {
            ViewModelProvider(it, GitViewModelFactory.factory(it.applicationContext))
                .get(GitViewModel::class.java)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as OnReposShowClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = bind()

    private fun bind() {
        val userWithRepos = model?.success?.value?.get(position)
        val user = userWithRepos?.user
        val repositories = userWithRepos?.repos ?: emptyList()
        user?.let {
            with(view!!) {

                created.text = context.getString(
                    R.string.created_at_date,
                    toHumanReadableDateString(user.created)
                )
                updated.text = context.getString(
                    R.string.updated_at_date,
                    toHumanReadableDateString(user.updated)
                )

                Glide.with(context)
                    .load(user.avatarUrl)
                    .into(avatar)

                setOnClickAction(avatar, Intent(Intent.ACTION_VIEW, Uri.parse(user.url)))

                login.text = user.login
                name.text = user.name

                setInfo(company, user.company)
                setInfo(location, user.location)
                setInfo(bio, user.bio)
                setInfo(site, user.site)
                setInfo(email, user.email)

                repos.text = context.getString(R.string.repositories, user.publicRepos)
                follower.text = context.getString(R.string.followers, user.followers)
                following.text = context.getString(R.string.followings, user.following)

                user.site?.let { siteUrl ->
                    site.underline()
                    setOnClickAction(site, Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl)))
                }

                user.email?.let {
                    email.underline()
                    setOnClickAction(email, Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(it))
                        type = "message/rfc822"
                    })
                }

                with(btn_repos) {
                    visibility = if (repositories.isNullOrEmpty()) View.GONE
                    else View.VISIBLE
                    setOnClickListener {
                        listener?.onReposShow(repositories)
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun setOnClickAction(view: View, intent: Intent) {
        context?.let { ctx ->
            view.setOnClickListener {
                val title = ctx.getString(R.string.title_of_browser_chooser)
                val chooser = Intent.createChooser(intent, title)
                if (intent.resolveActivity(ctx.packageManager) != null) {
                    startActivity(chooser)
                }
            }
        }
    }

    private fun setInfo(v: TextView, value: String?) {
        with(v) {
            if (value.isNullOrEmpty()) visibility = View.GONE
            else {
                visibility = View.VISIBLE
                text = value
            }
        }
    }

    interface OnReposShowClickListener {
        fun onReposShow(repos: List<GitRepo>)
    }

}