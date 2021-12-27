package com.openclassrooms.realestatemanager.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.openclassrooms.realestatemanager.databinding.ActivityMapBinding
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.MapViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import androidx.core.content.res.ResourcesCompat

import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.dialog.NoGpsDialog


/**
 * Created by Julien Jennequin on 25/12/2021 10:50
 * Project : RealEstateManager
 **/
class MapActivity : BaseActivity() {

    //region PROPERTIES
    private lateinit var binding: ActivityMapBinding
    private lateinit var mapViewModel: MapViewModel
    private lateinit var mMap: MapView

    private var mLocation: GeoPoint = GeoPoint(0.0, 0.0)
    private var realtyList: List<RealtyModel> = emptyList()
    //endregion

    companion object {
        private const val TAG = "MapActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initViewModel()
        initListeners()
        initObservers()
        initMap()
        checkPermission()
    }


    private fun initViewModel() {
        val mViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.mapViewModel =
            ViewModelProvider(this, mViewModelFactory).get(MapViewModel::class.java)
    }

    private fun initListeners() {


    }

    private fun initObservers() {
        disposeBag += mapViewModel.getAllRealty().subscribe(
            { result ->
                Log.d(TAG, result.toString())
                realtyList = result
                drawMarker()
            },
            { error ->
                Log.e(TAG, error.message.toString())
            }
        )
        val mLocationObserver: Observer<GeoPoint> = Observer {
            setUserMarker(it)
        }

        mapViewModel.mLocationLiveData.observe(this, mLocationObserver)
    }

    private fun initMap() {
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        mMap = binding.map
        mMap.controller.setZoom(2.0)
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.isTilesScaledToDpi = true
        mMap.setMultiTouchControls(true)
        mMap.isVerticalMapRepetitionEnabled = false
    }

    private fun drawMarker() {
        realtyList.forEach {
            val startMarker = Marker(mMap)
            startMarker.position = GeoPoint(it.latitude, it.longitude)
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            startMarker.title = " ${it.kind}, ${it.address}"
            mMap.overlays.add(startMarker)

            startMarker.setOnMarkerClickListener { marker, mapView ->
                val intent = Intent(binding.root.context, DetailRealtyActivity::class.java)
                intent.putExtra(Constants().REALTY_ID_EXTRAS, (it.id))
                startActivity(intent)
                true
            }
        }
    }

    private fun setUserMarker(geoPoint: GeoPoint) {
        val userMarker = Marker(mMap)
        userMarker.position = geoPoint
        userMarker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_location, null)

        userMarker.title = "Our last position"
        mMap.overlays.add(userMarker)
    }

    private fun checkPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                getLocation()
            }else{
                noLocationError()
            }
        }
    }

    private fun getLocation() {
        if (mapViewModel.isLocationEnabled(this)) {
            mapViewModel.getLastLocation(this)
        } else {
            Log.d("getLocation", "Permissions refused")
            noLocationError()
        }

    }

    private fun noLocationError() {
        val dialog = NoGpsDialog(this)
        dialog.showForegroundGpsDialog()
    }

}