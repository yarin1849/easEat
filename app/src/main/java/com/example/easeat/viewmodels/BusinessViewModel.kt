package com.example.easeat.viewmodels

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.text.BoringLayout
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easeat.database.common.BusinessRepository
import com.example.easeat.database.common.FirebaseListener
import com.example.easeat.database.common.ProductRepository
import com.example.easeat.models.Business
import com.example.easeat.models.Product
import com.example.easeat.models.Rating
import com.example.easeat.models.util.LoadingState
import com.example.easeat.models.util.Location
import com.example.easeat.models.util.hasInRadius
import com.google.firebase.Firebase
import com.google.type.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val businessRepository: BusinessRepository,
    private val productRepository: ProductRepository,
    private val geocoder: Geocoder
): ViewModel() {

    private val _loading = MutableLiveData<LoadingState>(LoadingState.Loaded)
    val loading: LiveData<LoadingState> get() =  _loading

    private val _exceptions = MutableLiveData<Exception?>(null)
    val exceptions: LiveData<Exception?> get() =  _exceptions

    private val businessListener by lazy {
        businessRepository.listenToBusinesses(viewModelScope)
    }


    val businessResults = MutableLiveData(listOf<Business>())

    private val businessProductsMapping = HashMap<String, List<Product>>()
    private val businessRatingsMapping = HashMap<String, List<Rating>>()

    private var currentBusinessProductsListener : FirebaseListener<List<Product>>? = null
    private val _currentBusinessProducts = MediatorLiveData(listOf<Product>())
    val currentBusinessProducts : LiveData<List<Product>> get() = _currentBusinessProducts

    private var currentBusinessRatingsListener : FirebaseListener<List<Rating>>? = null
    private val _currentBusinessRatings = MediatorLiveData(listOf<Rating>())
    val currentBusinessRatings : LiveData<List<Rating>> get() = _currentBusinessRatings


    private val _currentBusinessProvidesServiceToClient = MutableLiveData(true)
    val currentBusinessProvidesServiceToClient: LiveData<Boolean> get() = _currentBusinessProvidesServiceToClient

    val businesses by lazy {
        businessListener.get()
    }


    fun searchBusiness(query:String) {

        val all = businesses.value ?: return
        if(query.isEmpty()) {
            businessResults.postValue(all)
            return
        }
        val filtered = all.filter  {
            it.name.lowercase().contains(query)
        }
        businessResults.postValue(filtered)
    }

    fun checkBusinessServiceLocation(business: Business,
                                     userAddress: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val locations = geocoder.getFromLocationName(userAddress,1) ?: return@launch
            if(locations.isEmpty()) {
                return@launch
            }
            val locationBusiness = business.location
            val locationUser = Location(
                latitude = locations[0].latitude,
                longtitude = locations[0].longitude
            )
             _currentBusinessProvidesServiceToClient.postValue(
                 locationBusiness.hasInRadius(business.deliveryRadius, locationUser)
             )
        }
    }


    fun addRating(businessId: String, rating: Rating, image:Uri, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            _loading.postValue(LoadingState.Loading)
            try {
                businessRepository.addRating(businessId, rating,image)
                onSuccess.invoke()
            } catch (e: Exception) {
                _exceptions.postValue(e)
                onFailure.invoke()
            } finally {
                _loading.postValue(LoadingState.Loaded)
            }
        }
    }
    fun getBusinessProducts(id: String) {
        val cached = businessProductsMapping[id]
        val cachedRatings = businessRatingsMapping[id]
        if(cached != null && cachedRatings != null) {
            _currentBusinessProducts.postValue(cached)
            _currentBusinessRatings.postValue(cachedRatings)
            return
        }
        currentBusinessRatingsListener = businessRepository.listenToBusinessRatings(id, viewModelScope)
        currentBusinessProductsListener = productRepository.listenToBusinessProducts(id,viewModelScope)
        _currentBusinessRatings.addSource(currentBusinessRatingsListener!!.get()) { ratings ->
            businessRatingsMapping[id] = ratings
            _currentBusinessRatings.postValue(ratings)
        }


        _currentBusinessProducts.addSource(currentBusinessProductsListener!!.get()) { products ->
            businessProductsMapping[id] = products
            _currentBusinessProducts.postValue(products)
        }
    }

    override fun onCleared() {
        super.onCleared()
        businessListener.stopListening()
        currentBusinessProductsListener?.stopListening()
    }

    fun setInitialSearchResults() {
        val businesses = businesses.value ?: return
        businessResults.postValue(businesses)
    }
}