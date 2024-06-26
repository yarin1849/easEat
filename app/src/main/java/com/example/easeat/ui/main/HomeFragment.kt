package com.example.easeat.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easeat.adapters.BusinessAdapter
import com.example.easeat.adapters.BusinessListEvents
import com.example.easeat.databinding.FragmentHomeBinding
import com.example.easeat.databinding.FragmentLoginBinding
import com.example.easeat.json
import com.example.easeat.models.Business
import com.example.easeat.models.dto.RegisterDto
import com.example.easeat.observeNotNull
import com.example.easeat.viewmodels.BusinessViewModel
import com.example.easeat.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment: Fragment(), BusinessListEvents {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val authViewModel by activityViewModels<MainViewModel>()
    private val businessViewModel by activityViewModels<BusinessViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRestaurants.layoutManager = LinearLayoutManager(requireContext())
        businessViewModel.businesses.observe(viewLifecycleOwner) { businesses ->
            val adapter = BusinessAdapter(businesses, this)
            binding.rvRestaurants.adapter = adapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onShowDetails(business: Business) {
        val businessJson = json.encodeToString(business)
        val action = HomeFragmentDirections.actionHomeFragmentToBusinessFragment(businessJson)
        findNavController().navigate(action)
    }
}