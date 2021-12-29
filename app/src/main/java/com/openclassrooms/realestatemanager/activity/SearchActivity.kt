package com.openclassrooms.realestatemanager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.RealtyListAdapter
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import com.openclassrooms.realestatemanager.databinding.FragmentBottomSheetBinding
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.viewmodel.SearchViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory

/**
 * Created by Julien Jennequin on 29/12/2021 17:27
 * Project : RealEstateManager
 **/
class SearchActivity : BaseActivity() {

    //region PROPERTIES
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RealtyListAdapter
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var bottomSheetBinding:FragmentBottomSheetBinding

    private var realtyList: List<Realty> = emptyList()

    var i = 0
    //endregion


    companion object {
        private const val TAG = "SearchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        bottomSheetBinding = FragmentBottomSheetBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        initViewModel()
        initListener()
        iniObserver()
        initRecyclerView()
        initFilterSheet()
    }

    private fun initViewModel() {
        val mViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.searchViewModel =
            ViewModelProvider(this, mViewModelFactory).get(SearchViewModel::class.java)
    }


    fun initListener() {
        val bottomSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetParent)


        binding.fitlerUp.setOnClickListener {
            when (bottomSheetBehaviour.state) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.fitlerUp.rotation = (90).toFloat()
                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.fitlerUp.rotation = (270).toFloat()

                }
            }
        }


        bottomSheetBehaviour.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d(TAG, "slideOffset onSlide $slideOffset")

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })

        bottomSheetBinding.compteur.setOnClickListener{
            binding.textCompteur.text = i++.toString()
            Log.d(TAG, "i : $i")

        }
    }

    fun iniObserver() {
        disposeBag += searchViewModel.getAll().subscribe(
            { result ->
                Log.d(TAG, result.toString())
                realtyList = result
                updateView()
                realtyList.forEach { realty ->
                    disposeBag += searchViewModel.getPictures(realty.id).subscribe(
                        { result ->
                            realty.pictures = result
                        },
                        { error ->
                            Log.e(TAG, error.message.toString())
                        }
                    )
                }
            },
            { error ->
                Log.e(TAG, error.message.toString())
            }
        )
    }

    private fun initRecyclerView() {
        this.adapter = RealtyListAdapter(realtyList)

        binding.realtyRecyclerView.adapter = this.adapter
        binding.realtyRecyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.realtyRecyclerView.context,
            (binding.realtyRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        binding.realtyRecyclerView.addItemDecoration(dividerItemDecoration)

        adapter.setListener(object : RealtyListAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(binding.root.context, DetailRealtyActivity::class.java)
                intent.putExtra(Constants().REALTY_ID_EXTRAS, (realtyList[position].id))
                startActivity(intent)
            }
        })
    }

    private fun updateView() {
        adapter.dataList = realtyList
        adapter.notifyDataSetChanged()
    }

    fun initFilterSheet(){



    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

}