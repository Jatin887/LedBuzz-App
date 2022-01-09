package com.example.ledbuzz.model


data class Subject(
    var time : Long? = 0,
    var priority:String? = null,
    var alertStatus:String? = null,
    var subjectName:String?=null
)
