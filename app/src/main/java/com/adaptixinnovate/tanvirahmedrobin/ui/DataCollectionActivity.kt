package com.adaptixinnovate.tanvirahmedrobin.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityDataCollectionBinding
import com.adaptixinnovate.tanvirahmedrobin.model.LocationModel
import com.adaptixinnovate.tanvirahmedrobin.services.GetData
import com.adaptixinnovate.tanvirahmedrobin.services.SendData
import java.util.Calendar

class DataCollectionActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDataCollectionBinding
    private var dateOfBirth = ""
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInputs()
        setupGenderDropdown()
        setupDate()
        // Setup the toolbar
        setSupportActionBar(binding.customToolbar)
        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.dataSubmitButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val fatherName = binding.fatherNameInput.text.toString().trim()
            val motherName = binding.motherNameInput.text.toString().trim()
            val nid = binding.nidInput.text.toString().trim()
            val address = binding.addressInput.text.toString().trim()
            val wardName = binding.wardNameInput.text.toString().trim()
            val thanaName = binding.thanaNameInput.text.toString().trim()
            val mobile = binding.mobileInput.text.toString().trim()
            val gender = binding.genderNameInput.text.toString().trim()
            dateOfBirth = getSelectedDate()

            when {
                name.isEmpty() -> {
                    binding.nameInput.error = "Name cannot be empty"
                    return@setOnClickListener
                }
                fatherName.isEmpty() -> {
                    binding.fatherNameInput.error = "Father's name cannot be empty"
                    return@setOnClickListener
                }
                motherName.isEmpty() -> {
                    binding.motherNameInput.error = "Mother's name cannot be empty"
                    return@setOnClickListener
                }
                nid.isEmpty() -> {
                    binding.nidInput.error = "NID cannot be empty"
                    return@setOnClickListener
                }
                dateOfBirth.isEmpty() -> {
                    Toast.makeText(this, "Date of Birth cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                address.isEmpty() -> {
                    binding.addressInput.error = "Address cannot be empty"
                    return@setOnClickListener
                }
                gender.isEmpty()->{
                    binding.genderInputLayout.error = "Select a gender to continue"
                    return@setOnClickListener
                }
                wardName.isEmpty() -> {
                    binding.wardNameInput.error = "Ward name cannot be empty"
                    return@setOnClickListener
                }
                thanaName.isEmpty() -> {
                    binding.thanaNameInput.error = "Thana name cannot be empty"
                    return@setOnClickListener
                }
                mobile.isEmpty() || !mobile.matches("\\d{10}".toRegex()) -> {
                    binding.mobileInput.error = "Enter a valid 10-digit mobile number"
                    return@setOnClickListener
                }
                else -> {
                    SendData.dataCollection(name, fatherName, motherName, nid, dateOfBirth, address, wardName, thanaName, mobile, gender, this, binding.progressBar)
                }
            }
        }

    }

    private fun setupGenderDropdown() {
        val genderOptions = arrayOf("Male", "Female", "Other")
        autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.genderNameInput)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderOptions)
        autoCompleteTextView.setAdapter(adapter)  // Correctly set the adapter

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedGender = parent.adapter.getItem(position) as String
            Toast.makeText(this, "Selected Gender: $selectedGender", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupInputs() {
        GetData.fetchWards(this, binding.wardNameInput, binding.thanaNameInput)

    }

    private fun setupDate() {
        val days = (1..31).toList()
        val months = (1..12).toList()

        binding.dobCardView.daySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)
        binding.dobCardView.monthSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)

        setupYearSpinner()
    }

    private fun setupYearSpinner(){
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val startYear = currentYear - 90
        val endYear = currentYear

        val years = (startYear..endYear).toList()

        binding.dobCardView.yearSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)

    }


    private fun getSelectedDate(): String {
        val selectedDay = binding.dobCardView.daySpinner.selectedItem.toString()
        val selectedMonth = binding.dobCardView.monthSpinner.selectedItem.toString()
        val selectedYear = binding.dobCardView.yearSpinner.selectedItem.toString()

        return "$selectedDay/$selectedMonth/$selectedYear"
    }




    override fun onSupportNavigateUp(): Boolean {
        // This method is called when the up button is pressed. Just finish the activity
        finish()
        return true
    }

}