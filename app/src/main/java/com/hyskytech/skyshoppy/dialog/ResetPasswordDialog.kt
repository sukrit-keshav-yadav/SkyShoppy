package com.hyskytech.skyshoppy.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hyskytech.skyshoppy.R

fun Fragment.setupBottomSheetDialog(
    onSendClick : (String) -> Unit
){

    val dialog = BottomSheetDialog(requireContext(),R.style.DialogBottomSheet)
    val view = layoutInflater.inflate(R.layout.dialog_reset_password, null)

    dialog.setContentView(view)
    dialog.show()
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

    val edEmail = view.findViewById<EditText>(R.id.EdtEmailResetPass)
    val buttonSend = view.findViewById<Button>(R.id.BtnSendResetPass)
    val buttonCancel = view.findViewById<Button>(R.id.BtnCancelRestPass)

    buttonSend.setOnClickListener {
        val email = edEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    buttonCancel.setOnClickListener{
        dialog.dismiss()
    }
}