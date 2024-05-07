package com.example.easeat.adapters

import com.example.easeat.models.Business

interface BusinessListEvents {
    fun onShowDetails(business: Business)
}