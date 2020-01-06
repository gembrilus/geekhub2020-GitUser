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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GitUserWithRepos

        if (user != other.user) return false
        if (repos != other.repos) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + repos.hashCode()
        return result
    }
}