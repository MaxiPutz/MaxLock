package com.maxiputz.keychainclient.structs


//Title,URL,Username,Password,Notes,OTPAuth

class PasswordEntry  {

    var title : String

    var url: String

    var username: String

    var password : String

    var notes : String

    var OTPAuth : String

    constructor(
        title: String,
        website: String,
        username: String,
        password :String,
        notes : String,
        OTPAuth : String
    ) {
        this.title = title
        this.url = website
        this.username = username
        this.password = password
        this.notes = notes
        this.OTPAuth = OTPAuth
    }

}