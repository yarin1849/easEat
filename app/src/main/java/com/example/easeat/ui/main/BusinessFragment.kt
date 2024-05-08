package com.example.easeat.ui.auth

import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easeat.R
import com.example.easeat.adapters.BusinessAdapter
import com.example.easeat.adapters.BusinessListEvents
import com.example.easeat.adapters.ProductListEvents
import com.example.easeat.adapters.ProductsAdapter
import com.example.easeat.databinding.FragmentBusinessBinding
import com.example.easeat.databinding.FragmentHomeBinding
import com.example.easeat.databinding.FragmentLoginBinding
import com.example.easeat.json
import com.example.easeat.models.Business
import com.example.easeat.models.Product
import com.example.easeat.models.dto.RegisterDto
import com.example.easeat.models.size
import com.example.easeat.models.totalPrice
import com.example.easeat.observeNotNull
import com.example.easeat.viewmodels.BusinessViewModel
import com.example.easeat.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import java.util.Calendar

@AndroidEntryPoint
class BusinessFragment: Fragment(), ProductListEvents {

    private var _binding: FragmentBusinessBinding? = null
    private val binding: FragmentBusinessBinding get() = _binding!!
    private val authViewModel by activityViewModels<MainViewModel>()
    private val businessViewModel by activityViewModels<BusinessViewModel>()


    private val args by navArgs<BusinessFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val business = json.decodeFromString<Business>(args.business)
        binding.tvRestaurantName.text = business.name
        binding.btnSendOrder.setOnClickListener {
            authViewModel.sendOrder(
                onAddOrder =  {
                    val action = BusinessFragmentDirections.actionBusinessFragmentToAddReviewFragment(args.business,null)
                    findNavController().navigate(action)
                },
                onCannotSendOrder = { reason->
                    Snackbar.make(view, reason ?: "Could not send order, try again later", Snackbar.LENGTH_LONG).show()
                }
            )
        }

        binding.btnReviews.setOnClickListener {
            val action = BusinessFragmentDirections.actionBusinessFragmentToRatingsFragment(args.business)
            findNavController().navigate(action)
        }

        authViewModel.startOrderOnBusiness(business.id)
        businessViewModel.currentBusinessProducts.observe(viewLifecycleOwner) {

            Log.d("PRODUCTS!", it.size.toString())
            binding.rvProducts.adapter = ProductsAdapter(it, this)
        }


        businessViewModel.currentBusinessProvidesServiceToClient
            .observe(viewLifecycleOwner) {

            binding.providesServiceTv.text = if(it) {
                getString(R.string.this_restaurant_provides_service_in_your_area)
            }
            else {
                getString(R.string.this_restaurant_provides_service_in_your_area_no)
            }
        }

        businessViewModel.getBusinessProducts(business.id)

        val user = authViewModel.currentUser.value ?: return

        businessViewModel.checkBusinessServiceLocation(business, user.address)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun addToOrder(product: Product, quantity: Int) {
        authViewModel.addItemToOrder(product, quantity)
        Snackbar.make(binding.root, "Added $quantity ${product.name}", Snackbar.LENGTH_LONG).show()
        updateOrderDetails()
    }

    override fun removeFromOrder(product: Product) {
       authViewModel.removeItemFromOrder(product)
        Snackbar.make(binding.root, "Removed ${product.name} from order", Snackbar.LENGTH_LONG).show()
        updateOrderDetails()
    }

    private fun updateOrderDetails() {
        val order = authViewModel.sessionOrder.value ?: return

        val itemsInOrder = order.size()
        val totalPrice = order.totalPrice()

        binding.tvOrderDetails.text = String.format("Items in order: %d, total price: %.2f$", itemsInOrder, totalPrice)

    }

}