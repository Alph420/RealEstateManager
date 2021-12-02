package com.openclassrooms.realestatemanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding

/**
 * Created by Julien Jennequin on 02/12/2021 15:32
 * Project : RealEstateManager
 **/
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        configureTextViewMain()
        configureTextViewQuantity()
    }

    private fun configureTextViewMain() {
        binding.activityMainActivityTextViewMain.textSize = 15f
        binding.activityMainActivityTextViewMain.text = "Le premier bien immobilier enregistré vaut "
    }

    private fun configureTextViewQuantity() {
        val quantity = Utils.convertDollarToEuro(100)
        binding.activityMainActivityTextViewQuantity.textSize = 20f
        binding.activityMainActivityTextViewQuantity.text = quantity.toString()
    }
}