package com.example.easeat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.easeat.databinding.ActivityAuthBinding
import com.example.easeat.models.util.LoadingState
import com.example.easeat.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint





@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.exceptions.observeNotNull(this) {
            Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
        }

        if(FirebaseAuth.getInstance().currentUser!=null) {
            moveToHome()
        }


        viewModel.loading.observe(this) {
            binding.pbAuth.visibility = when(it) {
                is LoadingState.Loading -> View.VISIBLE
                is LoadingState.Loaded -> View.GONE
            }
        }
    }


    fun moveToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}