package iv.nakonechnyi.gituser.common

import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class UtilsKtTest {

    private val STRING_FOR_TEST = listOf(
        "2007-10-20T05:24:19Z",
        "2000-01-01T00:00:00Z",
        "2000-01-01T00:00:19Z")
    private val RESULT_RU = listOf(
        "20-окт-2007 05:24",
        "01-янв-2000 00:00",
        "01-янв-2000 00:00")
    private val RESULT_EN = listOf(
        "20-Oct-2007 05:24",
        "01-Jan-2000 00:00",
        "01-Jan-2000 00:00")


    @Test
    fun check_toHumanReadableDateString_return_correct_date() {
        STRING_FOR_TEST.forEachIndexed { index, s ->
            val result = toHumanReadableDateString(s)
            assertThat(result, anyOf(equalTo(RESULT_RU[index]), equalTo(RESULT_EN[index])))
        }
    }
}