package com.openclassrooms.realestatemanager.view.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.repository.RealtyRepository
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.view.adapter.RealtyListAdapter
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.SearchViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by Julien Jennequin on 29/12/2021 17:27
 * Project : RealEstateManager
 **/
class SearchActivity : BaseActivity() {

    //region PROPERTIES
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RealtyListAdapter
    private lateinit var kindAdapter: ArrayAdapter<String>
    private lateinit var cityAdapter: ArrayAdapter<String>
    private lateinit var searchViewModel: SearchViewModel

    lateinit var realtyRepository: RealtyRepository

    private var interestPoints = StringBuilder()

    private var interestPointsList = emptyList<String>()
    private var realtyList = emptyList<Realty>()

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
            bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED

            disposeBag += searchViewModel.getFilteredRealty(
                FilterConstraint(
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
                    binding.include.filterPicturesRange.selectedMaxValue.toInt(),
                    binding.include.filterCheckForPrice.isChecked,
                    binding.include.filterCheckForArea.isChecked,
                    binding.include.filterCheckForRoom.isChecked,
                    binding.include.filterCheckForBathroom.isChecked,
                    binding.include.filterCheckForBedroom.isChecked,
                    binding.include.checkFilterForPictures.isChecked,
                    binding.include.filterCheckForAvailability.isChecked,
                    binding.include.filterInDate.text.isNotEmpty(),
                    binding.include.filterOutDate.text.isNotEmpty()
                ),
                this.getString(R.string.search_all_kind),
                this.getString(R.string.search_all_city)
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "filter two success + $it")
                    realtyList = it.toMutableList()
                    refreshFilteredList(it.toMutableList())
                }, { error ->
                    Log.d(TAG, error.stackTraceToString())
                })
        }

        binding.include.filterReset.setOnClickListener {
            resetAllFieldData()
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
        disposeBag += searchViewModel.getAllRealty()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
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

    private fun resetAllFieldData() {
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
        binding.include.filterIsAvailable.isChecked = false

        binding.include.filterNearPlace.text = ""
        binding.include.filterInDate.text = ""
        binding.include.filterOutDate.text = ""

        interestPointsList = emptyList()

        disposeBag += searchViewModel.getAllRealty()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d(TAG, result.toString())
                    refreshFilteredList(result)
                },
                { error ->
                    Log.e(TAG, error.message.toString())
                }
            )
    }

    private fun refreshFilteredList(filteredResult: List<Realty>) {
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
        kind.add(this.getString(R.string.search_all_kind))
        city.add(this.getString(R.string.search_all_city))

        //Add kind of realty in adapter list
        realtyList.forEach {
            kind.add(it.kind)
            kindCheckedList.add(false)
        }

        //Add city of realty in adapter list
        realtyList.forEach {
            if (it.city.isNotEmpty()) {
                city.add(it.city)
                cityCheckedList.add(false)
            }
        }

        this.kindAdapter =
            ArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                kind.toSet().toList()
            )

        this.cityAdapter =
            ArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                city.toSet().toList()
            )

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

        builder.setPositiveButton(this.getString(R.string.dialog_positive_btn)) { dialog, _ ->
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