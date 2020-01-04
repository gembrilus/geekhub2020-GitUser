package iv.nakonechnyi.gituser.common

import android.graphics.Paint
import android.widget.TextView

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}