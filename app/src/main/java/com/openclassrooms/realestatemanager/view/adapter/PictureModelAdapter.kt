package com.openclassrooms.realestatemanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PictureItemBinding
import com.openclassrooms.realestatemanager.model.PicturesModel

/**
 * Created by Julien Jennequin on 28/12/2021 10:19
 * Project : RealEstateManager
 **/
class PictureModelAdapter(var dataList: List<PicturesModel>) :
    RecyclerView.Adapter<PictureModelAdapter.PictureViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PictureViewHolder {
        return PictureViewHolder(
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
        val picture = dataList[position]

        Glide.with(context)
            .load(picture.path)
            .error(R.drawable.ic_error)
            .into(binding.pictureImage)

        binding.pictureName.text = picture.name
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class PictureViewHolder(val itemPictureBinding: PictureItemBinding) :
        RecyclerView.ViewHolder(itemPictureBinding.root)

}