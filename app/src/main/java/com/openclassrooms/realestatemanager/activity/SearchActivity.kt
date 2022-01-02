package com.openclassrooms.realestatemanager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.RealtyListAdapter
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.viewmodel.Injection
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

    private var realtyList: List<Realty> = emptyList()

    //endregion

    //region MultipleChoiceBoxData
    lateinit var kindAdapter: ArrayAdapter<String>
    private val kind = mutableListOf<String>()
    private var isCheckedList = mutableListOf<Boolean>()
    //endregion

    companion object {
        private const val TAG = "SearchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initViewModel()
        initListener()
        iniObserver()
        initRecyclerView()
    }

    private fun initViewModel() {
        val mViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.searchViewModel =
            ViewModelProvider(this, mViewModelFactory).get(SearchViewModel::class.java)
    }


    private fun initListener() {
        val bottomSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetParent)

        binding.bottomSheetParent.setOnClickListener {
            when (bottomSheetBehaviour.state) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.include.bottomFilterView.visibility = View.INVISIBLE
                    binding.fitlerUp.rotation = (90).toFloat()
                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.include.bottomFilterView.visibility = View.VISIBLE
                    binding.fitlerUp.rotation = (270).toFloat()

                }
            }
        }

        bottomSheetBehaviour.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset == 0.00000F) {
                    binding.include.bottomFilterView.visibility = View.INVISIBLE
                    binding.fitlerUp.rotation = (90).toFloat()
                } else {
                    binding.include.bottomFilterView.visibility = View.VISIBLE
                    binding.fitlerUp.rotation = (270).toFloat()
                }

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })

        binding.include.filterValidateSearch.setOnClickListener {
            bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            //TODO ADD POINT OF INTEREST
            var filter = FilterConstraint(
                binding.include.filterKind.selectedItem.toString(),
                binding.include.filterMinPrice.text.toString().toInt(),
                binding.include.filterMaxPrice.text.toString().toInt(),
                binding.include.filterMinArea.text.toString().toDouble(),
                binding.include.filterMaxArea.text.toString().toDouble(),
                binding.include.filterMinRoom.text.toString().toInt(),
                binding.include.filterMaxRoom.text.toString().toInt(),
                binding.include.filterMinBathroom.text.toString().toInt(),
                binding.include.filterMaxBathroom.text.toString().toInt(),
                binding.include.filterMinBedroom.text.toString().toInt(),
                binding.include.filterMaxBedroom.text.toString().toInt(),
                "",
                binding.include.filterIsAvailable.isChecked
            )
            filter(filter)

        }

        binding.include.filterReset.setOnClickListener {
            binding.include.filterKind.setSelection(0)
            binding.include.filterMinPrice.setText(0.toString())
            binding.include.filterMaxPrice.setText(0.toString())
            binding.include.filterMinArea.setText(0.toString())
            binding.include.filterMaxArea.setText(0.toString())
            binding.include.filterMinRoom.setText(0.toString())
            binding.include.filterMaxRoom.setText(0.toString())
            binding.include.filterMinBathroom.setText(0.toString())
            binding.include.filterMaxBathroom.setText(0.toString())
            binding.include.filterMinBedroom.setText(0.toString())
            binding.include.filterMaxBedroom.setText(0.toString())
            binding.include.filterCheckForAvailability.isChecked = false
            refreshFilteredList(realtyList.toMutableList())
        }
    }

    private fun iniObserver() {
        disposeBag += searchViewModel.getAllRealty().subscribe(
            { result ->
                Log.d(TAG, result.toString())
                realtyList = result
                updateView()
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

    private fun filter(filter: FilterConstraint) {
        val listForLoop = mutableListOf<Realty>()
        val listToModify = mutableListOf<Realty>()

        realtyList.forEach {
            if (filter.kind == "all") {
                listForLoop.add(it)
                listToModify.add(it)
            } else if (it.kind.lowercase() == filter.kind) {
                listForLoop.add(it)
                listToModify.add(it)
            }
        }

        if (filter.minPrice != 0) {
            listForLoop.forEach {
                if (it.price < filter.minPrice) listToModify.remove(it)
            }
        }

        if (filter.maxPrice != 0) {
            listForLoop.forEach {
                if (it.price > filter.maxPrice) listToModify.remove(it)
            }
        }

        if (filter.minArea != 0.0) {
            listForLoop.forEach {
                if (it.area < filter.minArea) listToModify.remove(it)
            }
        }
        if (filter.maxArea != 0.0) {
            listForLoop.forEach {
                if (it.area > filter.maxArea) listToModify.remove(it)
            }
        }
        if (filter.minRoom != 0) {
            listForLoop.forEach {
                if (it.roomNumber < filter.minRoom) listToModify.remove(it)
            }
        }
        if (filter.maxRoom != 0) {
            listForLoop.forEach {
                if (it.roomNumber > filter.maxRoom) listToModify.remove(it)
            }
        }
        if (filter.minBathroom != 0) {
            listForLoop.forEach {
                if (it.bathRoom < filter.minBathroom) listToModify.remove(it)
            }
        }
        if (filter.maxBathroom != 0) {
            listForLoop.forEach {
                if (it.bathRoom > filter.maxBathroom) listToModify.remove(it)
            }
        }
        if (filter.minBedroom != 0) {
            listForLoop.forEach {
                if (it.bedRoom < filter.minBedroom) listToModify.remove(it)
            }
        }
        if (filter.maxBedroom != 0) {
            listForLoop.forEach {
                if (it.bedRoom > filter.maxBedroom) listToModify.remove(it)
            }
        }

        if(binding.include.filterCheckForAvailability.isChecked){
            listForLoop.forEach {
                if (it.available != filter.available) listToModify.remove(it)
            }
        }

        refreshFilteredList(listToModify)
    }

    private fun refreshFilteredList(filteredResult: MutableList<Realty>) {
        adapter.dataList = filteredResult
        adapter.notifyDataSetChanged()
    }

    private fun updateView() {
        adapter.dataList = realtyList
        adapter.notifyDataSetChanged()
        setFilterData()
    }

    private fun setFilterData() {
        kind.add("all")
        realtyList.forEach {
            kind.add(it.kind.lowercase())
            isCheckedList.add(false)
        }
        val distinct = kind.toSet().toList()

        this.kindAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, distinct)
        binding.include.filterKind.adapter = kindAdapter
        kindAdapter.notifyDataSetChanged()
    }


    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}