package com.openclassrooms.realestatemanager.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PictureModelAdapter
import com.openclassrooms.realestatemanager.databinding.ActivityEditRealtyBinding
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.viewmodel.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.lang.StringBuilder
import java.util.*

/**
 * Created by Julien Jennequin on 23/12/2021 14:40
 * Project : RealEstateManager
 **/
class EditRealtyActivity : BaseActivity() {

    //region PROPERTIES
    private lateinit var binding: ActivityEditRealtyBinding
    private lateinit var mAdapter: PictureModelAdapter
    private lateinit var editRealtyViewModel: EditRealtyViewModel
    private lateinit var realty: Realty

    private var realtyId = 0
    private var picturesList: MutableList<PicturesModel> = arrayListOf()
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
            it.getInt(Constants().REALTY_ID_EXTRAS)
        } ?: 0

        initViewModel()
        initListeners()
        initObservers()
        initRecyclerView()
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

        binding.editRealtyFromCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.editRealtyFromLibrary.setOnClickListener {
            getImageFromLibrary()
        }

    }

    private fun initObservers() {
        disposeBag += editRealtyViewModel.getRealtyById(realtyId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d(TAG, result.toString())
                    realty = result
                    initUI()
                    updateView()
                },
                { error ->
                    Log.e(TAG, error.message.toString())
                }
            )
    }

    private fun initRecyclerView() {
        this.mAdapter = PictureModelAdapter(picturesList)

        binding.recyclerView.adapter = this.mAdapter
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
            disposeBag += editRealtyViewModel.updateRealty(realty, picturesList).subscribe(
                {
                    Log.d(TAG, "update realty and insert picture with success")
                    picturesList.clear()
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
            binding.editRealtyKind.error = this.getString(R.string.edit_field_required_error)
            return false
        } else {
            realty.kind = binding.editRealtyKind.text.toString()
        }

        if (binding.editRealtyPrice.text.isNullOrEmpty()) {
            binding.editRealtyPrice.error = this.getString(R.string.edit_field_required_error)
            return false
        } else {
            realty.price = binding.editRealtyPrice.text.toString().toLong()
        }

        if (binding.editRealtyAddress.text.isNullOrEmpty()) {
            binding.editRealtyAddress.error = this.getString(R.string.edit_field_required_error)
            return false
        } else {
            realty.address = binding.editRealtyAddress.text.toString()
            realty = Utils.getLocationFromAddress(Geocoder(this), realty)
        }

        if (binding.editRealtyArea.text.isNullOrEmpty()) {
            binding.editRealtyArea.error = this.getString(R.string.edit_field_required_error)
            return false
        } else {
            realty.area = binding.editRealtyArea.text.toString().toLong()
        }

        if (binding.editRealtyNbRoom.text.isNullOrEmpty()) {
            binding.editRealtyNbRoom.error = this.getString(R.string.edit_field_required_error)
            return false
        } else {
            realty.roomNumber = binding.editRealtyNbRoom.text.toString().toInt()
        }

        if (binding.editRealtyBathRoom.text.isNullOrEmpty()) {
            binding.editRealtyBathRoom.error = this.getString(R.string.edit_field_required_error)
            return false
        } else {
            realty.bathRoom = binding.editRealtyBathRoom.text.toString().toInt()
        }

        if (binding.editRealtyBedRoom.text.isNullOrEmpty()) {
            binding.editRealtyBedRoom.error = this.getString(R.string.edit_field_required_error)
            return false
        } else {
            realty.bedRoom = binding.editRealtyBedRoom.text.toString().toInt()
        }

        if (binding.editRealtyAgent.text.isNullOrEmpty()) {
            binding.editRealtyAgent.error = this.getString(R.string.edit_field_required_error)
            return false
        } else {
            realty.estateAgent = binding.editRealtyAgent.text.toString()
        }

        if (binding.editRealtyDescription.text.isNullOrEmpty()) {
            binding.editRealtyDescription.error = this.getString(R.string.edit_field_required_error)
            return false
        } else {
            realty.description = binding.editRealtyDescription.text.toString()
        }

        if (!binding.editRealtyOutDate.text.isNullOrEmpty()) {
            if (binding.editRealtyInDate.text.isNullOrEmpty()) {
                binding.editRealtyInDate.error = this.getString(R.string.edit_field_required_error)
                return false
            }
        }

        realty.available = binding.editRealtyIsAvailable.isChecked

        return true

    }

    private fun showMultiCheckBoxesDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMultiChoiceItems(
            resources.getStringArray(R.array.genres), isCheckedList
        ) { _, index, isChecked ->
            isCheckedList[index] = isChecked
        }
        builder.setPositiveButton(this.getString(R.string.edit_dialog_positive_btn)) { dialog, _ ->
            dialog.dismiss()
            val interestPoints = StringBuilder()
            realty.pointOfInterest = emptyList()
            for (i in resources.getStringArray(R.array.genres).indices) {
                if (isCheckedList[i]) {
                    realty.pointOfInterest =
                        interestPoints.append(resources.getStringArray(R.array.genres)[i])
                            .append(", ").toString().replace("[", "").replace("]", "").trim()
                            .split(", ")
                }
            }

            if (realty.pointOfInterest.isNotEmpty()) {
                realty.pointOfInterest =
                    interestPoints.substring(0, interestPoints.length - 2).split(", ")
                binding.editRealtyInterestPoint.setText(
                    realty.pointOfInterest.toString().replace("[", "").replace("]", "").trim()
                )
            } else {
                binding.editRealtyInterestPoint.hint = ""
            }
        }
        val dialog: Dialog = builder.create()
        dialog.show()
    }

    private fun dispatchTakePictureIntent() {
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        getImgFromCameraActivityForResult.launch(callCameraIntent)
    }

    private fun getImageFromLibrary() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        getImgFromLibraryActivityForResult.launch(photoPickerIntent)
    }

    private val getImgFromCameraActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { ActivityResult ->
        if (ActivityResult.resultCode == Activity.RESULT_OK) {
            ActivityResult.data?.let { intent ->
                intent.extras?.let {
                    val photo = it.get("data") as Bitmap
                    val uriOfPhoto = Utils.getImageUri(this, photo)
                    popup(uriOfPhoto)
                }
            }
        }
    }

    private val getImgFromLibraryActivityForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { ActivityResult ->
            if (ActivityResult.resultCode == Activity.RESULT_OK) {
                ActivityResult.data?.let { intent ->
                    intent.data?.let {
                        popup(it)
                    }
                }
            }
        }

    private fun popup(picturePath: Uri) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val picture = PicturesModel(0, 0, "", picturePath.toString())
        builder.setTitle(this.getString(R.string.edit_dialog_title))

        val input = EditText(this)
        input.hint = this.getString(R.string.edit_dialog_hint)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton(this.getString(R.string.edit_dialog_positive_btn)) { dialog, _ ->
            if (input.text.isEmpty()) {
                Toast.makeText(
                    this,
                    this.getString(R.string.edit_dialog_error_msg),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                picture.name = input.text.toString()
                picturesList.add(picture)
                updateRecyclerView()
                dialog.cancel()
            }
        }
        builder.setNegativeButton(this.getString(R.string.edit_dialog_negative_btn)) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun updateView() {
        binding.editRealtyKind.setText(realty.kind)
        binding.editRealtyPrice.setText(realty.price.toString())
        binding.editRealtyAddress.setText(realty.address)
        binding.editRealtyArea.setText(realty.area.toString())
        binding.editRealtyBathRoom.setText(realty.bathRoom.toString())
        binding.editRealtyNbRoom.setText(realty.roomNumber.toString())
        binding.editRealtyBedRoom.setText(realty.bedRoom.toString())
        binding.editRealtyInterestPoint.setText(
            realty.pointOfInterest.toString().replace("[", "").replace("]", "").trim()
        )
        binding.editRealtyInDate.text = Utils.getTodayDate(realty.inMarketDate)
        binding.editRealtyOutDate.text = Utils.getTodayDate(realty.outMarketDate)
        binding.editRealtyAgent.setText(realty.estateAgent)
        binding.editRealtyDescription.setText(realty.description)
        updatePictures()
    }

    private fun updatePictures() {
        mAdapter.dataList = realty.pictures
        mAdapter.notifyDataSetChanged()
    }

    private fun updateRecyclerView() {
        mAdapter.dataList = realty.pictures + picturesList
        mAdapter.notifyDataSetChanged()
    }

    private fun updateDateInTextView(realtyDateTextView: TextView) {
        realtyDateTextView.text = Utils.getTodayDate(cal.time.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

}