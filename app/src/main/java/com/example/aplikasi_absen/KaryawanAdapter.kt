package com.example.aplikasi_absen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_absen.model.LoginResponse
import com.example.aplikasi_absen.model.LoginResponseItem


class KaryawanAdapter(val listKaryawan: List<LoginResponseItem>): RecyclerView.Adapter<KaryawanAdapter. KaryawanViewHolder>() {



    var deleteClickListener: ((LoginResponseItem) -> Unit)? = null
    class KaryawanViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val tvNamaKaryawan = itemView.findViewById<TextView>(R.id.itemListKaryawan_tv_NamaKaryawan)
        val tvPhoneNumber = itemView.findViewById<TextView>(R.id.itemListKaryawan_tv_PhoneNumber)
        val btnDelete = itemView.findViewById<ImageView>(R.id.itemListKarywan_img_btnDelete)



        fun bindview(karyawan: LoginResponseItem) {
            tvNamaKaryawan.text = karyawan.fullName
            tvPhoneNumber.text = karyawan.phone


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaryawanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_listkaryawan, parent, false)
        return KaryawanViewHolder(view)
    }

    override fun onBindViewHolder(holder: KaryawanViewHolder, position: Int) {
        val karyawan = listKaryawan[position]
        holder.bindview(karyawan)
        holder.btnDelete.setOnClickListener {
            deleteClickListener?.invoke(karyawan)
        }
    }

    override fun getItemCount(): Int {
        return listKaryawan.size
    }
}