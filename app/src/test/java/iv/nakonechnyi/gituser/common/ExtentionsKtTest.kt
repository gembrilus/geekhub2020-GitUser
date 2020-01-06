package iv.nakonechnyi.gituser.common

import android.content.Context
import android.graphics.Paint
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Config.OLDEST_SDK], manifest=Config.NONE)
class ExtentionsKtTest {

    private lateinit var context: Context

    private lateinit var textView: TextView

    private val TEXT_EXAMPLE = "Any content"

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        textView = TextView(context)
    }

    @Test
    fun underline() {

        textView.text = TEXT_EXAMPLE
        textView.underline()

        assertEquals(textView.paintFlags, Paint.UNDERLINE_TEXT_FLAG)

        assertEquals(textView.text,  TEXT_EXAMPLE)

    }
}