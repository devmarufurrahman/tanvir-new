package com.adaptixinnovate.tanvirahmedrobin.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.ui.MainActivity

class CongratulationsDialog(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        setContentView(R.layout.submission_msg)

        // Make dialog width match parent with margins
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set transparent background
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set animation
        window?.attributes?.windowAnimations = R.style.DialogAnimation

        // Close dialog when Continue button is clicked
        findViewById<Button>(R.id.btnContinue).setOnClickListener {
            dismiss()
            context.startActivity(Intent(context, MainActivity::class.java)) // Replace HomeActivity with your target activity
            (context as? Activity)?.finish()
        }
    }
}