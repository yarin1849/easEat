package com.example.easeat.database.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class BusinessRemoteDatabase {

    var fireStore = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()


}