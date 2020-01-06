package iv.nakonechnyi.gituser.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import iv.nakonechnyi.gituser.repository.GitUserRepository
import iv.nakonechnyi.gituser.repository.NetManager
import iv.nakonechnyi.gituser.repository.db.GitDb
import iv.nakonechnyi.gituser.repository.gitservice.GitUserInfoService
import iv.nakonechnyi.gituser.ui.GitViewModel

@Suppress("UNCHECKED_CAST")
class GitViewModelFactory(private val repository: GitUserRepository)
    : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GitViewModel(repository) as T
    }

    companion object {

        fun factory(context: Context) =
            GitViewModelFactory(
                GitUserRepository.get(
                    GitDb.database(context),
                    GitUserInfoService(),
                    NetManager(context)
                )
            )

    }

}