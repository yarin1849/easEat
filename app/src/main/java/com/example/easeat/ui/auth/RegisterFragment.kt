package com.example.easeat.ui.auth

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.easeat.AuthActivity
import com.example.easeat.databinding.FragmentRegisterBinding
import com.example.easeat.models.dto.RegisterDto
import com.example.easeat.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.regex.Pattern

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()

    private var selectedImageUri : Uri? = null

    private val galleryIntentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
        imageUri?.let {
            binding.ivProfile.setImageURI(it)
            selectedImageUri = it
        }
    }

    private val permissionsIntentLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if(granted) {
            galleryIntentLauncher.launch("image/*")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener(::onRegisterSubmit)
        binding.ivProfile.setOnClickListener(::openGallery)

        binding.existingAccount.setOnClickListener {
            findNavController().popBackStack()
        }

    }


    private fun openGallery(v: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2){
            galleryIntentLauncher.launch("image/*")
        }
        else if(requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
            permissionsIntentLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        else {
            galleryIntentLauncher.launch("image/*")
        }
    }

    private fun onRegisterSubmit(v: View) {
        val address = binding.etAddress.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val birthDay = binding.etBirthDay.text.toString()
        val name = binding.etName.text.toString()


        binding.etBirthdayLayout.error = null
        binding.etNameLayout.error = null
        binding.etAddressLayout.error = null
        binding.etEmailLayout.error = null
        binding.etPassword.error = null


        if(TextUtils.isEmpty(birthDay)) {
            binding.etBirthdayLayout.error = "Birthday must not be empty"
            return
        }
        val pattern = Pattern.compile("^\\d\\d-\\d\\d-\\d\\d\\d\\d")

        if(!pattern.matcher(birthDay).matches()) {
            binding.etBirthdayLayout.error = "Birthday must be in format dd-mm-yyyy"
            return
        }

        if(TextUtils.isEmpty(name)) {
            binding.etNameLayout.error = "Name must not be empty"
            return
        }
        if(TextUtils.isEmpty(address)) {
            binding.etAddressLayout.error = "Address must not be empty"
            return
        }
        if(TextUtils.isEmpty(email)) {
            binding.etEmailLayout.error = "Email must not be empty"
            return
        }
        if(TextUtils.isEmpty(password)) {
            binding.etPassword.error = "Password must not be empty"
            return
        }

        val c = Calendar.getInstance()

        val (d, m, y) = birthDay.split("-")
        c.set(Calendar.DAY_OF_MONTH, d.toInt())
        c.set(Calendar.MONTH, m.toInt())
        c.set(Calendar.YEAR, y.toInt())

        val registerDto = RegisterDto(
            name = name,
            email = email,
            password = password,
            address = address,
            birthday = c.timeInMillis,
            image =  selectedImageUri
        )

        binding.btnRegister.isEnabled = false
        viewModel.register(
            registerDto = registerDto,
            successCallback = {
                binding.btnRegister.isEnabled = true
                findNavController().popBackStack()
                Snackbar.make(binding.root, "Account successfully created, you may login.", Snackbar.LENGTH_LONG).show()
            },
            failCallback = {
                binding.btnRegister.isEnabled = true
            }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}