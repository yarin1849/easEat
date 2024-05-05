package com.example.easeat.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.easeat.AuthActivity
import com.example.easeat.databinding.FragmentLoginBinding
import com.example.easeat.models.dto.RegisterDto
import com.example.easeat.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener(::onLoginSubmit)

        binding.noAccount.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    private fun onLoginSubmit(v: View) {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        binding.etEmailLayout.error = null
        binding.etPassword.error = null

        if(TextUtils.isEmpty(email)) {
            binding.etEmailLayout.error = "Email must not be empty"
            return
        }
        if(TextUtils.isEmpty(password)) {
            binding.etPassword.error = "Password must not be empty"
            return
        }


        binding.btnLogin.isEnabled = false
        viewModel.login(
            email = email,
            password = password,
            successCallback = {
                binding.btnLogin.isEnabled = true
                val activity = requireActivity() as? AuthActivity
                activity?.moveToHome()
            },
            failCallback = {
                binding.btnLogin.isEnabled = true

            }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}