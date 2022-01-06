package com.example.mainactivity.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String, val search: String) : Parcelable {
    constructor() : this("", "", "", "")
}
