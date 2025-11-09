package com.aislados.clubdeportivo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aislados.clubdeportivo.model.CuotaVencida
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CuotasAdapter(private val cuotasList: List<CuotaVencida>) : RecyclerView.Adapter<CuotasAdapter.ViewHolder>() {

    // Esta clase interna representa una vista de tarjeta individual (un item).
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreSocio: TextView = view.findViewById(R.id.tv_nombre_socio)
        val detalleCuota: TextView = view.findViewById(R.id.tv_detalle_cuota)
    }

    // Crea una nueva vista de tarjeta inflando el layout XML.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cuota_vencida, parent, false)
        return ViewHolder(view)
    }

    // Devuelve el número total de items en la lista.
    override fun getItemCount(): Int {
        return cuotasList.size
    }

    // Conecta los datos de una cuota específica con una vista de tarjeta.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cuota = cuotasList[position]

        holder.nombreSocio.text = cuota.nombre
        val detalle = "F. Vencimiento: ${cuota.fechaVencimiento}\n" +
                "F. Pago: ${cuota.fechaPago}\n" +
                "Monto: ${cuota.monto}"
        holder.detalleCuota.text = detalle
    }
}