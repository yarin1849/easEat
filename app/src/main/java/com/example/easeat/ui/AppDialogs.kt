package com.example.easeat.ui

import android.app.AlertDialog
import android.content.Context
import com.example.easeat.models.Rating

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

        fun showReviewDeleteDialog(context: Context, review: Rating, onDeleteReviewConfirm: (Rating) -> Unit) {
            AlertDialog.Builder(context)
                .setTitle("EasEat")
                .setMessage("Are you sure you want to delete the review?")
                .setPositiveButton("Yes, Delete") { x, y ->
                    onDeleteReviewConfirm.invoke(review)
                }
                .setNegativeButton("Cancel",null)
                .show()
        }
    }
}