package iv.nakonechnyi.gituser.repository.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Embedded
import androidx.room.Relation
import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.entities.GitUser

data class GitUserWithRepos(
    @Embedded val user: GitUser,
    @Relation(
        parentColumn = "login",
        entity = GitRepo::class,
        entityColumn = "login"
    )
    val repos: List<GitRepo>
){
    fun toLiveData() = MutableLiveData<GitUserWithRepos>(this) as LiveData<GitUserWithRepos>
}