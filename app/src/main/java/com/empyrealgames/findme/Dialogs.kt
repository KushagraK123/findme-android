package com.empyrealgames.findme


import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.empyrealgames.findme.databinding.DialogUserNotFoundBinding

fun showDialogUserNotFound(
    context: Context,
    phone: String,
    name:String,
    onClickPositive: () -> Unit
): AlertDialog {
    val view = LayoutInflater.from(context).inflate(R.layout.dialog_user_not_found, null, false)

    val dialog = AlertDialog.Builder(context)
        .setView(view)
        .create()

    dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

    DialogUserNotFoundBinding.bind(view)
        .apply {
            tvTitle.text = "$phone/$name Was not found, would you like to send invitation to  him/her?"
            bNotNow.setOnClickListener {
                dialog.dismiss()
            }
            bSend.setOnClickListener {
                dialog.dismiss()
                onClickPositive()
            }
        }

    return dialog
}