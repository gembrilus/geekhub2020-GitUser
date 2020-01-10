package iv.nakonechnyi.gituser

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.entities.GitUser
import iv.nakonechnyi.gituser.repository.NetManager
import iv.nakonechnyi.gituser.repository.db.GitDb
import iv.nakonechnyi.gituser.repository.gitservice.GitUserInfoService
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

abstract class InitialTestClass {

    val TEST_USER = GitUser(
        id = 1,
        login = "gembrilus",
        name = "Максим",
        company = null,
        location = null,
        bio = null,
        url = "https://github.com/users/gembrilus",
        avatarUrl = "https://github.com/users/gembrilus/avatar.html",
        created = "2007-10-20T05:24:19Z",
        updated = "2008-01-29T00:59:19Z",
        followers = 1,
        following = 3,
        publicRepos = 1,
        site = null,
        email = null
    )

    val TEST_REPOS = listOf(
        GitRepo(
            id = 1,
            name = "Test Repo",
            description = null,
            created = "2007-10-20T05:24:19Z",
            updated = "2008-01-29T00:59:19Z",
            url = "https://github.com/gembrilus/Test Repo",
            language = "kotin",
            owner = GitRepo.Owner("gembrilus"),
            size = 1
        )
    )

    val context = mock<Context>()

    val database by lazy {
        mock<GitDb>{
            on { gitUserReposDao() } doReturn mock()
        }
    }

}