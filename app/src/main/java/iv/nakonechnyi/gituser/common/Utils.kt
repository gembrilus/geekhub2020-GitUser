package iv.nakonechnyi.gituser.common

import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

fun toHumanReadableDateString(date: String): String {
    val fromPattern = "yyyy-MM-dd'T'hh:mm:ss'Z'"
    val toPattern = "dd-MMM-yyyy HH:mm"

    val sourceDate = SimpleDateFormat(fromPattern, Locale.ENGLISH).parse(date)
        ?: throw IllegalArgumentException("Date is nullable")

    return SimpleDateFormat(toPattern, Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }.format(sourceDate)

}