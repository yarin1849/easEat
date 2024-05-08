package com.example.easeat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.easeat.databinding.ProductItemBinding
import com.example.easeat.models.Product
import com.squareup.picasso.Picasso
import kotlin.math.max
import kotlin.math.min

class ProductsAdapter(
    private val products: List<Product>,
    private val events: ProductListEvents
): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {
    val orderProductsMap = hashMapOf<String, Int>().apply { for(p in products) { put(p.id, 0) } }

    val currentInOrder = hashMapOf<String, Boolean>().apply { for(p in products) { put(p.id, false) } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
         return ProductsViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, position)
    }


    inner class ProductsViewHolder(private val binding: ProductItemBinding) : ViewHolder(binding.root) {

        fun bind(product: Product, position: Int) {

            val quantity = orderProductsMap[product.id] ?: 0

            binding.addMinus.setOnClickListener {
                orderProductsMap.computeIfPresent(product.id) {_, quantity->
                    var newQuantity = (quantity - 1)
                    newQuantity = max(0, newQuantity)
                    binding.tvAmount.text = newQuantity.toString()
                    newQuantity
                }
            }
            binding.addPlus.setOnClickListener {
                orderProductsMap.computeIfPresent(product.id) {_, quantity->
                    var newQuantity = (quantity + 1)
                    newQuantity = min(product.stock, newQuantity)
                    binding.tvAmount.text = newQuantity.toString()
                    newQuantity
                }

            }
            binding.addToOrder.setOnClickListener {
                events.addToOrder(product, orderProductsMap[product.id]!!)
                currentInOrder[product.id] = true
                notifyItemChanged(position)
            }

            binding.removeFromOrder.visibility = if(currentInOrder[product.id] == true) {
                binding.removeFromOrder.setOnClickListener {
                    events.removeFromOrder(product)
                    currentInOrder[product.id] = false
                    notifyItemChanged(position)
                }
                View.VISIBLE
            }
            else {
                View.GONE
            }


            Picasso.get().load(product.image).into(binding.ivFood)

            binding.tvFoodName.text = product.name
            binding.tvFoodDesc.text = product.desc
            binding.tvFoodPrice.text = String.format("%.2f$", product.price)
        }
    }
}