package iv.nakonechnyi.gituser.repository

import iv.nakonechnyi.gituser.repository.db.GitDb
import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos
import iv.nakonechnyi.gituser.repository.gitservice.GitUserInfoService

class GitUserRepository private constructor(
    private val db: GitDb,
    private val service: GitUserInfoService,
    private val netManager: NetManager
) {

    private val dao by lazy { db.gitUserReposDao() }

    private val isNetworkAvailable get() = netManager.isNetworkAvailable

    suspend fun getFullUserInfo(userName: String?): Status<GitUserWithRepos> {
        val user = userName?.let { findUserInDb(it) }
        return when {
            userName == null -> {
                if (isNetworkAvailable){
                    Status.Success(getAllSavedUsers())
                } else {
                    Status.DbSuccess(getAllSavedUsers())
                }
            }
            user != null -> {
                if (isNetworkAvailable){
                    Status.Success(listOf(user))
                } else {
                    Status.DbSuccess(listOf(user))
                }
            }
            isNetworkAvailable ->   fetchFromNet(userName)
            else ->                 Status.NetworkFailed(getAllSavedUsers())
        }
    }

    suspend fun removeUserFromDb(login: String) =
        dao.delete(login)

    fun closeDb() = db.close()

    private suspend fun fetchFromNet(userName: String) =
        try {
            val user = gitUserInfo(userName)
            val repos = gitReposByUsername(userName, user.publicRepos)
            val userWithRepos = GitUserWithRepos(user, repos)

            putUserToDb(userWithRepos)

            Status.Success(listOf(GitUserWithRepos(user, repos)))

        } catch (e: Throwable) {

            Status.Failed<GitUserWithRepos>(e)

        }

    private suspend fun gitUserInfo(userName: String) =
        service.gitUserInfo(userName)

    private suspend fun gitReposByUsername(userName: String, perPage: Int) =
        service.gitReposByUsername(userName, perPage)

    private suspend fun getAllSavedUsers() =
        dao.getAllSavedUsers()

    private suspend fun putUserToDb(user: GitUserWithRepos) =
        dao.insert(user)

    private suspend fun findUserInDb(login: String) =
        dao.findByLogin(login)

    companion object {

        @Volatile
        private var INSTANCE: GitUserRepository? = null

        fun get(db: GitDb, service: GitUserInfoService, netManager: NetManager) =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: GitUserRepository(
                            db,
                            service,
                            netManager
                        ).also {
                            INSTANCE = it
                        }
                }
    }
}