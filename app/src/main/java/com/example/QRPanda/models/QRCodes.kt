package com.example.QRPanda.models

import android.os.Parcel
import android.os.Parcelable

//Dados recebidos pela api, Parcelable para passar de uma atividade para outra
data class QRCodes(val messageQR: String, val _id: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(messageQR)
        parcel.writeString(_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QRCodes> {
        override fun createFromParcel(parcel: Parcel): QRCodes {
            return QRCodes(parcel)
        }

        override fun newArray(size: Int): Array<QRCodes?> {
            return arrayOfNulls(size)
        }
    }

}