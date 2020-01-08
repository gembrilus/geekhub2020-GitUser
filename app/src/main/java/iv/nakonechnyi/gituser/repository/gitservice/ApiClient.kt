package iv.nakonechnyi.gituser.repository.gitservice

import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.entities.GitUser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {
    @GET("users/{username}")
    suspend fun fetchGitUserInfo(@Path("username") username: String): GitUser

    @GET("users/{username}/repos")
    suspend fun fetchGitReposByUsername(@Path("username") username: String, @Query(value = "per_page") perPage: Int): List<GitRepo>
}