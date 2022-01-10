package com.openclassrooms.realestatemanager.activity

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.core.widget.doOnTextChanged
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivitySimulatorBinding
import com.openclassrooms.realestatemanager.utils.Utils
import kotlin.math.pow

/**
 * Created by Julien Jennequin on 21/12/2021 10:47
 * Project : RealEstateManager
 **/
class SimulatorActivity : BaseActivity() {

    private lateinit var binding: ActivitySimulatorBinding

    companion object {
        private const val TAG = "SimulatorActivity"
    }

    //region variables of calcul
    var amount: Double = 0.0
    var percentagePerYears: Double = 0.0
    var term: Double = 1.0
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySimulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
    }

    private fun initListener() {
        binding.simulatorLoan.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                amount = text.toString().toDouble()
                calculate()
            }
        }

        binding.simulatorInteresetRate.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                percentagePerYears = text.toString().toDouble() / 100
                calculate()
            }
        }

        binding.simulatorYear.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.d(TAG, p1.toString())
                binding.seekBarValue.text = p1.toString()
                term = p1.toDouble()
                calculate()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun calculate() {
        // link of algo
        // https://www.creditexpert.fr/credit-immobilier/calculer-mensualites/
        //m = [(M*t)/12] / [1-(1+(t/12))^-n]

        if (amount > 0 && percentagePerYears != 0.00 && term != 0.00) {
            val result =
                ((amount * percentagePerYears) / 12) / (1 - (1 + (percentagePerYears / 12)).pow(
                    -(term * 12)
                ))

            val total = result * (term * 12)

            binding.simulatorMonthlyCount.text = Utils.formatPrice(result.toLong())
            binding.simulatorTotalCount.text = Utils.formatPrice(total.toLong())
        } else {
            binding.simulatorMonthlyCount.text = "0"
            binding.simulatorTotalCount.text = "0"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}