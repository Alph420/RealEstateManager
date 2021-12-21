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
import androidx.appcompat.app.AppCompatActivity
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
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.utils.plusAssign
import io.reactivex.rxjava3.disposables.CompositeDisposable


/**
 * Created by Julien Jennequin on 02/12/2021 15:32
 * Project : RealEstateManager
 **/
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private var realtyList: List<RealtyModel> = emptyList()

    private lateinit var adapter: RealtyListerAdapter

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //TODO create two xml for tablet and smartphone
        //TODO and use fragment for display good fragment


        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.overflowIcon?.colorFilter = PorterDuffColorFilter(
            ResourcesCompat.getColor(
                resources,
                R.color.green_money_twice,
                null
            ), PorterDuff.Mode.SRC_ATOP
        )

        initUI()
        initViewModel()
        initListeners()
        initObservers()
        initRecyclerView()
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

        R.id.createItem -> {
            //TODO START ACTIVITY EditRealty
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
        binding.appBuildVersion.text = BuildConfig.VERSION_NAME
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

    }

    private fun initObservers() {
        disposeBag += mainViewModel.getAll().subscribe(
            { result ->
                Log.d(TAG, result.toString())
                realtyList = result
                updateView(result)
            },
            { error ->
                Log.e(TAG, error.message.toString())
            }
        )
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

}