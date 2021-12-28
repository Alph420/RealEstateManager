package com.openclassrooms.realestatemanager.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.ActivityEditRealtyBinding
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.viewmodel.*
import java.lang.StringBuilder
import java.util.*

/**
 * Created by Julien Jennequin on 23/12/2021 14:40
 * Project : RealEstateManager
 **/
class EditRealtyActivity : BaseActivity() {

    //region PROPERTIES

    private lateinit var binding: ActivityEditRealtyBinding

    private lateinit var editRealtyViewModel: EditRealtyViewModel
    private lateinit var realty: RealtyModel

    private var realtyId = ""

    //endregion

    //region Date

    private var cal: Calendar = Calendar.getInstance()

    private val dateInSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInTextView(binding.editRealtyInDate)
            realty.inMarketDate = cal.time.time
        }

    private val dateOutSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInTextView(binding.editRealtyOutDate)
            realty.outMarketDate = cal.time.time

        }
    //endregion

    //region MultipleChoiceBoxData

    private val GENRES = arrayOf(
        "City Center",
        "Restaurants",
        "Metro/Train station",
        "SuperMarket",
        "Shcool",
        "Cinema",
        "Swimming pool",
        "Hospital",
        "Library",
        "Park",
        "Nightlife Street",
        "theater",
        "Bank",
        "Pharmacy"
    )
    private var isCheckedList = booleanArrayOf(
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
    //endregion

    companion object {
        private const val TAG = "EditRealtyActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditRealtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realtyId = intent.extras?.let {
            it.get(Constants().REALTY_ID_EXTRAS).toString()
        }.toString()

        initViewModel()
        initListeners()
        initObservers()
    }

    private fun initUI() {
        binding.editRealtyIsAvailable.isChecked = realty.available

        if (!binding.editRealtyIsAvailable.isChecked) {
            binding.editRealtyOutDate.visibility = View.VISIBLE
        } else {
            binding.editRealtyOutDate.visibility = View.GONE
        }
    }

    private fun initViewModel() {
        val mViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.editRealtyViewModel =
            ViewModelProvider(this, mViewModelFactory).get(EditRealtyViewModel::class.java)
    }

    private fun initListeners() {
        binding.editRealtyInDate.setOnClickListener {
            datePickerDialog(dateInSetListener)
        }

        binding.editRealtyOutDate.setOnClickListener {
            datePickerDialog(dateOutSetListener)
        }
        binding.editRealtyIsAvailable.setOnClickListener {
            realty.available = binding.editRealtyIsAvailable.isChecked
            initUI()
        }
        binding.editRealtyValidateBtn.setOnClickListener {
            saveRealty()
        }

        binding.interestPointLayout.setOnClickListener {
            showMultiCheckBoxesDialog()
        }

    }

    private fun initObservers() {
        disposeBag += editRealtyViewModel.getById(realtyId).subscribe(
            { result ->
                Log.d(TAG, result.toString())
                realty = result
                initUI()
                updateView(result)
            },
            { error ->
                Log.e(TAG, error.message.toString())
            }
        )
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

    private fun saveRealty() {
        if (verify()) {
            disposeBag += editRealtyViewModel.updateRealty(realty).subscribe(
                {
                    Log.d(TAG, "update realty with success")
                },
                {
                    Log.d(
                        TAG,
                        "update realty failed : ${it.stackTraceToString()}"
                    )
                }
            )
        }
    }

    private fun verify(): Boolean {

        if (binding.editRealtyKind.text.isNullOrEmpty()) {
            binding.editRealtyKind.error = "Missing required field"
            return false
        } else {
            realty.kind = binding.editRealtyKind.text.toString()
        }

        if (binding.editRealtyPrice.text.isNullOrEmpty()) {
            binding.editRealtyPrice.error = "Missing required field"
            return false
        } else {
            realty.price = binding.editRealtyPrice.text.toString().toLong()
        }

        if (binding.editRealtyAddress.text.isNullOrEmpty()) {
            binding.editRealtyAddress.error = "Missing required field"
            return false
        } else {
            realty.address = binding.editRealtyAddress.text.toString()
            realty.latitude = Utils.getLocationFromAddress(this, realty.address).latitude
            realty.longitude = Utils.getLocationFromAddress(this, realty.address).longitude
        }

        if (binding.editRealtyArea.text.isNullOrEmpty()) {
            binding.editRealtyArea.error = "Missing required field"
            return false
        } else {
            realty.area = binding.editRealtyArea.text.toString().toLong()
        }

        if (binding.editRealtyNbRoom.text.isNullOrEmpty()) {
            binding.editRealtyNbRoom.error = "Missing required field"
            return false
        } else {
            realty.roomNumber = binding.editRealtyNbRoom.text.toString().toInt()
        }

        if (binding.editRealtyBathRoom.text.isNullOrEmpty()) {
            binding.editRealtyBathRoom.error = "Missing required field"
            return false
        } else {
            realty.bathRoom = binding.editRealtyBathRoom.text.toString().toInt()
        }

        if (binding.editRealtyBedRoom.text.isNullOrEmpty()) {
            binding.editRealtyBedRoom.error = "Missing required field"
            return false
        } else {
            realty.bedRoom = binding.editRealtyBedRoom.text.toString().toInt()
        }

        if (binding.editRealtyAgent.text.isNullOrEmpty()) {
            binding.editRealtyAgent.error = "Missing required field"
            return false
        } else {
            realty.estateAgent = binding.editRealtyAgent.text.toString()
        }

        if (binding.editRealtyDescription.text.isNullOrEmpty()) {
            binding.editRealtyDescription.error = "Missing required field"
            return false
        } else {
            realty.description = binding.editRealtyDescription.text.toString()
        }

        if (!binding.editRealtyOutDate.text.isNullOrEmpty()) {
            if (binding.editRealtyInDate.text.isNullOrEmpty()) {
                binding.editRealtyInDate.error = "Missing required field"
                return false
            }
        }

        realty.available = binding.editRealtyIsAvailable.isChecked

        return true

    }

    private fun showMultiCheckBoxesDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMultiChoiceItems(
            GENRES, isCheckedList
        ) { _, index, isChecked ->
            isCheckedList[index] = isChecked
        }
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            val interestPoints = StringBuilder()
            realty.pointOfInterest = ""
            for (i in GENRES.indices) {
                if (isCheckedList[i]) {
                    realty.pointOfInterest =
                        interestPoints.append(GENRES[i]).append(", ").toString()

                }
            }

            if (realty.pointOfInterest.isNotEmpty()) {
                realty.pointOfInterest = interestPoints.substring(0, interestPoints.length - 2)
                binding.editRealtyInterestPoint.setText(realty.pointOfInterest)
            } else {
                binding.editRealtyInterestPoint.hint = ""
            }
        }
        val dialog: Dialog = builder.create()
        dialog.show()
    }

    private fun updateView(realty: RealtyModel) {
        binding.editRealtyKind.setText(realty.kind)
        binding.editRealtyPrice.setText(realty.price.toString())
        binding.editRealtyAddress.setText(realty.address)
        binding.editRealtyArea.setText(realty.area.toString())
        binding.editRealtyBathRoom.setText(realty.bathRoom.toString())
        binding.editRealtyNbRoom.setText(realty.roomNumber.toString())
        binding.editRealtyBedRoom.setText(realty.bedRoom.toString())
        binding.editRealtyInterestPoint.setText(realty.pointOfInterest)
        binding.editRealtyInDate.text = Utils.getTodayDate(realty.inMarketDate)
        binding.editRealtyOutDate.text = Utils.getTodayDate(realty.outMarketDate)
        binding.editRealtyAgent.setText(realty.estateAgent)
        binding.editRealtyDescription.setText(realty.description)
    }

    private fun updateDateInTextView(realtyDateTextView: TextView) {
        realtyDateTextView.text = Utils.getTodayDate(cal.time.time)
    }

}