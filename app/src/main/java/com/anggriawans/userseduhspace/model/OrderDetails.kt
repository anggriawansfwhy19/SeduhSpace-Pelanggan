package com.anggriawans.userseduhspace.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class OrderDetails():Serializable {
    var userUid: String? =null
    var userName: String? =null
    var coffeeNames: MutableList<String>? = null
    var coffeeImages: MutableList<String>? = null
    var coffeePrices: MutableList<String>? = null
    var coffeeQuantities: MutableList<Int>? = null
    var address: String? =null
    var tableNumber: String? = null
    var totalPrice: String? =null
    var phoneNumber: String? =null
    var orderAccepted: Boolean = false
    var paymentReceived: Boolean = false
    var itemPushKey: String? =null
    var currentTime: Long = 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        tableNumber = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        coffeeItemName: ArrayList<String>,
        coffeeItemPrice: ArrayList<String>,
        coffeeItemImage: ArrayList<String>,
        coffeeItemQuantities: ArrayList<Int>,
        address: String,
        tableNumber: String,
        totalAmount: String,
        phone: String,
        time: Long,
        itemPushKey: String?,
        isAccepted: Boolean,
        isCompleted: Boolean
    ) : this() {
        this.userUid = userId
        this.userName = name
        this.coffeeNames = coffeeItemName
        this.coffeePrices = coffeeItemPrice
        this.coffeeImages = coffeeItemImage
        this.coffeeQuantities = coffeeItemQuantities
        this.address = address
        this.tableNumber = tableNumber
        this.totalPrice = totalAmount
        this.phoneNumber = phone
        this.currentTime = time
        this.itemPushKey = itemPushKey
        this.orderAccepted = isAccepted
        this.paymentReceived = isCompleted
    }

    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(tableNumber)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}