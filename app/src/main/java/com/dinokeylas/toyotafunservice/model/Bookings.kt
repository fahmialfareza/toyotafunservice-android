package com.dinokeylas.toyotafunservice.model

data class Bookings (
    var userId: String,
    var date: String,
    var time: String,
    var province: String,
    var city: String,
    var garage: String,
    var additionalService: String,
    var officer: String,
    var complaint: String,
    var estimation: Int,
    var component: String,
    var waitingApproval: Boolean,
    var totalCost: Double,
    var status: String
)