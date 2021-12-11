package com.openclassrooms.realestatemanager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity.LEFT
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

/**
 * Created by Julien Jennequin on 02/12/2021 15:32
 * Project : RealEstateManager
 **/
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private var realtyList: List<RealtyModel> = emptyList()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // mainViewModel = ViewModelProvider(this).get(MainViewModel(AppDatabase.getInstance(this))::class.java)


        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        initUI()
        initViewModel()
        initListeners()
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)

        return true
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
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun initObservers() {
        mainViewModel.getAll().subscribe(
            {result ->
                Log.d(TAG,result.toString())
                realtyList = result
            },
            {error ->
                Log.e(TAG, error.message.toString())
            }
        )
    }
}