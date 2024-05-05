package com.example.easeat.ui

import android.app.AlertDialog
import android.content.Context

class AppDialogs {
    companion object {
        fun showLogoutDialog(context: Context, onLogout: () -> Unit) {

            AlertDialog.Builder(context)
                .setTitle("EasEat")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes, Logout") { x, y ->
                    onLogout.invoke()
                }
                .setNegativeButton("Cancel",null)
                .show()
        }
    }
}