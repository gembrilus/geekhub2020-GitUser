package iv.nakonechnyi.gituser.di

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import iv.nakonechnyi.gituser.InitialTestClass
import iv.nakonechnyi.gituser.repository.GitUserRepository
import iv.nakonechnyi.gituser.ui.GitViewModel
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.isA
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK], manifest = Config.NONE)
class GitViewModelFactoryTest: InitialTestClass() {

    private lateinit var repository: GitUserRepository
    private lateinit var factory: GitViewModelFactory

    @Before
    fun setUp() {
        repository = mock()
        factory =spy(
            GitViewModelFactory.factory(
                ApplicationProvider.getApplicationContext()
            )
        )
    }

    @Test
    fun check_that_view_model_is_created_correct() {
        val model = spy(factory.create(GitViewModel::class.java))

        doNothing().whenever(model).setPosition(any())
        doNothing().whenever(model).removeItem(TEST_USER.login)
        doNothing().whenever(model).refresh(TEST_USER.login)

        model.setPosition(any())
        model.removeItem(TEST_USER.login)
        model.refresh(TEST_USER.login)

        assertThat(model,isA(GitViewModel::class.java))
        assertThat(model, hasProperty("success"))
        assertThat(model, hasProperty("failed"))
        assertThat(model, hasProperty("noNetwork"))
        assertThat(model, hasProperty("position"))

        verify(model).setPosition(any())
        verify(model).removeItem(TEST_USER.login)
        verify(model).refresh(TEST_USER.login)

        verifyNoMoreInteractions(model)
    }
}