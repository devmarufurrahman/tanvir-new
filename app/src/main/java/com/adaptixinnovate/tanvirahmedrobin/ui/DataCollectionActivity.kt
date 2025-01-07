package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
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
import com.adaptixinnovate.tanvirahmedrobin.network.retrofit.RetrofitClient
import com.adaptixinnovate.tanvirahmedrobin.services.SendData
import com.adaptixinnovate.tanvirahmedrobin.utils.SetupDropDown
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class DataCollectionActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDataCollectionBinding
    private var dateOfBirth = ""
    var selectedWardId: String? = null
    var selectedThanaId: String? = null
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWards(this, binding.wardNameInput, binding.thanaNameInput)
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
            val mobile = binding.mobileInput.text.toString().trim()
            val gender = binding.genderNameInput.text.toString().trim()

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
                    dateOfBirth = getSelectedDate().toString()
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
                selectedWardId.isNullOrBlank()-> {
                    binding.wardNameInput.error = "Ward name cannot be empty"
                    return@setOnClickListener
                }
                selectedThanaId.isNullOrBlank()-> {
                    binding.thanaNameInput.error = "Thana name cannot be empty"
                    return@setOnClickListener
                }
                mobile.isEmpty() || !mobile.matches("\\d{11}".toRegex()) -> {
                    binding.mobileInput.error = "Enter a valid 11-digit mobile number"
                    return@setOnClickListener
                } else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    SendData.dataCollection(name, fatherName, motherName, nid, dateOfBirth, mobile, gender, address, selectedWardId!!, selectedThanaId!!, this, binding.progressBar)
                }
            }
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            recreate()
            // Code to refresh the content goes here

            binding.swipeRefreshLayout.isRefreshing = false
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

    private fun fetchWards(context: Context, wardView: AutoCompleteTextView, thanaView: AutoCompleteTextView) {
        RetrofitClient.instance.getWard().enqueue(object : retrofit2.Callback<List<LocationModel>> {
            override fun onResponse(call: retrofit2.Call<List<LocationModel>>, response: retrofit2.Response<List<LocationModel>>) {
                if (response.isSuccessful) {
                    val wardsList = response.body() ?: emptyList()
                    val dropDown = SetupDropDown()

                    dropDown.setupDropdown(wardView, wardsList, context) { wardId ->
                        // Handle the selected ward ID here
                        println("Selected Ward ID: $wardId")
                        selectedWardId = wardId.toString()
                        loadThanas(wardId, context, thanaView)
                    }

                } else {
                    println(response)
                    Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<LocationModel>>, t: Throwable) {
                println("Error: ${t.message}")
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun loadThanas(wardId: Int, context: Context, inputView: AutoCompleteTextView) {
        RetrofitClient.instance.getThanasByWardId(wardId).enqueue(object :
            Callback<List<LocationModel>> {
            override fun onResponse(call: Call<List<LocationModel>>, response: Response<List<LocationModel>>) {
                if (response.isSuccessful) {
                    val thanaList = response.body() ?: emptyList()
                    val dropDown = SetupDropDown()
                    dropDown.setupDropdown(inputView, thanaList, context) { thanaId ->
                        // Handle the selected ward ID here
                        println("Selected Thana ID: $thanaId")
                        selectedThanaId = thanaId.toString()
                    }

                } else {
                    Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<LocationModel>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun setupDate() {
        val days = listOf("Select Day") + (1..31).map { it.toString() }
        val months = listOf("Select Month") + (1..12).map { it.toString() }

        binding.dobCardView.daySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)
        binding.dobCardView.monthSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)

        setupYearSpinner()
    }


    private fun setupYearSpinner() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val startYear = currentYear - 90
        val endYear = currentYear

        val years = listOf("Select Year") + (startYear..endYear).map { it.toString() }

        binding.dobCardView.yearSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)
    }



    private fun getSelectedDate(): String? {
        val selectedDay = binding.dobCardView.daySpinner.selectedItem.toString()
        val selectedMonth = binding.dobCardView.monthSpinner.selectedItem.toString()
        val selectedYear = binding.dobCardView.yearSpinner.selectedItem.toString()

        if (selectedDay == "Select Day" && selectedMonth == "Select Month" && selectedYear == "Select Year") {
            Toast.makeText(this, "Please select a valid date", Toast.LENGTH_SHORT).show()
            return null
        }

        return "$selectedDay-$selectedMonth-$selectedYear"
    }





    override fun onSupportNavigateUp(): Boolean {
        // This method is called when the up button is pressed. Just finish the activity
        finish()
        return true
    }

}