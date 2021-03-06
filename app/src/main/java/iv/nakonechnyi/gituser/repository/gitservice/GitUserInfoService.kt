package iv.nakonechnyi.gituser.repository.gitservice

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import iv.nakonechnyi.gituser.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GitUserInfoService {

    private val IO = Dispatchers.IO

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val client = retrofit.create(ApiClient::class.java)

    suspend fun gitUserInfo(userName:String) = withContext(IO) {
        client.fetchGitUserInfo(userName)
    }

    suspend fun gitReposByUsername(userName: String, perPage: Int) = withContext(IO){
        client.fetchGitReposByUsername(userName, perPage)
    }

}