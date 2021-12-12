package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Utils
import com.openclassrooms.realestatemanager.databinding.RealtyItemBinding
import com.openclassrooms.realestatemanager.model.RealtyModel

/**
 * Created by Julien Jennequin on 11/12/2021 10:34
 * Project : RealEstateManager
 **/
class RealtyListerAdapter(var dataList:List<RealtyModel>) : RecyclerView.Adapter<RealtyListerAdapter.RealtyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealtyViewHolder {
        return RealtyViewHolder(RealtyItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RealtyViewHolder, position: Int) {
        val binding = holder.itemRealtyBinding
        val context = binding.root.context
        val realty = dataList[position]

        Glide.with(binding.pictures)
            .load(realty.pictures)
            .error(R.drawable.ic_error)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(10)))
            .into(binding.pictures)

        binding.realtyType.text = realty.kind
        binding.realtyLocation.text = realty.address
        binding.realtyPrice.text =  context.getString(R.string.forex_symbole).plus(Utils.formatPrice(realty.price))


    }

    override fun getItemCount(): Int {
      return dataList.size
    }

    class RealtyViewHolder(val itemRealtyBinding:RealtyItemBinding):
        RecyclerView.ViewHolder(itemRealtyBinding.root)

}