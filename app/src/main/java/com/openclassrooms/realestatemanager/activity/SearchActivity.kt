package com.openclassrooms.realestatemanager.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.openclassrooms.realestatemanager.adapter.RealtyListAdapter
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.SearchViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import android.app.Dialog
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import com.openclassrooms.realestatemanager.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

/**
 * Created by Julien Jennequin on 29/12/2021 17:27
 * Project : RealEstateManager
 **/
class SearchActivity : BaseActivity() {

    //region PROPERTIES
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RealtyListAdapter
    private lateinit var searchViewModel: SearchViewModel

    private var interestPoints = StringBuilder()
    private var interestPointsList = emptyList<String>()
    private var realtyList: List<Realty> = emptyList()
    //endregion

    //region spinnerKindChoice
    private lateinit var kindAdapter: ArrayAdapter<String>
    private val kind = mutableListOf<String>()
    private var isCheckedList = mutableListOf<Boolean>()
    //endregion

    //region Date
    private var cal: Calendar = Calendar.getInstance()

    private val dateInSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInTextView(binding.include.filterInDate)
        }

    private val dateOutSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInTextView(binding.include.filterOutDate)
        }
    //endregion

    //region MultipleChoiceBoxData

    //endregion

    companion object {
        private const val TAG = "SearchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initUI()
        initViewModel()
        initListener()
        iniObserver()
        initRecyclerView()
    }

    private fun initUI() {
        binding.include.filterPriceRange.isNotifyWhileDragging = true
    }

    private fun initViewModel() {
        val mViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.searchViewModel =
            ViewModelProvider(this, mViewModelFactory).get(SearchViewModel::class.java)
    }

    private fun initListener() {
        val bottomSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetParent)

        binding.include.filterUp.setOnClickListener {
            when (bottomSheetBehaviour.state) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.include.bottomFilterView.visibility = View.INVISIBLE
                    binding.include.filterUp.rotation = (90).toFloat()
                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.include.bottomFilterView.visibility = View.VISIBLE
                    binding.include.filterUp.rotation = (270).toFloat()

                }
            }
        }

        bottomSheetBehaviour.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset == 0.00000F) {
                    binding.include.bottomFilterView.visibility = View.INVISIBLE
                    binding.include.filterUp.rotation = (90).toFloat()
                } else {
                    binding.include.bottomFilterView.visibility = View.VISIBLE
                    binding.include.filterUp.rotation = (270).toFloat()
                }

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })

        binding.include.filterValidateSearch.setOnClickListener {
            if (verify()) {
                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                //TODO ADD PICTURE, LOCATION FILTER
                val filter = FilterConstraint(
                    binding.include.filterKind.selectedItem.toString(),
                    binding.include.filterPriceRange.selectedMinValue.toInt(),
                    binding.include.filterPriceRange.selectedMaxValue.toInt(),
                    binding.include.filterAreaRange.selectedMinValue.toDouble(),
                    binding.include.filterAreaRange.selectedMaxValue.toDouble(),
                    binding.include.filterRoomRange.selectedMinValue.toInt(),
                    binding.include.filterRoomRange.selectedMaxValue.toInt(),
                    binding.include.filterBathroomRange.selectedMinValue.toInt(),
                    binding.include.filterBathroomRange.selectedMaxValue.toInt(),
                    binding.include.filterBedroomRange.selectedMinValue.toInt(),
                    binding.include.filterBedroomRange.selectedMaxValue.toInt(),
                    interestPointsList,
                    binding.include.filterIsAvailable.isChecked,
                    Utils.getDateFromString(binding.include.filterInDate.text.toString()),
                    Utils.getDateFromString(binding.include.filterOutDate.text.toString())
                )
                filter(filter)
            }
        }

        binding.include.filterReset.setOnClickListener {
            resetField()
        }

        binding.include.filterNearPlace.setOnClickListener {
            binding.include.filterNearPlace.text = ""
            interestPoints.clear()
            showNearPlaceChoice()
        }

        binding.include.filterInDate.setOnClickListener {
            datePickerDialog(dateInSetListener)
        }

        binding.include.filterOutDate.setOnClickListener {
            datePickerDialog(dateOutSetListener)
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

    private fun datePickerDialog(dateInSetListener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(
            this,
            dateInSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun verify(): Boolean {
        /*  if (binding.include.filterInDate.text.isNullOrEmpty()) {
              binding.include.filterInDate.error = "Missing field put 0 for no filter"
              return false
          } else {
              binding.include.filterInDate.error = null
          }

          if (binding.include.filterOutDate.text.isNullOrEmpty()) {
              binding.include.filterOutDate.error = "Missing field put 0 for no filter"
              return false
          } else {
              binding.include.filterOutDate.error = null
          }*/

        return true
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

        if (filter.pointOfInterest.isNotEmpty()) {
            listForLoop.forEach {
                if (!it.pointOfInterest.containsAll(filter.pointOfInterest)) listToModify.remove(it)
            }
        }

        if (binding.include.filterCheckForAvailability.isChecked) {
            listForLoop.forEach {
                if (it.available != filter.available) listToModify.remove(it)
            }
        }

        if (filter.inMarketDate != 0L) {
            listForLoop.forEach {
                if (it.inMarketDate < filter.inMarketDate) listToModify.remove(it)
            }
        }

        if (filter.outMarketDate != 0L) {
            listForLoop.forEach {
                if (it.outMarketDate > filter.outMarketDate) listToModify.remove(it)
            }
        }

        refreshFilteredList(listToModify)
    }

    private fun resetField() {
        binding.include.filterKind.setSelection(0)
        binding.include.filterPriceRange.selectedMinValue = 0
        binding.include.filterPriceRange.selectedMaxValue = 10000000
        binding.include.filterCheckForAvailability.isChecked = false
        binding.include.filterNearPlace.text = ""
        refreshFilteredList(realtyList.toMutableList())
        binding.include.filterInDate.text = ""
        binding.include.filterOutDate.text = ""
    }

    private fun refreshFilteredList(filteredResult: MutableList<Realty>) {
        if (filteredResult.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.emptyView.visibility = View.INVISIBLE
        }
        adapter.dataList = filteredResult
        adapter.notifyDataSetChanged()
    }

    private fun updateView() {
        adapter.dataList = realtyList
        adapter.notifyDataSetChanged()
        setFilterData()
    }

    private fun updateDateInTextView(realtyDateTextView: TextView) {
        realtyDateTextView.text = Utils.getTodayDate(cal.time.time)
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

    private fun showNearPlaceChoice() {
        val genreCheckedList = booleanArrayOf(
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false
        )

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMultiChoiceItems(
            resources.getStringArray(R.array.genres),
            genreCheckedList
        ) { _, index, isChecked ->
            genreCheckedList[index] = isChecked
        }

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            for (i in resources.getStringArray(R.array.genres).indices) {
                if (genreCheckedList[i]) {
                    interestPoints.append(resources.getStringArray(R.array.genres)[i]).append(", ")
                        .toString()
                }
            }

            if (interestPoints.isNotEmpty()) {
                interestPointsList =
                    interestPoints.toString().substring(0, interestPoints.length - 2).split(", ")
                binding.include.filterNearPlace.text =
                    interestPoints.substring(0, interestPoints.length - 2)
            } else {
                binding.include.filterNearPlace.hint = ""
            }
        }
        val dialog: Dialog = builder.create()
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}