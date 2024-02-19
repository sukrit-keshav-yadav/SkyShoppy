package com.hyskytech.skyshoppy.View.dialog

import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
    dialog.setCanceledOnTouchOutside(false)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edEmail = view.findViewById<EditText>(R.id.EdtEmailResetPass)
    val buttonSend = view.findViewById<Button>(R.id.BtnSendResetPass)
    val buttonCancel = view.findViewById<Button>(R.id.BtnCancelRestPass)

    buttonSend.setOnClickListener {
        val email = edEmail.text.toString().trim()
        if (email.isNullOrEmpty() or !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(requireContext(), "Please enter valid email", Toast.LENGTH_SHORT).show()
        }else{
        onSendClick(email)
        dialog.dismiss()
        }
    }

    buttonCancel.setOnClickListener{
        dialog.dismiss()
    }
}