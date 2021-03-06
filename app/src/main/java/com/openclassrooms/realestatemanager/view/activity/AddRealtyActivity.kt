package com.openclassrooms.realestatemanager.view.activity

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
import com.openclassrooms.realestatemanager.view.adapter.PictureModelAdapter
import com.openclassrooms.realestatemanager.databinding.ActivityAddRealtyBinding
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.Notifications
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.plusAssign
import com.openclassrooms.realestatemanager.viewmodel.AddRealtyViewModel
import com.openclassrooms.realestatemanager.viewmodel.Injection
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import java.util.*


/**
 * Created by Julien Jennequin on 15/12/2021 15:24
 * Project : RealEstateManager
 **/
class AddRealtyActivity : BaseActivity() {

    //region PROPERTIES
    private lateinit var binding: ActivityAddRealtyBinding
    private lateinit var addRealtyViewModel: AddRealtyViewModel
    private lateinit var mAdapter: PictureModelAdapter
    private lateinit var realty: RealtyModel

    private var picturesList: MutableList<PicturesModel> = arrayListOf()
    //endregion

    //region Date
    private var cal: Calendar = Calendar.getInstance()

    private val dateInSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInTextView(binding.addRealtyInDate)
            realty.inMarketDate = cal.time.time
        }

    private val dateOutSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInTextView(binding.addRealtyOutDate)
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
        private const val TAG = "AddRealtyActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRealtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realty =
            RealtyModel(0, "", 0, 0, 0, 0, 0, "", "", "", "", "", "", 0.0, 0.0, "", true, 0, 0, "")

        initUI()
        initViewModel()
        initListeners()
        initRecyclerView()
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

        binding.addRealtyFromCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.addRealtyFromLibrary.setOnClickListener {
            getImageFromLibrary()
        }

        binding.addRealtyValidateBtn.setOnClickListener {
            saveRealty()
        }
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

    private fun updateDateInTextView(realtyDateTextView: TextView) {
        realtyDateTextView.text = Utils.getTodayDate(cal.time.time)
    }

    private fun saveRealty() {
        if (verify()) {
            disposeBag += addRealtyViewModel.insertRealty(realty, picturesList)
                .subscribe(
                    {
                        Log.d(TAG, "insert realty and pictures with success")
                        Notifications().notifyUserInsertSuccess(this.applicationContext, realty)
                    },
                    {
                        Log.d(TAG, "insert realty failed : ${it.stackTraceToString()}")
                    }
                )
        }
    }

    private fun verify(): Boolean {

        if (binding.addRealtyKind.text.isNullOrEmpty()) {
            binding.addRealtyKind.error = this.getString(R.string.add_field_required_error)
            return false
        } else {
            realty.kind = binding.addRealtyKind.text.toString().lowercase()
        }

        if (binding.addRealtyPrice.text.isNullOrEmpty()) {
            binding.addRealtyPrice.error = this.getString(R.string.add_field_required_error)
            return false
        } else {
            realty.price = binding.addRealtyPrice.text.toString().toLong()
        }

        if (binding.addRealtyAddress.text.isNullOrEmpty()) {
            binding.addRealtyAddress.error = this.getString(R.string.add_field_required_error)
            return false
        } else {
            realty.address = binding.addRealtyAddress.text.toString()
            realty = Utils.getLocationFromAddress(Geocoder(this), realty)
        }

        if (binding.addRealtyArea.text.isNullOrEmpty()) {
            binding.addRealtyArea.error = this.getString(R.string.add_field_required_error)
            return false
        } else {
            realty.area = binding.addRealtyArea.text.toString().toLong()
        }

        if (binding.addRealtyNbRoom.text.isNullOrEmpty()) {
            binding.addRealtyNbRoom.error = this.getString(R.string.add_field_required_error)
            return false
        } else {
            realty.roomNumber = binding.addRealtyNbRoom.text.toString().toInt()
        }

        if (binding.addRealtyBathRoom.text.isNullOrEmpty()) {
            binding.addRealtyBathRoom.error = this.getString(R.string.add_field_required_error)
            return false
        } else {
            realty.bathRoom = binding.addRealtyBathRoom.text.toString().toInt()
        }

        if (binding.addRealtyBedRoom.text.isNullOrEmpty()) {
            binding.addRealtyBedRoom.error = this.getString(R.string.add_field_required_error)
            return false
        } else {
            realty.bedRoom = binding.addRealtyBedRoom.text.toString().toInt()
        }

        if (binding.addRealtyAgent.text.isNullOrEmpty()) {
            binding.addRealtyAgent.error = this.getString(R.string.add_field_required_error)
            return false
        } else {
            realty.estateAgent = binding.addRealtyAgent.text.toString()
        }

        if (binding.addRealtyDescription.text.isNullOrEmpty()) {
            binding.addRealtyDescription.error = this.getString(R.string.add_field_required_error)
            return false
        } else {
            realty.description = binding.addRealtyDescription.text.toString()
        }

        if (binding.addRealtyInDate.text.isNullOrEmpty()) {
            binding.addRealtyInDate.error = this.getString(R.string.add_field_required_error)
        }

        if (!binding.addRealtyOutDate.text.isNullOrEmpty()) {
            if (binding.addRealtyInDate.text.isNullOrEmpty()) {
                binding.addRealtyInDate.error = this.getString(R.string.add_field_required_error)
                return false
            }
        }

        realty.available = binding.addRealtyIsAvailable.isChecked

        return true
    }

    private fun showMultiCheckBoxesDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val interestPoints = StringBuilder()

        builder.setMultiChoiceItems(
            resources.getStringArray(R.array.genres), isCheckedList
        ) { _, index, isChecked ->
            isCheckedList[index] = isChecked
        }
        builder.setPositiveButton(this.getString(R.string.add_dialog_positive_btn)) { dialog, _ ->
            dialog.dismiss()
            realty.pointOfInterest = ""

            for (i in resources.getStringArray(R.array.genres).indices) {
                if (isCheckedList[i]) {
                    realty.pointOfInterest =
                        interestPoints.append(resources.getStringArray(R.array.genres)[i])
                            .append(", ").toString()
                }
            }

            if (realty.pointOfInterest.isNotEmpty()) {
                realty.pointOfInterest = interestPoints.substring(0, interestPoints.length - 2)
                binding.addRealtyInterestPoint.setText(realty.pointOfInterest)
            } else {
                binding.addRealtyInterestPoint.hint = ""
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
        builder.setTitle(this.getString(R.string.add_dialog_title))

        val input = EditText(this)
        input.hint = this.getString(R.string.add_dialog_hint)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(this.getString(R.string.add_dialog_positive_btn)) { dialog, _ ->
            if (input.text.isEmpty()) {
                Toast.makeText(
                    this,
                    this.getString(R.string.add_dialog_error_msg),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                picture.name = input.text.toString()
                picturesList.add(picture)
                dialog.cancel()
            }
        }
        builder.setNegativeButton(this.getString(R.string.add_dialog_negative_btn)) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}