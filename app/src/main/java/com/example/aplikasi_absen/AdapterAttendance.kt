package com.example.aplikasi_absen

import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_absen.model.AttendanceResponse
import com.example.aplikasi_absen.model.AttendanceResponseItem
import com.google.android.material.card.MaterialCardView
import kotlinx.datetime.toLocalTime
import java.sql.Date

class AdapterAttendance(val listAttendance: AttendanceResponse ): RecyclerView.Adapter<AdapterAttendance.AdapterViewHolder>() {

    var itemClickListener: ((AttendanceResponse) -> Unit)? = null
    class AdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardAttendance = itemView.findViewById<MaterialCardView>(R.id.listAttendanceCard)
        val namaKaryawan = itemView.findViewById<TextView>(R.id.itemListAttendance_tv_textNama)
        val statusKaryawan = itemView.findViewById<TextView>(R.id.itemListAttendance_tv_textStatus)
        val iconStatus = itemView.findViewById<ImageView>(R.id.itemListAttendance_img_iconStatus)

        fun bindviewStatus(status: AttendanceResponseItem) {
            namaKaryawan.text = status.nameKaryawan

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attendance, parent, false)
        return AdapterAttendance.AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val status = listAttendance[position]

        holder.bindviewStatus(status)

        val iconStatus = holder.iconStatus

        if(status.absenceReason.isNotEmpty()) {
            holder.namaKaryawan.setTextColor(Color.RED)
            holder.statusKaryawan.text = status.absenceReason
            holder.iconStatus.isGone = true
        } else if (status.attendStatus == "attended") {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            val format = SimpleDateFormat("HH:mm")
            val checkedIn = format.format(parser.parse(status.attendedAt))
            holder.iconStatus.setColorFilter(ContextCompat
                .getColor(holder.itemView.context, R.color.defautlGreen),
                android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.statusKaryawan.text = "Attended at ${checkedIn}"
        } else if (status.attendStatus == "checked_out") {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            val format = SimpleDateFormat("HH:mm")
            val checkedOut = format.format(parser.parse(status.checkedOut))
            holder.statusKaryawan.text = "Checked Out ${checkedOut}"
            holder.iconStatus.setImageResource(R.drawable.ic_baseline_no_meeting_room_24)
            iconStatus.setColorFilter(ContextCompat
                .getColor(holder.itemView.context, R.color.defautRed),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        }

        holder.cardAttendance.setOnClickListener {
            itemClickListener?.invoke(listAttendance)
        }

    }

    override fun getItemCount(): Int {
        return listAttendance.size
    }
}