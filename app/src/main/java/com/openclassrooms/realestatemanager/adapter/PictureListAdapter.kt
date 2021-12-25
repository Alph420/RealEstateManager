package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PictureItemBinding

/**
 * Created by Julien Jennequin on 25/12/2021 15:13
 * Project : RealEstateManager
 **/
class PictureListAdapter(var dataList: List<String>) :
    RecyclerView.Adapter<PictureListAdapter.PictureViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PictureListAdapter.PictureViewHolder {
        return PictureListAdapter.PictureViewHolder(
            PictureItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val binding = holder.itemPictureBinding
        val context = binding.root.context
        val path = dataList[position]

        Glide.with(context)
            .load(path)
            .error(R.drawable.ic_error)
            .into(binding.imageView)



    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class PictureViewHolder(val itemPictureBinding: PictureItemBinding) :
        RecyclerView.ViewHolder(itemPictureBinding.root)

}