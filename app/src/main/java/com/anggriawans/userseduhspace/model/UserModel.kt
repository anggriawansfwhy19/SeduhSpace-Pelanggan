package com.anggriawans.userseduhspace.model

data class UserModel(

    val name: String?=null,
    val email: String?=null,
    val password: String?=null,
    val phone: String?=null,
    val address: String?=null,
    val profileImage: String?=null,
    val paymentProof: String? = null,
    val tableNumber: String? = null
)
