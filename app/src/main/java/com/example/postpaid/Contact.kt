package com.example.postpaid

class Contact {
    //private variables
    // getting ID
    // setting id
    var id: Int = 0
    lateinit var phoneNumber: String
    lateinit var type: String
    lateinit var duration: String
    lateinit var date: String
    lateinit var time: String
    lateinit var startDate: String
    lateinit var endDate: String
    constructor() {

    }

    constructor(id: Int, _phone_number: String, type: String, duration: String, date: String, time: String) {
        this.id = id
        this.phoneNumber = _phone_number
        this.type = type
        this.duration = duration
        this.date = date
        this.time = time
    }

    constructor(_phone_number: String, type: String, duration: String, date: String, time: String) {
        this.phoneNumber = _phone_number
        this.type = type
        this.duration = duration
        this.date = date
        this.time = time
    }

    constructor(start_date: String, end_date: String) {

        this.startDate = start_date
        this.endDate = end_date
    }
}