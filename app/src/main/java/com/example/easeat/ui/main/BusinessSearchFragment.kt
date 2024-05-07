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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easeat.adapters.BusinessAdapter
import com.example.easeat.adapters.BusinessListEvents
import com.example.easeat.databinding.FragmentBusinessBinding
import com.example.easeat.databinding.FragmentBusinessSearchBinding
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
import java.util.Calendar

@AndroidEntryPoint
class BusinessSearchFragment: Fragment() {

    private var _binding: FragmentBusinessSearchBinding? = null
    private val binding: FragmentBusinessSearchBinding get() = _binding!!
    private val authViewModel by activityViewModels<MainViewModel>()
    private val businessViewModel by activityViewModels<BusinessViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessSearchBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}