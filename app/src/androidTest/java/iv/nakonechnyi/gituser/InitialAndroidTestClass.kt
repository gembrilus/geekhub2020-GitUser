package iv.nakonechnyi.gituser

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import iv.nakonechnyi.gituser.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_repos.*
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class InitialAndroidTestClass {

    protected lateinit var device: UiDevice

    @get:Rule
    val activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    open fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    protected fun recyclerUserCount(): Int {
        val recyclerView =
            activityRule.activity.recycler_user
        return recyclerView.adapter?.itemCount!!
    }

    protected fun recyclerReposCount(): Int {
        val recyclerView =
            activityRule.activity.recycler_repos
        return recyclerView.adapter?.itemCount!!
    }

    @After
    open fun after(){

    }

}