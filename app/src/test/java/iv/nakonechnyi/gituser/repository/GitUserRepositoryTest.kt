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
import kotlinx.coroutines.runBlocking
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
    private val user = GitUserWithRepos(TEST_USER, TEST_REPOS)

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
    fun check_getFullUserInfo_when_network_enabled_return_success() = runBlocking<Unit> {

        doReturn(true).whenever(netManager).isNetworkAvailable
        doReturn(TEST_USER).whenever(gitUserInfoService).gitUserInfo(login)
        doReturn(TEST_REPOS).whenever(gitUserInfoService).gitReposByUsername(login, 1)
        doReturn(null).whenever(dao).findByLogin(login)
        doReturn(1).whenever(dao).insert(user)

        assertEquals(Status.Success(listOf(GitUserWithRepos(TEST_USER, TEST_REPOS))), repository.getFullUserInfo(login))

        verify(gitUserInfoService, atLeastOnce()).gitUserInfo(login)
        verify(gitUserInfoService, atLeastOnce()).gitReposByUsername(login, 1)
        verify(netManager, atLeastOnce()).isNetworkAvailable
        verify(dao, atLeastOnce()).findByLogin(login)
        verify(dao, atLeastOnce()).insert(user)
        verifyNoMoreInteractions(gitUserInfoService)
        verifyNoMoreInteractions(netManager)
        verifyNoMoreInteractions(dao)

    }

    @Test
    fun check_getFullUserInfo_when_network_disabled_return_NetworkFailed() = runBlocking<Unit> {

        doReturn(false).whenever(netManager).isNetworkAvailable
        doReturn(null).whenever(dao).findByLogin(login)
        doReturn(listOf(user)).whenever(dao).getAllSavedUsers()

        assertEquals(Status.NetworkFailed(listOf(user)), repository.getFullUserInfo(login))

        verify(gitUserInfoService, never()).gitUserInfo(login)
        verify(gitUserInfoService, never()).gitReposByUsername(login, 1)
        verify(netManager, atLeastOnce()).isNetworkAvailable
        verify(dao, atLeastOnce()).findByLogin(login)
        verify(dao, atLeastOnce()).getAllSavedUsers()
        verifyNoMoreInteractions(gitUserInfoService)
        verifyNoMoreInteractions(netManager)
        verifyNoMoreInteractions(dao)

    }

    @Test
    fun check_getFullUserInfo_when_network_enabled_return_failed() = runBlocking<Unit> {

        val error = RuntimeException("error")
        doReturn(true).whenever(netManager).isNetworkAvailable
        doThrow(error).whenever(gitUserInfoService).gitUserInfo(login)
        doReturn(null).whenever(dao).findByLogin(login)

        assertEquals(Status.Failed<GitUserWithRepos>(error), repository.getFullUserInfo(login))

        verify(gitUserInfoService, atLeastOnce()).gitUserInfo(login)
        verify(netManager, atLeastOnce()).isNetworkAvailable
        verify(dao, atLeastOnce()).findByLogin(login)
        verifyNoMoreInteractions(gitUserInfoService)
        verifyNoMoreInteractions(netManager)
        verifyNoMoreInteractions(dao)

    }

    @Test
    fun check_getFullUserInfo_when_network_enabled_and_user_exist_in_db_return_DbSuccess_with_one_user_in_list() = runBlocking<Unit> {

        doReturn(true).whenever(netManager).isNetworkAvailable
        doReturn(TEST_USER).whenever(gitUserInfoService).gitUserInfo(login)
        doReturn(TEST_REPOS).whenever(gitUserInfoService).gitReposByUsername(login, 1)
        doReturn(user).whenever(dao).findByLogin(login)
        doReturn(1).whenever(dao).insert(user)

        assertEquals(Status.DbSuccess(listOf(user)), repository.getFullUserInfo(login))

        verify(gitUserInfoService, never()).gitUserInfo(login)
        verify(gitUserInfoService, never()).gitReposByUsername(login, 1)
        verify(dao, atLeastOnce()).findByLogin(login)
        verify(netManager, never()).isNetworkAvailable
        verifyNoMoreInteractions(gitUserInfoService)
        verifyNoMoreInteractions(netManager)
        verifyNoMoreInteractions(dao)

    }

    @After
    fun setDown() {
        Dispatchers.resetMain()
    }
}