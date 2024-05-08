package com.example.easeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easeat.database.common.UserRepository
import com.example.easeat.models.Order
import com.example.easeat.models.Product
import com.example.easeat.models.dto.RegisterDto
import com.example.easeat.models.util.LoadingState
import com.example.easeat.models.util.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository):  ViewModel() {

    val currentUser = userRepository.getCurrentUser()
    val currentUserOrders = userRepository.getCurrentUserOrders()

    private val _loading = MutableLiveData<LoadingState>(LoadingState.Loaded)
    val loading: LiveData<LoadingState> get() =  _loading

    private val _exceptions = MutableLiveData<Exception?>(null)
    val exceptions: LiveData<Exception?> get() =  _exceptions

    private val _sessionOrder = MutableLiveData(Order())
    val sessionOrder: LiveData<Order> get() = _sessionOrder

    fun signOut() {
        userRepository.signOut()
    }

    fun addItemToOrder(product: Product, quantity: Int) {
        val order = _sessionOrder.value ?: return
        order.items.add(OrderItem(
            quantity=quantity,
            productId = product.id,
            price = product.price)
        )
        _sessionOrder.postValue(order)
    }
    fun removeItemFromOrder(product: Product) {
        val order = _sessionOrder.value ?: return
        order.items.removeIf { it.productId == product.id }
        _sessionOrder.postValue(order)
    }

    fun startOrderOnBusiness(id: String) {
        val order = _sessionOrder.value ?: return
        order.businessId = id
        order.items = mutableListOf()
        _sessionOrder.postValue(order)
    }

    fun sendOrder(onAddOrder: () -> Unit, onCannotSendOrder: (String?) -> Unit) {
        val order = _sessionOrder.value ?: return
        val user = currentUser.value ?: return

        if(order.items.isEmpty()) {
            onCannotSendOrder.invoke("Order must have at-least 1 item")
            return
        }
        order.orderAddress = user.address
        order.orderDate = System.currentTimeMillis()
        viewModelScope.launch {
            _loading.postValue(LoadingState.Loading)
            try {
                userRepository.addOrder(order)
                onAddOrder.invoke()
            } catch (e: Exception) {
                _exceptions.postValue(e)
                onCannotSendOrder.invoke(e.message)
            } finally {
                _loading.postValue(LoadingState.Loaded)
            }
        }
    }



    fun login(email: String, password: String, successCallback: () -> Unit, failCallback: () -> Unit) {
        _loading.postValue(LoadingState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.login(email,password)
                withContext(Dispatchers.Main) {
                    successCallback.invoke()
                }
            }
            catch(e: Exception) {
                _exceptions.postValue(e)
                withContext(Dispatchers.Main) {
                    failCallback.invoke()
                }
            }
            finally {
                _loading.postValue(LoadingState.Loaded)
            }
        }
    }

    fun register(registerDto: RegisterDto, successCallback: () -> Unit, failCallback: () -> Unit) {
        _loading.postValue(LoadingState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.register(viewModelScope, registerDto)
                withContext(Dispatchers.Main) {
                    successCallback.invoke()
                }
            }
            catch(e: Exception) {
                _exceptions.postValue(e)
                withContext(Dispatchers.Main) {
                    failCallback.invoke()
                }
            }
            finally {
                _loading.postValue(LoadingState.Loaded)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        userRepository.stopListening()
    }
}