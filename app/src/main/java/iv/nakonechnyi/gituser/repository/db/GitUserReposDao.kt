package iv.nakonechnyi.gituser.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.entities.GitUser

@Dao
interface GitUserReposDao {
    @Transaction
    suspend fun insert(user: GitUserWithRepos){
        insert(user.user)
        user.repos.forEach { insert(it) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: GitUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repo: GitRepo)

    @Transaction @Query("SELECT * FROM users WHERE login = :login")
    suspend fun findByLogin(login: String): GitUserWithRepos

    @Transaction @Query("SELECT * FROM users")
    suspend fun getAllSavedUsers(): List<GitUserWithRepos>

    @Query("DELETE FROM users WHERE login = :login")
    suspend fun deleteUser(login: String)

    @Query("DELETE FROM repos WHERE login = :login")
    suspend fun deleteRepos(login: String)

    @Transaction
    suspend fun delete(login: String) {
        deleteUser(login)
        deleteRepos(login)
    }
}