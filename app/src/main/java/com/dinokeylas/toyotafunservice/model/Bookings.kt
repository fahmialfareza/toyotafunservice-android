package com.dinokeylas.toyotafunservice.model

data class Bookings (
    var userId: String="",
    var userEmail: String="",
    var userName: String="",
    var date: String="",
    var time: String="",
    var province: String="",
    var city: String="",
    var garage: String="",
    var additionalService: String="",
    var officer: String="",
    var complaint: String="",
    var estimation: Int=0,
    var component: String="",
    var waitingApproval: Boolean=false,
    var totalCost: Double=0.0,
    var status: String=""
)