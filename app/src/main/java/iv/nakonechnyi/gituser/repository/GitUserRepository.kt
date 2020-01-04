package iv.nakonechnyi.gituser.repository

import android.content.Context
import android.util.Log
import iv.nakonechnyi.gituser.repository.db.GitDb
import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos
import iv.nakonechnyi.gituser.repository.gitservice.GitUserInfoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitUserRepository private constructor(
    private val db: GitDb,
    private val service: GitUserInfoService,
    private val netManager: NetManager
) {

    private val dao by lazy { db.gitUserReposDao() }

    private val isNetworkAvailable get() = netManager.isNetworkAvailable

    suspend fun getFullUserInfo(userName: String?): Status<GitUserWithRepos> {
        val user = userName?.let{findUserInDb(it)}
        return when {
            userName == null -> {
                Log.i("REPOSITORY", "NO_USERNAME__LOAD_ALL_USERS")
                Status.Success(getAllSavedUsers())
            }
            user != null -> {
                Log.i("REPOSITORY", "USER_EXIST__LOAD_USER_FROM_DB")
                Status.Success(listOf(user))
            }
            !isNetworkAvailable -> {
                Log.i("REPOSITORY", "NO_USERNAME_AND_NO_NETWORK__LOAD_ALL_USERS")
                Status.NetworkFailed(getAllSavedUsers())
            }
            else -> {
                Log.i("REPOSITORY", "LOAD_USER_FROM_NET")
                fetchFromNet(userName)
            }
        }
    }

    fun closeDb() = db.close()

    private suspend fun fetchFromNet(userName: String) =
        try {
            val user = gitUserInfo(userName)
            val repos = gitReposByUsername(userName)
            val userWithRepos = GitUserWithRepos(user, repos)
            Log.i("LOAD_USER_FROM_NET", "USER_WITH_REPOS_CREATED")
            putUserToDb(userWithRepos)
            Log.i("LOAD_USER_FROM_NET", "USER_WITH_REPOS_PUTTED_TO_DB")
            Status.Success(listOf(GitUserWithRepos(user, repos)))

        } catch (e: Throwable) {
            Log.i("LOAD_USER_FROM_NET", e.message)
            Status.Failed<GitUserWithRepos>(e)

        }

    suspend fun removeUserFromDb(login: String) =
        dao.delete(login)

    private suspend fun gitUserInfo(userName: String) =
        service.gitUserInfo(userName)

    private suspend fun gitReposByUsername(userName: String) =
        service.gitReposByUsername(userName)

    private suspend fun getAllSavedUsers() =
        dao.getAllSavedUsers()

    private suspend fun putUserToDb(user: GitUserWithRepos) =
        dao.insert(user)

    private suspend fun findUserInDb(login: String) =
        dao.findByLogin(login)

    companion object {

        @Volatile
        private var INSTANCE: GitUserRepository? = null

        fun get(context: Context) =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: GitUserRepository(
                            GitDb.database(context),
                            GitUserInfoService(context),
                            NetManager(context)
                        ).also {
                            INSTANCE = it
                        }
                }
    }
}