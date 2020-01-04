package iv.nakonechnyi.gituser.ui.repos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iv.nakonechnyi.gituser.R
import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.ui.adapters.GitReposAdapter
import kotlinx.android.synthetic.main.fragment_repos.view.*

class ReposFragment : Fragment() {

    private var repositories: List<GitRepo> = emptyList()

    companion object {

        fun newInstance(repos: List<GitRepo>): ReposFragment {
            return ReposFragment().apply {
                repositories = repos
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  = inflater.inflate(R.layout.fragment_repos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            with(recycler_repos) {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter = GitReposAdapter(repositories)
            }
        }

    }
}
