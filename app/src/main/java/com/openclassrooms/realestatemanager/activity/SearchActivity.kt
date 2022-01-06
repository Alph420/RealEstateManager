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
import java.util.*
import kotlin.concurrent.thread

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
    private lateinit var cityAdapter: ArrayAdapter<String>
    private val kind = mutableListOf<String>()
    private val city = mutableListOf<String>()
    private var kindCheckedList = mutableListOf<Boolean>()
    private var cityCheckedList = mutableListOf<Boolean>()

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
                val filter = FilterConstraint(
                    binding.include.filterKind.selectedItem.toString(),
                    binding.include.filterCity.selectedItem.toString(),
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
                    Utils.getDateFromString(binding.include.filterOutDate.text.toString()),
                    binding.include.filterPicturesRange.selectedMinValue.toInt(),
                    binding.include.filterPicturesRange.selectedMaxValue.toInt()
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

        listForLoop.forEach { realty ->
            if (filter.city != "all") {
                if (filter.city.lowercase() != realty.city.lowercase()) listToModify.remove(realty)
            }
            if (binding.include.filterCheckForPrice.isChecked) {
                if (realty.price < filter.minPrice) listToModify.remove(realty)
                if (realty.price > filter.maxPrice) listToModify.remove(realty)
            }
            if (binding.include.filterCheckForArea.isChecked) {
                if (realty.area < filter.minArea) listToModify.remove(realty)
                if (realty.area > filter.maxArea) listToModify.remove(realty)
            }
            if (binding.include.filterCheckForRoom.isChecked) {
                if (realty.roomNumber < filter.minRoom) listToModify.remove(realty)
                if (realty.roomNumber > filter.maxRoom) listToModify.remove(realty)
            }
            if (binding.include.filterCheckForBathroom.isChecked) {
                if (realty.bathRoom < filter.minBathroom) listToModify.remove(realty)
                if (realty.bathRoom > filter.maxBathroom) listToModify.remove(realty)
            }
            if (binding.include.filterCheckForBedroom.isChecked) {
                if (realty.bedRoom < filter.minBedroom) listToModify.remove(realty)
                if (realty.bedRoom > filter.maxBedroom) listToModify.remove(realty)
            }
            if (binding.include.checkFilterForPictures.isChecked) {
                if (realty.pictures.size < filter.minPictures) listToModify.remove(realty)
                if (realty.pictures.size > filter.maxPictures) listToModify.remove(realty)
            }

            if (!realty.pointOfInterest.containsAll(filter.pointOfInterest)) listToModify.remove(
                realty
            )
            if (binding.include.filterCheckForAvailability.isChecked) {
                if (realty.available != filter.available) listToModify.remove(realty)
            }

            if (binding.include.filterInDate.text.isNotEmpty()) {
                if (realty.inMarketDate < filter.inMarketDate) listToModify.remove(realty)
            }
            if (binding.include.filterOutDate.text.isNotEmpty()) {
                if (realty.outMarketDate > filter.outMarketDate) listToModify.remove(realty)
            }
        }

        refreshFilteredList(listToModify)
    }

    private fun resetField() {
        binding.include.filterKind.setSelection(0)
        binding.include.filterCity.setSelection(0)

        binding.include.filterPriceRange.selectedMinValue =
            binding.include.filterPriceRange.absoluteMinValue
        binding.include.filterPriceRange.selectedMaxValue =
            binding.include.filterPriceRange.absoluteMaxValue
        binding.include.filterCheckForPrice.isChecked = false

        binding.include.filterAreaRange.selectedMinValue =
            binding.include.filterAreaRange.absoluteMinValue
        binding.include.filterAreaRange.selectedMaxValue =
            binding.include.filterAreaRange.absoluteMaxValue
        binding.include.filterCheckForArea.isChecked = false

        binding.include.filterRoomRange.selectedMinValue =
            binding.include.filterRoomRange.absoluteMinValue
        binding.include.filterRoomRange.selectedMaxValue =
            binding.include.filterRoomRange.absoluteMaxValue
        binding.include.filterCheckForRoom.isChecked = false

        binding.include.filterBathroomRange.selectedMinValue =
            binding.include.filterBathroomRange.absoluteMinValue
        binding.include.filterBathroomRange.selectedMaxValue =
            binding.include.filterBathroomRange.absoluteMaxValue
        binding.include.filterCheckForBathroom.isChecked = false

        binding.include.filterBedroomRange.selectedMinValue =
            binding.include.filterBedroomRange.absoluteMinValue
        binding.include.filterBedroomRange.selectedMaxValue =
            binding.include.filterBedroomRange.absoluteMaxValue
        binding.include.filterCheckForBedroom.isChecked = false

        binding.include.filterPicturesRange.selectedMinValue =
            binding.include.filterPicturesRange.absoluteMinValue
        binding.include.filterPicturesRange.selectedMaxValue =
            binding.include.filterPicturesRange.absoluteMaxValue
        binding.include.filterCheckForBathroom.isChecked = false


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
        city.add("all")

        thread {
            realtyList.forEach {
                kind.add(it.kind.lowercase())
                kindCheckedList.add(false)
            }

            realtyList.forEach {
                if (it.city.isNotEmpty()) {
                    city.add(it.city.lowercase())
                    cityCheckedList.add(false)
                }
            }
        }

        val kindDistinct = kind.toSet().toList()
        val cityDistinct = city.toSet().toList()

        this.kindAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, kindDistinct)

        this.cityAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, cityDistinct)

        binding.include.filterKind.adapter = kindAdapter
        binding.include.filterCity.adapter = cityAdapter

        kindAdapter.notifyDataSetChanged()
        cityAdapter.notifyDataSetChanged()
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