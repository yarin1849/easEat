package com.example.easeat.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.easeat.adapters.RatingsAdapter
import com.example.easeat.databinding.FragmentReviewsBinding
import com.example.easeat.json
import com.example.easeat.models.Business
import com.example.easeat.viewmodels.BusinessViewModel
import com.example.easeat.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RatingsFragment : Fragment(){


    private var _binding: FragmentReviewsBinding? = null
    private val binding: FragmentReviewsBinding get() = _binding!!

    private val authViewModel by activityViewModels<MainViewModel>()
    private val businessViewModel by activityViewModels<BusinessViewModel>()

    private val args by navArgs<RatingsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val business = json.decodeFromString<Business>(args.business)

        binding.tvRestaurantName.text = "${business.name}'s Reviews"
        businessViewModel.currentBusinessRatings.observe(viewLifecycleOwner) {
            binding.rvRatings.adapter = RatingsAdapter(it)
        }
    }
}