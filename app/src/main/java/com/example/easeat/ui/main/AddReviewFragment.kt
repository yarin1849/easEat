package com.example.easeat.ui.main

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.easeat.AuthActivity
import com.example.easeat.MainActivity
import com.example.easeat.databinding.FragmentRateBinding
import com.example.easeat.databinding.FragmentRegisterBinding
import com.example.easeat.json
import com.example.easeat.models.Business
import com.example.easeat.models.Rating
import com.example.easeat.models.dto.RegisterDto
import com.example.easeat.models.util.LoadingState
import com.example.easeat.viewmodels.BusinessViewModel
import com.example.easeat.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import java.util.Calendar
import java.util.regex.Pattern

@AndroidEntryPoint
class AddReviewFragment: Fragment() {

    private var _binding: FragmentRateBinding? = null
    private val binding: FragmentRateBinding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    private val businessViewModel by activityViewModels<BusinessViewModel>()

    private var selectedImageUri : Uri? = null

    private val args by navArgs<AddReviewFragmentArgs>()

    private val galleryIntentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
        imageUri?.let {
            binding.ivReview.setImageURI(it)
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
        _binding = FragmentRateBinding.inflate(inflater,container,false)
        return binding.root
    }

    private var existingRating: Rating? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.ivReview.setOnClickListener(::openGallery)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }


        val business = json.decodeFromString<Business>(args.business)
        binding.btnPost.setOnClickListener {
            onReviewSubmit(business.id, args.existingRating != null)
        }

        // Existing rating handling
        val existingRatingJson = args.existingRating ?: return
        existingRating = json.decodeFromString<Rating>(existingRatingJson) ?: return
        Picasso.get().load(existingRating!!.image).into(binding.ivReview)
        binding.etReview.setText(existingRating!!.content)
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

    private fun onReviewSubmit(bid:String, editExisting: Boolean) {
        val reviewContent = binding.etReview.text.toString()

        binding.etReviewLayout.error = null


        if(TextUtils.isEmpty(reviewContent)) {
            binding.etReviewLayout.error = "Review must not be empty"
            return
        }
        if(selectedImageUri == null && !editExisting) {
            Toast.makeText(requireContext(),"Must pick image", Toast.LENGTH_LONG).show()
            return
        }


        binding.btnPost.isEnabled = false


        val user = viewModel.currentUser.value ?: return
        businessViewModel.addRating(
            editExisting = editExisting,
            businessId = bid,
            rating = if(editExisting) {
                existingRating!!.copy(content = reviewContent)
            }
            else {  Rating(
                id = "",
                businessId = bid,
                personName = user.name,
                content = reviewContent
            )},
            image = selectedImageUri,
            onSuccess = {
                binding.btnPost.isEnabled = true
                findNavController().popBackStack()
                Snackbar.make(binding.root, "Review successfully posted.", Snackbar.LENGTH_LONG).show()
            },
            onFailure = {
                binding.btnPost.isEnabled = true
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}