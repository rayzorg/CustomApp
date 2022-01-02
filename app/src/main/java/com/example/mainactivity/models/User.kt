package com.example.mainactivity.models

class User {
    private var  uid:String=""
    private var username:String=""
    private var profileImageUrl: String=""
    private var search:String=""
    private var status:String=""

    constructor()
    constructor(
        uid: String,
        username: String,
        profileImageUrl: String,
        search: String,
        status: String
    ) {
        this.uid = uid
        this.username = username
        this.profileImageUrl = profileImageUrl
        this.search = search
        this.status = status
    }

    fun getUID():String?{
        return uid
    }
    fun setUID(uid: String){
        this.uid=uid
    }
    fun getUsername():String?{
        return username
    }
    fun setUsername(username: String){
        this.username=username
    }
    fun getProfileImageUrl():String?{
        return profileImageUrl
    }
    fun setProfileImageUrl(profileImageUrl: String){
        this.profileImageUrl=profileImageUrl
    }
    fun getStatus():String?{
        return status
    }
    fun setStatus(status: String){
        this.status=status
    }
    fun getSearch():String?{
        return search
    }
    fun setSearch(search: String){
        this.search=search
    }
}