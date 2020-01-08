package iv.nakonechnyi.gituser.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import iv.nakonechnyi.gituser.*
import iv.nakonechnyi.gituser.repository.db.GitUserReposDao
import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos
import iv.nakonechnyi.gituser.repository.gitservice.GitUserInfoService
import iv.nakonechnyi.gituser.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GitUserRepositoryTest: InitialTestClass() {

    private val login = "gembrilus"

    private lateinit var repository: GitUserRepository

    private lateinit var dao: GitUserReposDao

    private lateinit var netManager: NetManager

    private lateinit var gitUserInfoService: GitUserInfoService

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        Dispatchers.setMain(GitTestCoroutineDispatcher)

        netManager = spy(NetManager(context))
        dao = database.gitUserReposDao()
        gitUserInfoService = mock()
        repository = GitUserRepository.get(database, gitUserInfoService, netManager)
    }

    @Test
    fun check_getFullUserInfo_when_network_enabled_return_success() {

        doReturn(true).whenever(netManager).isNetworkAvailable
        doReturn(TEST_USER).whenever(gitUserInfoService).fetchTestUser(login)
        doReturn(TEST_REPOS).whenever(gitUserInfoService).fetchTestRepos(login, 1)
        doReturn(null).whenever(dao).findTestUserInDb(login)

        val user = gitUserInfoService.fetchTestUser(login)
        val repos = gitUserInfoService.fetchTestRepos(login, 1)
        val expectedValue = Status.Success(listOf(GitUserWithRepos(user, repos)))

        val actualValue = repository.fetch(login)

        print(actualValue)

        assertEquals(expectedValue, actualValue)

        verify(gitUserInfoService, atLeastOnce()).fetchTestUser(login)
        verify(gitUserInfoService, atLeastOnce()).fetchTestRepos(login, 1)
        verify(netManager, atLeastOnce()).isNetworkAvailable
        verify(dao, atLeastOnce()).findTestUserInDb(login)

        verifyNoMoreInteractions(gitUserInfoService)
        verifyNoMoreInteractions(netManager)

    }

    @Test
    fun check_getFullUserInfo_when_network_disabled_return_NetworkFailed() {

        doReturn(false).whenever(netManager).isNetworkAvailable
        doReturn(TEST_USER).whenever(gitUserInfoService).fetchTestUser(login)
        doReturn(TEST_REPOS).whenever(gitUserInfoService).fetchTestRepos(login, 1)
        doReturn(null).whenever(dao).findTestUserInDb(login)

        val user = gitUserInfoService.fetchTestUser(login)
        val repos = gitUserInfoService.fetchTestRepos(login, 1)
        val list = listOf(GitUserWithRepos(user, repos))

        doReturn(list).whenever(dao).fetchAllTestUsers()


        val expectedValue = Status.NetworkFailed(dao.fetchAllTestUsers())
        val actualValue = repository.fetch(login)

        print(actualValue)

        assertEquals(expectedValue, actualValue)

        verify(gitUserInfoService, atLeastOnce()).fetchTestUser(login)
        verify(gitUserInfoService, atLeastOnce()).fetchTestRepos(login, 1)
        verify(netManager, atLeastOnce()).isNetworkAvailable
        verify(database, atLeastOnce()).gitUserReposDao()
        verify(dao, atLeastOnce()).findTestUserInDb(login)
        verify(dao, atLeastOnce()).fetchAllTestUsers()

        verifyNoMoreInteractions(gitUserInfoService)
        verifyNoMoreInteractions(netManager)

    }

    @Test
    fun check_getFullUserInfo_when_network_enabled_return_failed() {

        val error = RuntimeException("error")

        doReturn(true).whenever(netManager).isNetworkAvailable
        doThrow(error).whenever(gitUserInfoService).fetchTestUser(login)
        doThrow(error).whenever(gitUserInfoService).fetchTestRepos(login, 1)
        doReturn(null).whenever(dao).findTestUserInDb(login)

        val expectedValue1 = try {
            val user = gitUserInfoService.fetchTestUser(login)
            val repos = TEST_REPOS
            Status.Success(listOf(GitUserWithRepos(user, repos)))
        } catch (t: Throwable) {
            Status.Failed<GitUserWithRepos>(t)
        }

        val expectedValue2 = try {
            val user = TEST_USER
            val repos = gitUserInfoService.fetchTestRepos(login, 1)
            Status.Success(listOf(GitUserWithRepos(user, repos)))
        } catch (t: Throwable) {
            Status.Failed<GitUserWithRepos>(t)
        }

        val actualValue = repository.fetch(login)

        print(actualValue)

        assertEquals(expectedValue1, actualValue)
        assertEquals(expectedValue2, actualValue)

        verify(gitUserInfoService, atLeastOnce()).fetchTestUser(login)
        verify(gitUserInfoService, atLeastOnce()).fetchTestRepos(login, 1)
        verify(netManager, atLeastOnce()).isNetworkAvailable
        verify(dao, atLeastOnce()).findTestUserInDb(login)

        verifyNoMoreInteractions(gitUserInfoService)
        verifyNoMoreInteractions(netManager)

    }

    @Test
    fun check_getFullUserInfo_when_network_enabled_and_user_exist_in_db_return_success_with_one_user_in_list() {

        doReturn(false).whenever(netManager).isNetworkAvailable
        doReturn(TEST_USER).whenever(gitUserInfoService).fetchTestUser(login)
        doReturn(TEST_REPOS).whenever(gitUserInfoService).fetchTestRepos(login, 1)

        val user = gitUserInfoService.fetchTestUser(login)
        val repos = gitUserInfoService.fetchTestRepos(login, 1)
        val userWithRepos = GitUserWithRepos(user, repos)

        doReturn(userWithRepos).whenever(dao).findTestUserInDb(login)

        val expectedValue = Status.Success(listOf(dao.findTestUserInDb(login)))

        val actualValue = repository.fetch(login)

        print(actualValue)

        assertEquals(expectedValue, actualValue)

        verify(gitUserInfoService, atLeastOnce()).fetchTestUser(login)
        verify(gitUserInfoService, atLeastOnce()).fetchTestRepos(login, 1)
        verify(dao, atLeastOnce()).findTestUserInDb(login)
        verify(netManager, never()).isNetworkAvailable

        verifyNoMoreInteractions(gitUserInfoService)
        verifyNoMoreInteractions(netManager)

    }

    @After
    fun setDown() {
        Dispatchers.resetMain()
    }
}