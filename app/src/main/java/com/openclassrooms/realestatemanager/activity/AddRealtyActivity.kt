package com.openclassrooms.realestatemanager.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.app.Dialog
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.ActivityAddRealtyBinding
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.Notifications
import com.openclassrooms.realestatemanager.viewmodel.AddRealtyViewModel
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import java.lang.StringBuilder


/**
 * Created by Julien Jennequin on 15/12/2021 15:24
 * Project : RealEstateManager
 **/
class AddRealtyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRealtyBinding
    private lateinit var addRealtyViewModel: AddRealtyViewModel

    private lateinit var realty: RealtyModel

    //region Date

    var cal = Calendar.getInstance()

    private val dateInSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInTextView(binding.addRealtyInDate)
        }

    private val dateOutSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInTextView(binding.addRealtyOutDate)
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
        private const val TAG = "AddRealtyActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRealtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realty = RealtyModel(0, "", 0, 0, 0, "", "", "", true, 0, 0, "", ByteArray(0))

        initUI()
        initViewModel()
        initListeners()
        initObservers()
    }

    private fun initUI() {
        if (!binding.addRealtyIsAvailable.isChecked) {
            binding.addRealtyOutDate.visibility = View.VISIBLE
        } else {
            binding.addRealtyOutDate.visibility = View.GONE
        }
    }

    private fun initViewModel() {
        val mViewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.addRealtyViewModel =
            ViewModelProvider(this, mViewModelFactory).get(AddRealtyViewModel::class.java)
    }

    private fun initListeners() {

        binding.addRealtyInDate.setOnClickListener {
            datePickerDialog(dateInSetListener)
        }

        binding.addRealtyOutDate.setOnClickListener {
            datePickerDialog(dateOutSetListener)
        }

        binding.interestPointLayout.setOnClickListener {
            showMultiCheckBoxesDialog()
        }

        binding.addRealtyIsAvailable.setOnClickListener {
            initUI()
        }

        binding.addRealtyValidateBtn.setOnClickListener {
            saveRealty()
        }

    }

    private fun initObservers() {

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

    private fun updateDateInTextView(realtyDateTextView: TextView) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        realtyDateTextView.text = sdf.format(cal.time)
    }

    private fun saveRealty() {
        if (verify()) {
            //TODO save realty in db
            addRealtyViewModel.insertRealty(realty).subscribe(
                {
                    //TODO NOTIFY USER TO SUCCESS OF TASK
                    Notifications().notifyUserInsertSuccess(this.applicationContext)
                    Log.d(TAG, "insert realty with success")
                },
                {
                    Log.d(TAG, "insert realty failed : ${it.stackTraceToString()}")
                }
            )
        }
    }

    private fun verify(): Boolean {

        if (binding.addRealtyKind.text.isNullOrEmpty()) {
            binding.addRealtyKind.error = "Missing required field"
            return false
        } else {
            realty.kind = binding.addRealtyKind.text.toString()
        }

        if (binding.addRealtyPrice.text.isNullOrEmpty()) {
            binding.addRealtyPrice.error = "Missing required field"
            return false
        } else {
            realty.price = binding.addRealtyPrice.text.toString().toLong()
        }

        if (binding.addRealtyAddress.text.isNullOrEmpty()) {
            binding.addRealtyAddress.error = "Missing required field"
            return false
        } else {
            realty.address = binding.addRealtyAddress.text.toString()
        }

        if (binding.addRealtyArea.text.isNullOrEmpty()) {
            binding.addRealtyArea.error = "Missing required field"
            return false
        } else {
            realty.area = binding.addRealtyArea.text.toString().toLong()
        }

        if (binding.addRealtyNbRoom.text.isNullOrEmpty()) {
            binding.addRealtyNbRoom.error = "Missing required field"
            return false
        } else {
            realty.roomNumber = binding.addRealtyNbRoom.text.toString().toInt()
        }

        if (binding.addRealtyAgent.text.isNullOrEmpty()) {
            binding.addRealtyAgent.error = "Missing required field"
            return false
        } else {
            realty.estateAgent = binding.addRealtyAgent.text.toString()
        }

        if (binding.addRealtyDescription.text.isNullOrEmpty()) {
            binding.addRealtyDescription.error = "Missing required field"
            return false
        } else {
            realty.description = binding.addRealtyDescription.text.toString()
        }

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
                binding.addRealtyInterestPoint.hint = realty.pointOfInterest
            } else {
                binding.addRealtyInterestPoint.hint = ""
            }
        }
        val dialog: Dialog = builder.create()
        dialog.show()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

}