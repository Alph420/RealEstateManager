package com.openclassrooms.realestatemanager.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import org.osmdroid.config.Configuration.*
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
import com.openclassrooms.realestatemanager.database.AppDatabase.Companion.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.lang.StringBuilder


/**
 * Created by Julien Jennequin on 22/12/2021 11:51
 * Project : RealEstateManager
 **/
class DetailRealtyActivity : BaseActivity() {

    //region PROPERTIES
    private lateinit var binding: ActivityDetailBinding
    private lateinit var realtyDetailViewModel: RealtyDetailViewModel
    private lateinit var mMap: MapView
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


        initViewModel()
        initListeners()
        initObservers()
        initMap()
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

        binding.realtyDetailCenter.setOnClickListener {
            mMap.controller.setCenter(GeoPoint(realty.latitude, realty.longitude))
            mMap.controller.setZoom(14.0)
        }
    }

    private fun initObservers() {
        disposeBag += realtyDetailViewModel.getById(realtyId).subscribe(
            { result ->
                Log.d(TAG, result.toString())
                realty = result
                updateView()
            },
            { error ->
                Log.e(TAG, error.message.toString())
            }
        )
    }

    private fun initMap() {
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        mMap = binding.map
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.isTilesScaledToDpi = true
        mMap.setMultiTouchControls(true)
        mMap.minZoomLevel = 4.0
        mMap.maxZoomLevel = 21.0
        mMap.isVerticalMapRepetitionEnabled = false
    }

    private fun updateView() {
        Glide.with(this)
            .load(realty.pictures)
            .error(R.drawable.ic_error)
            .into(binding.recyclerView)


        binding.realtyDetailArea.text = realty.area.toString() + " m2"
        binding.realtyDetailRoom.text = realty.roomNumber.toString()
        binding.realtyDetailBathroom.text = realty.bathRoom.toString()
        binding.realtyDetailBedroom.text = realty.bedRoom.toString()
        binding.realtyDetailDescription.text = realty.description
        binding.realtyDetailLocationAddress.text = realty.address
        drawMarker()
    }

    private fun drawMarker() {
        val startMarker = Marker(mMap)
        startMarker.position = GeoPoint(realty.latitude, realty.longitude)
        startMarker.title = "${realty.kind}, ${realty.address}"
        mMap.overlays.add(startMarker)

        mMap.controller.setCenter(GeoPoint(realty.latitude, realty.longitude))
        mMap.controller.setZoom(12.0)
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }
}