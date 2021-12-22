package com.openclassrooms.realestatemanager.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.ActivityDetailBinding
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.RealtyDetailViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory

/**
 * Created by Julien Jennequin on 22/12/2021 11:51
 * Project : RealEstateManager
 **/
class RealtyDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var realtyDetailViewModel: RealtyDetailViewModel

    private lateinit var realty: RealtyModel
    private var realtyId = ""

    companion object {
        private const val TAG = "RealtyDetailActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        realtyId = intent.extras?.let {
            it.get(Constants().REALTY_ID_EXTRAS).toString()
        }.toString()

        initUI()
        initViewModel()
        initListeners()
        initObservers()
    }

    fun initUI() {


    }

    fun initViewModel() {
        val mViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.realtyDetailViewModel =
            ViewModelProvider(this, mViewModelFactory).get(RealtyDetailViewModel::class.java)
    }

    fun initListeners() {

    }

    fun initObservers() {
       disposeBag +=  realtyDetailViewModel.getById(realtyId).subscribe(
            {result ->
                Log.d(TAG, result.toString())
                updateView(result)
            },
            {error ->
                Log.e(TAG, error.message.toString())
            }
        )

    }

    private fun updateView(realtyModel: RealtyModel) {
        binding.realtyDetailArea.text = realtyModel.area.toString() + " m2"
        binding.realtyDetailRoom.text = realtyModel.roomNumber.toString()
        //binding.realtyDetailBathroom!!.text = realtyModel.area.toString()
        //binding.realtyDetailBedroom!!.text = realtyModel.area.toString()
        binding.realtyDetailDescription.text = realtyModel.description
        Utils.getLocationFromAddress(binding.root.context,realtyModel.address)

    }
}