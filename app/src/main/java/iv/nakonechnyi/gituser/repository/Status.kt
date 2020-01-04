package iv.nakonechnyi.gituser.repository

import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos

sealed class Status<T> {
    data class Success<T>(val result: List<T>) : Status<T>()
    data class Failed<T>(val error: Throwable) : Status<T>()
    data class NetworkFailed<T>(val result: List<T>) : Status<T>()
}