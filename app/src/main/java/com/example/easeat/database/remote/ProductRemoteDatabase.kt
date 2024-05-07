package com.example.easeat.database.remote

import android.content.SharedPreferences
import com.example.easeat.LAST_UPDATE_KEY
import com.example.easeat.models.Business
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class ProductRemoteDatabase {

    var fireStore = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()



}