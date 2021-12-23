package com.openclassrooms.realestatemanager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.databinding.ActivityDetailBinding
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.RealtyDetailViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.openclassrooms.realestatemanager.R


/**
 * Created by Julien Jennequin on 22/12/2021 11:51
 * Project : RealEstateManager
 **/
class DetailRealtyActivity : BaseActivity(), OnMapReadyCallback {

    //region PROPERTIES
    private lateinit var binding: ActivityDetailBinding
    private lateinit var realtyDetailViewModel: RealtyDetailViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var realty: RealtyModel

    private var realtyId = ""
    //endregion

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

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initViewModel()
        initListeners()
        initObservers()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        drawMarker(realty)
    }

    private fun initViewModel() {
        val mViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.realtyDetailViewModel =
            ViewModelProvider(this, mViewModelFactory).get(RealtyDetailViewModel::class.java)
    }

    private fun initListeners() {
        binding.realtyEdit.setOnClickListener {
            val intent = Intent(binding.root.context, EditRealtyActivity::class.java)
            intent.putExtra(Constants().REALTY_ID_EXTRAS, realty.id)
            startActivity(intent)
        }
    }

    private fun initObservers() {
        disposeBag += realtyDetailViewModel.getById(realtyId).subscribe(
            { result ->
                Log.d(TAG, result.toString())
                realty = result
                updateView(result)
            },
            { error ->
                Log.e(TAG, error.message.toString())
            }
        )
    }

    private fun updateView(realtyModel: RealtyModel) {
        binding.realtyDetailArea.text = realtyModel.area.toString() + " m2"
        binding.realtyDetailRoom.text = realtyModel.roomNumber.toString()
        binding.realtyDetailBathroom.text = realtyModel.bathRoom.toString()
        binding.realtyDetailBedroom.text = realtyModel.bedRoom.toString()
        binding.realtyDetailDescription.text = realtyModel.description
        binding.realtyDetailLocationAddress.text = realtyModel.address
    }

    private fun drawMarker(realtyModel: RealtyModel) {
        mMap.let {
            it.addMarker(
                MarkerOptions().position(
                    LatLng(
                        realtyModel.latitude,
                        realtyModel.longitude
                    )
                )
            )
            it.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        realtyModel.latitude,
                        realtyModel.longitude
                    ), 15.0F
                )
            )
        }
    }
}