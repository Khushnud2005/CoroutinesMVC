package uz.vianet.coroutinesmvc.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

object Utils {

    fun Activity.toast(context: Context,msg:String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    fun customDialog(context: Context?, title: String, message: String, dialogListener: DialogListener) {
        context?.let { context ->
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setCancelable(true)
            builder.setPositiveButton("OK") { dialogInterface: DialogInterface?, i: Int ->
                dialogListener.onPositiveClick()
                dialogInterface?.dismiss()
            }
            builder.setNegativeButton("NO")
            { dialogInterface: DialogInterface?, i: Int ->
                dialogListener.onNegativeClick()
                dialogInterface?.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    interface DialogListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }
}

