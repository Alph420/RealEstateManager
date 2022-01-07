package com.openclassrooms.realestatemanager.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.Gravity.LEFT
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.adapter.RealtyListAdapter
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import androidx.recyclerview.widget.DividerItemDecoration
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.adapter.PictureModelAdapter
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.plusAssign
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


/**
 * Created by Julien Jennequin on 02/12/2021 15:32
 * Project : RealEstateManager
 **/
class MainActivity : BaseActivity() {

    //region PROPERTIES
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var realtyAdapter: RealtyListAdapter
    private lateinit var pictureRealtyAdapter: PictureModelAdapter
    private lateinit var realty: Realty
    private lateinit var mMap: MapView

    private var realtyList: List<Realty> = emptyList()
    private var picturesList = emptyList<PicturesModel>()
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
        initRealyRecylerView()
        if (binding.root.tag.equals(Constants().TAG_LARGE_MAIN_ACTIVITY)) {
            initMap()
            initPictureRecyclerView()
        }
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
            startActivity(Intent(this, SearchActivity::class.java))

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

        R.id.mapItem -> {
            startActivity(Intent(this, MapActivity::class.java))
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
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        mMap = binding.map!!
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.isTilesScaledToDpi = true
        mMap.setMultiTouchControls(true)
        mMap.minZoomLevel = 4.0
        mMap.maxZoomLevel = 21.0
        mMap.isVerticalMapRepetitionEnabled = false
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
                R.id.searchItem -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                }
                R.id.addItem -> {
                    startActivity(Intent(this, AddRealtyActivity::class.java))
                }
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.simulator -> {
                    startActivity(Intent(this, SimulatorActivity::class.java))
                }
                R.id.mapItem -> {
                    startActivity(Intent(this, MapActivity::class.java))
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
        disposeBag += mainViewModel.getAllRealty().subscribe(
            { result ->
                Log.d(TAG, result.toString())
                realtyList = result
                updateView()
                initDetailPart()
                initDetailPart()
            },
            { error ->
                Log.e(TAG, error.message.toString())
            }
        )
    }

    private fun initDetailPart() {
        if (empty && binding.root.tag == Constants().TAG_LARGE_MAIN_ACTIVITY) {
            if (realtyList.isNotEmpty()) {
                realty = realtyList[0]
                setDataOfRetail()
                drawMarker()
                empty = false
            }
        }
    }

    private fun initRealyRecylerView() {
        this.realtyAdapter = RealtyListAdapter(realtyList)

        binding.realtyRecyclerView.adapter = this.realtyAdapter
        binding.realtyRecyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.realtyRecyclerView.context,
            (binding.realtyRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        binding.realtyRecyclerView.addItemDecoration(dividerItemDecoration)

        realtyAdapter.setListener(object : RealtyListAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                if (binding.root.tag == Constants().TAG_LARGE_MAIN_ACTIVITY) {
                    realty = realtyList[position]
                    setDataOfRetail()
                    drawMarker()
                } else {
                    val intent = Intent(binding.root.context, DetailRealtyActivity::class.java)
                    intent.putExtra(Constants().REALTY_ID_EXTRAS, (realtyList[position].id))
                    startActivity(intent)
                }
            }
        })
    }

    private fun initPictureRecyclerView() {
        this.pictureRealtyAdapter = PictureModelAdapter(picturesList)

        binding.recyclerView!!.adapter = this.pictureRealtyAdapter
    }

    private fun setDataOfRetail() {
        updatePictures()
        binding.realtyDetailArea!!.text = realty.area.toString() + this.getString(R.string.m2)
        binding.realtyDetailRoom!!.text = realty.roomNumber.toString()
        binding.realtyDetailBathroom!!.text = realty.bathRoom.toString()
        binding.realtyDetailBedroom!!.text = realty.bedRoom.toString()
        binding.realtyDetailDescription!!.text = realty.description
        binding.realtyDetailLocationAddress!!.text =
            "${realty.address}, ${realty.city}, ${realty.region}, ${realty.department}, ${realty.country}"
        binding.realtyDetailNearPlaces!!.text =
            realty.pointOfInterest.toString().replace("[", "").replace("]", "").replace(", ", "\n")
    }

    private fun drawMarker() {
        mMap.controller.setCenter(GeoPoint(realty.latitude, realty.longitude))
        mMap.controller.setZoom(15.0)
        val startMarker = Marker(mMap)
        startMarker.position = GeoPoint(realty.latitude, realty.longitude)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mMap.overlays.add(startMarker)
    }


    private fun checkIfWifiIsAvailable() {
        if (Utils.isInternetAvailable(this)) {
            Toast.makeText(this, this.getString(R.string.wifi_available), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateView() {
        realtyAdapter.dataList = realtyList
        realtyAdapter.notifyDataSetChanged()
    }

    private fun updatePictures() {
        pictureRealtyAdapter.dataList = realty.pictures

        if (realty.pictures.isEmpty()) {
            binding.emptyView!!.visibility = View.VISIBLE
        } else {
            binding.emptyView!!.visibility = View.INVISIBLE
        }
        pictureRealtyAdapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        if (binding.root.tag.equals(Constants().TAG_LARGE_MAIN_ACTIVITY)) {
            binding.map!!.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (binding.root.tag.equals(Constants().TAG_LARGE_MAIN_ACTIVITY)) {
            binding.map!!.onPause()
        }
    }
}