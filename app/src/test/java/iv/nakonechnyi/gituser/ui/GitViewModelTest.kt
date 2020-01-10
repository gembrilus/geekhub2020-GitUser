package iv.nakonechnyi.gituser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import iv.nakonechnyi.gituser.InitialTestClass
import iv.nakonechnyi.gituser.repository.GitUserRepository
import iv.nakonechnyi.gituser.repository.Status
import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos
import iv.nakonechnyi.gituser.utils.GitTestCoroutineDispatcher
import iv.nakonechnyi.gituser.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GitViewModelTest : InitialTestClass() {

    private lateinit var gitViewModel: GitViewModel
    private lateinit var repository: GitUserRepository
    private lateinit var TEST_USER_OBJECT: GitUserWithRepos

    private val login = "login"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {

        Dispatchers.setMain(GitTestCoroutineDispatcher)
        repository = mock()
        gitViewModel = GitViewModel(repository)
        TEST_USER_OBJECT = GitUserWithRepos(TEST_USER, TEST_REPOS)
    }

    @Test
    fun check_property_position() {

        for (i in 0..10) {
            gitViewModel.setPosition(i)
            assertThat(gitViewModel.position.getOrAwaitValue(), equalTo(i))
        }
    }

    @Test
    fun check_property_success() = runBlocking {

        val expectedValue = listOf(TEST_USER_OBJECT)

        doReturn(Status.Success(expectedValue)).whenever(repository).getFullUserInfo(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.success.getOrAwaitValue(), equalTo(expectedValue))
        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(false))
    }

    @Test
    fun check_property_failed() = runBlocking {

        val expectedValue = Throwable()

        doReturn(Status.Failed<GitUserWithRepos>(expectedValue)).whenever(repository).getFullUserInfo(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.failed.getOrAwaitValue(), equalTo(expectedValue))
        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(false))

    }

    @Test
    fun check_property_NoNetwork() = runBlocking {

        val expectedValue = true
        val returnValue = Status.NetworkFailed(listOf(TEST_USER_OBJECT))

        doReturn(returnValue).whenever(repository).getFullUserInfo(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(expectedValue))

    }

    @Test
    fun check_property_NoNetwork_when_network_is_resumed() = runBlocking {

        val expectedValue = false
        val returnValueOnSuccess = Status.Success(listOf(TEST_USER_OBJECT))

        doReturn(returnValueOnSuccess).whenever(repository).getFullUserInfo(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(expectedValue))
    }

    @Test
    fun check_property_NoNetwork_when_fails_on_network_request() = runBlocking {

        val expectedValue = false
        val returnValueOnFailure = Status.Failed<GitUserWithRepos>(Throwable())

        doReturn(returnValueOnFailure).whenever(repository).getFullUserInfo(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(expectedValue))
    }

    @Test
    fun check_propertis_success_and_nonetwork_on_status_DbSuccess() = runBlocking {

        val expectedOnNoNetwork = true
        val expectedOnSuccess = listOf(TEST_USER_OBJECT)
        val returnValue = Status.DbSuccess(expectedOnSuccess)

        doReturn(returnValue).whenever(repository).getFullUserInfo(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.success.getOrAwaitValue(), equalTo(expectedOnSuccess))
        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(expectedOnNoNetwork))
    }

    @Test
    fun refresh() {

        val mock = spy(gitViewModel)

        repeat(5) {
            mock.refresh(login)
        }

        verify(mock, times(5)).refresh(login)

    }

    @After
    fun resetViewModel() {
        Dispatchers.resetMain()
    }

}