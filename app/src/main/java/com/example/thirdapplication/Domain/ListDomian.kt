package com.example.thirdapplication.Domain

import android.os.Parcel
import android.os.Parcelable

data class ListDomain(
    var title: String = "",
    val description: String = "",
    val pic: String = "",
    val detail: String = ""

):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(pic)
        parcel.writeString(detail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListDomain> {
        override fun createFromParcel(parcel: Parcel): ListDomain {
            return ListDomain(parcel)
        }

        override fun newArray(size: Int): Array<ListDomain?> {
            return arrayOfNulls(size)
        }
    }

}