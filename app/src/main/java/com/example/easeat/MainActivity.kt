package com.example.easeat

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
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

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val bottomNavigationView = binding.bottomNavigationViewMain
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.homeFragment) {
                binding.toolbar.btnLogout.setImageResource(R.drawable.baseline_logout_24)
                binding.toolbar.btnLogout.setOnClickListener(::logoutRequest)
            }
            else {
                binding.toolbar.btnLogout.setImageResource(R.drawable.baseline_arrow_forward_24)
                binding.toolbar.btnLogout.setOnClickListener(::back)
            }
        }


        //setupActionBarWithNavController(navController,appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        viewModel.exceptions.observeNotNull(this) {
            Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
        }

        viewModel.currentUser.observeNotNull(this) { user->
            Picasso.get().load(user.image).into(binding.toolbar.ivProfileImage)
            binding.toolbar.tvUserName.text = "Logged in as ${user.name}"
        }
        viewModel.loading.observe(this) {
            binding.pbMain.visibility = when(it) {
                is LoadingState.Loading -> View.VISIBLE
                is LoadingState.Loaded -> View.GONE
            }
        }
    }


    fun back(v: View) {
        navController.popBackStack()
    }

    fun logoutRequest(v: View) {
        AppDialogs.showLogoutDialog(this) {
            viewModel.signOut()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

}