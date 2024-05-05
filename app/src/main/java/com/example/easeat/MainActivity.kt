package com.example.easeat

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.easeat.databinding.ActivityMainBinding
import com.example.easeat.models.util.LoadingState
import com.example.easeat.ui.AppDialogs
import com.example.easeat.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.exceptions.observeNotNull(this) {
            Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
        }

        viewModel.currentUser.observeNotNull(this) { user->
            Picasso.get().load(user.image).into(binding.toolbar.ivProfileImage)
            binding.toolbar.tvUserName.text = "Logged in as ${user.name}"
        }
        binding.toolbar.btnLogout.setOnClickListener {
            AppDialogs.showLogoutDialog(this) {
                viewModel.signOut()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }

        viewModel.loading.observe(this) {
            binding.pbMain.visibility = when(it) {
                is LoadingState.Loading -> View.VISIBLE
                is LoadingState.Loaded -> View.GONE
            }
        }
    }

}