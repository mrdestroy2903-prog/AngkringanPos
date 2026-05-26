package com.fajar.angkringanpos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdukAdapter(private val listProduk: ArrayList<Produk>) :
    RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder>() {

    class ProdukViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtNamaProduk: TextView =
            itemView.findViewById(R.id.txtNamaProduk)

        val txtHarga: TextView =
            itemView.findViewById(R.id.txtHarga)

        val txtStok: TextView =
            itemView.findViewById(R.id.txtStok)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produk, parent, false)

        return ProdukViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {

        val produk = listProduk[position]

        holder.txtNamaProduk.text = produk.nama
        holder.txtHarga.text = produk.harga
        holder.txtStok.text = produk.stok
    }

    override fun getItemCount(): Int {
        return listProduk.size
    }
}