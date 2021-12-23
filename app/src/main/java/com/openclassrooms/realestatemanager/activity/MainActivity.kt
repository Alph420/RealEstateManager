package com.openclassrooms.realestatemanager.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.Gravity.LEFT
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.adapter.RealtyListerAdapter
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.plusAssign


/**
 * Created by Julien Jennequin on 02/12/2021 15:32
 * Project : RealEstateManager
 **/
class MainActivity : BaseActivity(), OnMapReadyCallback {

    //region PROPERTIES
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: RealtyListerAdapter
    private lateinit var realty: RealtyModel
    private lateinit var mMap: GoogleMap

    private var realtyList: List<RealtyModel> = emptyList()
    private var empty = true
    //endregion

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        initUI()
        initViewModel()
        initListeners()
        initObservers()
        initRecyclerView()
        initMap()
        checkIfWifiIsAvailable()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.addItem -> {
            startActivity(Intent(this, AddRealtyActivity::class.java))
            true
        }

        R.id.searchItem -> {
            //TODO  SearchRealty
            true
        }
        R.id.simulator -> {
            startActivity(Intent(this, SimulatorActivity::class.java))
            true
        }

        R.id.settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun initUI() {
        binding.topAppBar.overflowIcon?.colorFilter = PorterDuffColorFilter(
            ResourcesCompat.getColor(
                resources,
                R.color.green_money_twice,
                null
            ), PorterDuff.Mode.SRC_ATOP
        )

        binding.appBuildVersion.text = BuildConfig.VERSION_NAME
    }

    private fun initMap() {
        if (binding.root.tag.equals(Constants().TAG_LARGE_MAIN_ACTIVITY)) {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        drawMarker(realty)
    }

    private fun initViewModel() {
        val mViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.mainViewModel =
            ViewModelProvider(this, mViewModelFactory).get(MainViewModel::class.java)
    }

    private fun initListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(LEFT); //CLOSE Nav Drawer!
            } else {
                binding.drawerLayout.openDrawer(LEFT); //OPEN Nav Drawer!
            }
        }

        binding.nvView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.addItem -> {
                    startActivity(Intent(this, AddRealtyActivity::class.java))
                }
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.simulator -> {
                    startActivity(Intent(this, SimulatorActivity::class.java))

                }
            }
            return@setNavigationItemSelectedListener true
        }

        if (binding.root.tag.equals(Constants().TAG_LARGE_MAIN_ACTIVITY)) {
            binding.realtyEdit!!.setOnClickListener {
                val intent = Intent(binding.root.context, EditRealtyActivity::class.java)
                intent.putExtra(Constants().REALTY_ID_EXTRAS, realty.id)
                startActivity(intent)
            }
        }

    }

    private fun initObservers() {
        disposeBag += mainViewModel.getAll().subscribe(
            { result ->
                Log.d(TAG, result.toString())
                realtyList = result
                updateView(result)
                initDetailPart(result)
            },
            { error ->
                Log.e(TAG, error.message.toString())
            }
        )
    }

    private fun initDetailPart(result: List<RealtyModel>) {
        if (empty && binding.root.tag == Constants().TAG_LARGE_MAIN_ACTIVITY) {
            if (result.isNotEmpty()){
                setDataOfRetail(result[0])
                realty = result[0]
                empty = false
            }
        }
    }

    private fun initRecyclerView() {
        this.adapter = RealtyListerAdapter(realtyList)

        binding.realtyRecyclerView.adapter = this.adapter
        binding.realtyRecyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.realtyRecyclerView.context,
            (binding.realtyRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        binding.realtyRecyclerView.addItemDecoration(dividerItemDecoration)

        adapter.setListener(object : RealtyListerAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                if (binding.root.tag == Constants().TAG_LARGE_MAIN_ACTIVITY) {
                    setDataOfRetail(realtyList[position])
                    realty = realtyList[position]
                    drawMarker(realty)

                } else {
                    val intent = Intent(binding.root.context, RealtyDetailActivity::class.java)
                    intent.putExtra(Constants().REALTY_ID_EXTRAS, (realtyList[position].id))
                    startActivity(intent)
                }
            }
        })


    }

    private fun setDataOfRetail(realtyModel: RealtyModel) {
        binding.realtyDetailArea!!.text = realtyModel.area.toString() + " m2"
        binding.realtyDetailRoom!!.text = realtyModel.roomNumber.toString()
        binding.realtyDetailBathroom!!.text = realtyModel.bathRoom.toString()
        binding.realtyDetailBedroom!!.text = realtyModel.bedRoom.toString()
        binding.realtyDetailDescription!!.text = realtyModel.description
        binding.realtyDetailLocationAddress!!.text = realtyModel.address
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


    private fun checkIfWifiIsAvailable() {
        if (Utils.isInternetAvailable(this)) {
            Toast.makeText(this, "Wifi available", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateView(result: List<RealtyModel>) {
        adapter.dataList = result
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
    }

}