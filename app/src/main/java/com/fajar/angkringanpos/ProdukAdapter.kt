package com.fajar.angkringanpos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdukAdapter(
    private val listProduk: List<Produk>,
    private val onItemClick: ((Produk) -> Unit)? = null // Parameter tambahan untuk deteksi klik
) : RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder>() {

    class ProdukViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNamaProduk: TextView = itemView.findViewById(R.id.txtNamaProduk)
        val txtHarga: TextView = itemView.findViewById(R.id.txtHarga)
        val txtStok: TextView = itemView.findViewById(R.id.txtStok)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produk, parent, false)
        return ProdukViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        val produk = listProduk[position]

        holder.txtNamaProduk.text = produk.nama
        holder.txtHarga.text = "Rp ${produk.harga}"
        holder.txtStok.text = "Stok: ${produk.stok}"

        // Logika Klik: Hanya berjalan jika onItemClick diisi (seperti di halaman Kasir)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(produk)
        }
    }

    override fun getItemCount(): Int {
        return listProduk.size
    }
}