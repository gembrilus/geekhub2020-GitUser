package iv.nakonechnyi.gituser.repository.gitservice

import com.nhaarman.mockitokotlin2.*
import iv.nakonechnyi.gituser.InitialTestClass
import iv.nakonechnyi.gituser.utils.fetchTestRepos
import iv.nakonechnyi.gituser.utils.fetchTestUser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

//Бессмысленный тест - по сути тестируем саму Макиту!!

@RunWith(MockitoJUnitRunner::class)
class GitUserInfoServiceTest: InitialTestClass() {


    private lateinit var gitUserInfoService: GitUserInfoService

    @Before
    fun setUp() {
        gitUserInfoService = mock()
    }

    @Test
    fun check_that_the_method_gitUserInfo_works() {

        doReturn(TEST_USER).whenever(gitUserInfoService).fetchTestUser("gembrilus")

        val actual = gitUserInfoService.fetchTestUser("gembrilus")

        assertEquals(TEST_USER, actual)

        verify(gitUserInfoService, atLeastOnce()).fetchTestUser("gembrilus")

    }

    @Test
    fun check_that_the_method_gitReposByUsername_works() {

        doReturn(TEST_REPOS).whenever(gitUserInfoService).fetchTestRepos("gembrilus", 1)

        val actual = gitUserInfoService.fetchTestRepos("gembrilus", 1)

        assertEquals(TEST_REPOS, actual)

        verify(gitUserInfoService, atLeastOnce()).fetchTestRepos("gembrilus", 1)

    }
}