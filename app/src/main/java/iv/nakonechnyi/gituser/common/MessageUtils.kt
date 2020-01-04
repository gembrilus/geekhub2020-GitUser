package iv.nakonechnyi.gituser.common

import android.app.AlertDialog
import android.content.Context
import iv.nakonechnyi.gituser.R

fun showErrorMessage(context: Context, t: Throwable) =
    AlertDialog.Builder(context)
        .setIcon(R.drawable.ic_error_outline_black_24dp)
        .setTitle(context.getString(R.string.dialog_error_title))
        .setMessage(t.cause?.message ?: t.message)
        .create()
        .run { show() }