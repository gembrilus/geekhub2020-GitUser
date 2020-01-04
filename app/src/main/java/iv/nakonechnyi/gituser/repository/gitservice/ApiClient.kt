package iv.nakonechnyi.gituser.repository.gitservice

import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.entities.GitUser
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiClient {
    @GET("users/{username}")
    suspend fun fetchGitUserInfo(@Path("username") username: String): GitUser

    @GET("users/{username}/repos")
    suspend fun fetchGitReposByUsername(@Path("username") username: String): List<GitRepo>
}