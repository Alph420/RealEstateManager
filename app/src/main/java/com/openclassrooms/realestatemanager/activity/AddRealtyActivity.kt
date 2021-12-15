package com.openclassrooms.realestatemanager.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.databinding.ActivityAddRealtyBinding
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.utils.Constants

/**
 * Created by Julien Jennequin on 15/12/2021 15:24
 * Project : RealEstateManager
 **/
class AddRealtyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRealtyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRealtyBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initListeners()

    }

    fun initListeners() {
        binding.addRealtyAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(intent, ""), Constants().SELECT_PICTURE)
        }

        binding.addRealtyValidateBtn.setOnClickListener{
            saveRealty()
        }

    }

    fun initObservers() {

    }

    private fun saveRealty(){

    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

}