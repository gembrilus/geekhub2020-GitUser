package iv.nakonechnyi.gituser.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iv.nakonechnyi.gituser.R
import iv.nakonechnyi.gituser.common.toHumanReadableDateString
import iv.nakonechnyi.gituser.entities.GitRepo
import kotlinx.android.synthetic.main.recycler_repos.view.*

class GitReposAdapter(private var repos: List<GitRepo>) :
    RecyclerView.Adapter<GitReposAdapter.GitReposViewHolder>() {

    inner class GitReposViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            val context = itemView.context
            itemView.setOnClickListener {
                context.let { ctx ->
                    val title = ctx.getString(R.string.title_of_browser_chooser)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repos[adapterPosition].url))
                    val chooser = Intent.createChooser(intent, title)
                    if (intent.resolveActivity(ctx.packageManager) != null) {
                        context.startActivity(chooser)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).inflate(R.layout.recycler_repos, parent, false)
            .run(::GitReposViewHolder)

    override fun getItemCount() = repos.size

    override fun onBindViewHolder(holder: GitReposViewHolder, position: Int) {
        val repo = repos[position]
        with(holder.itemView) {
            repoId.text = (position + 1).toString()
            created.text =
                context.getString(R.string.created_at_date, toHumanReadableDateString(repo.created))
            updated.text =
                context.getString(R.string.updated_at_date, toHumanReadableDateString(repo.updated))
            name.text = repo.name
            description.text = repo.description
            language.text = context.getString(R.string.written_on_language, repo.language)
            size.text = context.getString(R.string.total_size_of_repo, repo.size)
        }
    }
}