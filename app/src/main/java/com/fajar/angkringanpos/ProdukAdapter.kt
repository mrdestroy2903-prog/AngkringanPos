package com.fajar.angkringanpos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class ProdukAdapter(
    private val listProduk: List<Produk>,
    private val onItemClick: ((Produk) -> Unit)? = null
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

        // Tampilkan harga dengan format Rupiah yang rapi (Contoh: Rp 4.000)
        holder.txtHarga.text = formatRupiah(produk.hargaJual.toLong())

        // Stok tetap angka biasa sesuai permintaan mas
        holder.txtStok.text = "Stok: ${produk.stok}"

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(produk)
        }
    }

    override fun getItemCount(): Int {
        return listProduk.size
    }

    // Fungsi Helper untuk memformat angka ke Rupiah
    private fun formatRupiah(number: Long): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.maximumFractionDigits = 0
        return numberFormat.format(number).replace("Rp", "Rp ").trim()
    }
}