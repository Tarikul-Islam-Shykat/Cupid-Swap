package com.example.cupidswap.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import com.example.cupidswap.R

object Utils {

    var dialog: AlertDialog? = null

    fun showDialog(context : Context){
        dialog = AlertDialog.Builder(context)
            .setView(R.layout.sample_loading_dialog_layout)
            .setCancelable(false)
            .create()

        dialog!!.show()
    }

    fun dismissDialog(context : Context){
        dialog!!.dismiss()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun dismissKeyboard(activity: Activity) {
        val imm: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus) imm.hideSoftInputFromWindow(
            activity.currentFocus!!
                .applicationWindowToken, 0
        )
    }


}