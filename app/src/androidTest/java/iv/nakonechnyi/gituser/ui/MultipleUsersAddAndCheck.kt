package iv.nakonechnyi.gituser.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import iv.nakonechnyi.gituser.InitialAndroidTestClass
import iv.nakonechnyi.gituser.R
import iv.nakonechnyi.gituser.ui.adapters.GitUsersAdapter
import iv.nakonechnyi.gituser.utils.wait
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class MultipleUsersAddAndCheck: InitialAndroidTestClass() {

    private lateinit var users: List<String>

    @Before
    override fun setUp() {
        super.setUp()
        users = listOf("gembrilus", "seezov", "defunkt", "mojombo")
    }

    @Test
    fun add_multiple_users_and_check_their_count(){

        val count = recyclerUserCount()

        if (count > 0){
            for (i in 0 until count){
                onView(ViewMatchers.withId(R.id.recycler_user))
                    .perform(
                        actionOnItemAtPosition<GitUsersAdapter.GitUserHolder>(0, ViewActions.swipeRight())
                    )
            }
        }

        repeat(4) {
            onView(ViewMatchers.withHint(R.string.enter_a_user_login))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText(users[it]))
                .perform(ViewActions.pressImeActionButton(), ViewActions.closeSoftKeyboard())
                .perform(ViewActions.clearText())

            onView(ViewMatchers.isRoot()).perform(wait(2000))
        }

        onView(ViewMatchers.withHint(R.string.enter_a_user_login))
            .perform(ViewActions.clearText())
            .perform(ViewActions.pressImeActionButton(), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.isRoot()).perform(wait(2000))

        assertTrue(recyclerUserCount() == users.size)

    }


}