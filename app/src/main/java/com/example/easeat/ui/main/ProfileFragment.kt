package com.example.easeat.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.easeat.adapters.UserRatingListEvents
import com.example.easeat.adapters.UserRatingsAdapter
import com.example.easeat.databinding.FragmentHomeBinding
import com.example.easeat.databinding.FragmentProfileBinding
import com.example.easeat.json
import com.example.easeat.models.Rating
import com.example.easeat.observeNotNull
import com.example.easeat.viewmodels.BusinessViewModel
import com.example.easeat.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString


@AndroidEntryPoint
class ProfileFragment : Fragment(), UserRatingListEvents {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!
    private val authViewModel by activityViewModels<MainViewModel>()
    private val businessViewModel by activityViewModels<BusinessViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.currentUserRatings.observeNotNull(viewLifecycleOwner) {
            binding.rvMyReviews.adapter = UserRatingsAdapter(it.toMutableList(), this)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun editRating(rating: Rating) {
        val businesses = businessViewModel.businesses.value ?: return
        val business = businesses.firstOrNull { it.id == rating.businessId  } ?: return
        val action = ProfileFragmentDirections.actionProfileFragmentToAddReviewFragment(
            json.encodeToString(business),
            json.encodeToString(rating)
        )
        findNavController().navigate(action)
    }

    override fun deleteRating(rating: Rating) {
        val businesses = businessViewModel.businesses.value ?: return
        val business = businesses.firstOrNull { it.id == rating.businessId  } ?: return
        businessViewModel.deleteRating(business.id, rating.id)
    }

}