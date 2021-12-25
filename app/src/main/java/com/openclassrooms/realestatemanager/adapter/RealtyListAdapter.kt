package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.databinding.RealtyItemBinding
import com.openclassrooms.realestatemanager.model.RealtyModel

/**
 * Created by Julien Jennequin on 11/12/2021 10:34
 * Project : RealEstateManager
 **/
class RealtyListAdapter(var dataList: List<RealtyModel>) :
    RecyclerView.Adapter<RealtyListAdapter.RealtyViewHolder>() {
    //Declarative interface
    private lateinit var listener: ItemClickListener

    //set method
    fun setListener(listener: ItemClickListener) {
        this.listener = listener
    }

    //Defining interface
    interface ItemClickListener {
        //Achieve the click method, passing the subscript.
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

        //TODO ADD PICTURES


        binding.realtyType.text = realty.kind
        binding.realtyLocation.text = realty.address
        binding.realtyPrice.text =
            context.getString(R.string.forex_symbole).plus(Utils.formatPrice(realty.price))

        if(!realty.available){
            binding.realtyAvailable.setImageDrawable(context.getDrawable(R.drawable.ic_not_available))
        }else{
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