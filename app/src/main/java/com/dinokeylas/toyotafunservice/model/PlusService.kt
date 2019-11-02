package com.dinokeylas.toyotafunservice.model

class PlusService (val names: String, val checkeds: Boolean = false){
    var name: String = names
        set(value) {field = value}
        get() = field

    var checked: Boolean = checkeds
        set(value) {field = value}
        get() = field
}