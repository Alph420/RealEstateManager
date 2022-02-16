package com.openclassrooms.realestatemanager.view.dialog

import android.R
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.databinding.DialogNoGpsBinding

/**
 * Created by Julien Jennequin on 25/12/2021 11:47
 * Project : RealEstateManager
 **/
class NoGpsDialog(context: Context) : androidx.appcompat.app.AlertDialog(context) {

    fun showForegroundGpsDialog(): androidx.appcompat.app.AlertDialog {
        val rootView: DialogNoGpsBinding = DialogNoGpsBinding.inflate(layoutInflater)
        val dialog: androidx.appcompat.app.AlertDialog = MaterialAlertDialogBuilder(context)
            .setView(rootView.root.rootView)
            .show()

        rootView.btnSettings.setOnClickListener { v ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        return dialog
    }
}