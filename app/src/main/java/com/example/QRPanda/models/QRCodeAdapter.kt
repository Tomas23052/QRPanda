package com.example.QRPanda.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.QRPanda.models.QRCodeAdapter.QRCodeViewHolder
import com.example.QRPanda.R


//Adaptador da RecyclerView
class QRCodeAdapter : RecyclerView.Adapter<QRCodeViewHolder> {
    var codesList: List<QRCodes>? = null
    var context: Context
    var onItemClick : ((QRCodes) -> Unit)? = null

    constructor(context: Context, codes: List<QRCodes>?) {
        this.context = context
        codesList = codes
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QRCodeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return QRCodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: QRCodeViewHolder, position: Int) {
        val qrCodes = codesList!![position]
        holder.code.text = qrCodes.messageQR
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(qrCodes)
        }
    }

    override fun getItemCount(): Int {
        return codesList!!.size
    }

    inner class QRCodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var code: TextView

        init {
            code = itemView.findViewById(R.id.txv_qrcodes)
        }
    }


}