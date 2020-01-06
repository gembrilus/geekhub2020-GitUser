package iv.nakonechnyi.gituser.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import iv.nakonechnyi.gituser.InitialAndroidTestClass
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class CheckDoesNotFailOnRotateTest: InitialAndroidTestClass() {

    @Test
    fun rotate_check() {

        with(device) {

            repeat(4) { setOrientationLeft() }
            repeat(4) { setOrientationRight() }
            setOrientationNatural()

        }
    }
}