package iv.nakonechnyi.gituser.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import iv.nakonechnyi.gituser.InitialAndroidTestClass
import iv.nakonechnyi.gituser.R
import iv.nakonechnyi.gituser.ui.adapters.GitReposAdapter
import iv.nakonechnyi.gituser.ui.adapters.GitUsersAdapter
import iv.nakonechnyi.gituser.utils.wait
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoadUsersFromNetTest: InitialAndroidTestClass() {

    private lateinit var loginGembrilus: String

    @Before
    override fun setUp() {
        super.setUp()

        loginGembrilus = "gembrilus"

    }

    @Test
    fun enter_a_loginGembrilus_and_fetch_info_from_internet() {

        val count = recyclerUserCount()

        if (count > 0){

            for (i in 0 until count){
                onView(withId(R.id.recycler_user))
                    .perform(actionOnItemAtPosition<GitUsersAdapter.GitUserHolder>(0, swipeRight()))
            }

        }

        onView(withHint(R.string.enter_a_user_login))
            .check(matches(isDisplayed()))
            .perform(clearText())
            .perform(typeText(loginGembrilus))
            .perform(pressImeActionButton(), closeSoftKeyboard())
            .perform(clearText())

        onView(isRoot()).perform(wait(2000))

        assertThat(recyclerUserCount(), equalTo(1))

        onView(withId(R.id.recycler_user))
            .perform(actionOnItemAtPosition<GitUsersAdapter.GitUserHolder>(0, click()))

        onView(withText(loginGembrilus)).check(matches(isDisplayed()))

        onView(withId(R.id.avatar))
            .check(matches(isDisplayed()))
            .perform(click())

        device.pressBack()

        onView(withId(R.id.btn_repos))
            .check(matches(isDisplayed()))
            .perform(click())

        if (recyclerReposCount() > 0) {
            onView(withId(R.id.recycler_repos))
                .perform(actionOnItemAtPosition<GitReposAdapter.GitReposViewHolder>(0, click()))
        }

        repeat(2){ device.pressBack() }

        if (recyclerUserCount() > 0) {
            onView(withId(R.id.recycler_user))
                .perform(actionOnItemAtPosition<GitUsersAdapter.GitUserHolder>(0, swipeRight()))
        }
    }

}