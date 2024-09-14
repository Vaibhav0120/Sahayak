package com.example.thirdapplication.Domain

import android.graphics.drawable.Icon
import android.health.connect.datatypes.units.Percentage
import android.os.Parcel
import android.os.Parcelable

data class WeatherDomain(
    val title: String="",
    val description: String="",
    val icon: String="",
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherDomain> {
        override fun createFromParcel(parcel: Parcel): WeatherDomain {
            return WeatherDomain(parcel)
        }

        override fun newArray(size: Int): Array<WeatherDomain?> {
            return arrayOfNulls(size)
        }
    }
}
