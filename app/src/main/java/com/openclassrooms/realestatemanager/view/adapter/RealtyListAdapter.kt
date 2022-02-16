package com.openclassrooms.realestatemanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealtyItemBinding
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.utils.Utils

/**
 * Created by Julien Jennequin on 11/12/2021 10:34
 * Project : RealEstateManager
 **/
class RealtyListAdapter(var dataList: List<Realty>) :
    RecyclerView.Adapter<RealtyListAdapter.RealtyViewHolder>() {
    private lateinit var listener: ItemClickListener

    fun setListener(listener: ItemClickListener) {
        this.listener = listener
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealtyViewHolder {
        return RealtyViewHolder(
            RealtyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RealtyViewHolder, position: Int) {
        val binding = holder.itemRealtyBinding
        val context = binding.root.context
        val realty = dataList[position]

        if (realty.pictures.isNotEmpty()) {
            Glide.with(context)
                .load(realty.pictures[0].path)
                .error(R.drawable.ic_error)
                .into(binding.pictures)
        }

        binding.realtyType.text = realty.kind
        binding.realtyLocation.text = realty.address
        binding.realtyPrice.text =
            context.getString(R.string.forex_symbole).plus(Utils.formatPrice(realty.price))

        if (!realty.available) {
            binding.realtyAvailable.setImageDrawable(context.getDrawable(R.drawable.ic_not_available))
        } else {
            binding.realtyAvailable.setImageDrawable(context.getDrawable(R.drawable.ic_available))
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class RealtyViewHolder(val itemRealtyBinding: RealtyItemBinding) :
        RecyclerView.ViewHolder(itemRealtyBinding.root)
}