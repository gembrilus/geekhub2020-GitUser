package iv.nakonechnyi.gituser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import iv.nakonechnyi.gituser.InitialTestClass
import iv.nakonechnyi.gituser.repository.GitUserRepository
import iv.nakonechnyi.gituser.repository.Status
import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos
import iv.nakonechnyi.gituser.utils.GitTestCoroutineDispatcher
import iv.nakonechnyi.gituser.utils.fetch
import iv.nakonechnyi.gituser.utils.getOrAwaitValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

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
    fun check_property_success() {

        val expectedValue = listOf(TEST_USER_OBJECT)

        doReturn(Status.Success(expectedValue)).whenever(repository).fetch(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.success.getOrAwaitValue(), equalTo(expectedValue))
        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(false))
    }

    @Test
    fun check_property_failed() {

        val expectedValue = Throwable()

        doReturn(Status.Failed<GitUserWithRepos>(expectedValue)).whenever(repository).fetch(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.failed.getOrAwaitValue(), equalTo(expectedValue))
        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(false))

    }

    @Test
    fun check_property_NoNetwork() {

        val expectedValue = true
        val returnValue = Status.NetworkFailed(listOf(TEST_USER_OBJECT))

        doReturn(returnValue).whenever(repository).fetch(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(expectedValue))

    }

    @Test
    fun check_property_NoNetwork_when_network_is_resumed() {

        val expectedValue = false
        val returnValueOnSuccess = Status.Success(listOf(TEST_USER_OBJECT))

        doReturn(returnValueOnSuccess).whenever(repository).fetch(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(expectedValue))
    }

    @Test
    fun check_property_NoNetwork_when_fails_on_network_request() {

        val expectedValue = false
        val returnValueOnFailure = Status.Failed<GitUserWithRepos>(Throwable())

        doReturn(returnValueOnFailure).whenever(repository).fetch(login)

        gitViewModel.refresh(login)

        assertThat(gitViewModel.noNetwork.getOrAwaitValue(), equalTo(expectedValue))
    }

    @Test
    fun check_propertis_success_and_nonetwork_on_status_DbSuccess() {

        val expectedOnNoNetwork = true
        val expectedOnSuccess = listOf(TEST_USER_OBJECT)
        val returnValue = Status.DbSuccess(expectedOnSuccess)

        doReturn(returnValue).whenever(repository).fetch(login)

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