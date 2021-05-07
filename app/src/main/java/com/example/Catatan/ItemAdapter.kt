package com.example.Catatan

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.Catatan.R
import kotlinx.android.synthetic.main.item_raw.view.*

class ItemAdapter(val context: Context, val items: ArrayList<EmpModel>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val no = view.tv_id
        val itemraw = view.Layer_item_raw
        val tv_namakegiatan = view.tv_kegiatan
        val tv_waktukegiatan = view.tv_waktu
        val tv_lokasikegiatan = view.tv_lokasi
        val iv_Delete = view.iv_delete
    }

    // method untuk membuat view holder
    // inflate = memunculkan data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    { return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_raw, parent, false
            )
        )
    }

    // memasukkan data ke view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.no.text = (position+1).toString()
        holder.tv_namakegiatan.text = item.nama_kegiatan
        holder.tv_waktukegiatan.text = item.waktu_kegiatan
        holder.tv_lokasikegiatan.text = item.lokasi_kegiatan

        if(position % 2 == 0) {
            holder.itemraw.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }else{
            holder.itemraw.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
        }
        holder.iv_Delete.setOnClickListener{
            if (context is History)
                context.deleteRecordAlertDialog(item!!)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}