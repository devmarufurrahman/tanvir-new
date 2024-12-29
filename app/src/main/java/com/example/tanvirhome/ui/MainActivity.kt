package com.example.tanvirhome.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tanvirhome.R
import com.example.tanvirhome.databinding.ActivityMainBinding
import com.example.tanvirhome.services.GetData
import com.example.tanvirhome.services.SendData
import com.google.android.material.navigation.NavigationView
import java.util.Calendar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Lazy initialization of binding object
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayoutVar: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    // Data sources
    private val wardNames = arrayOf("Ward 1", "Ward 2", "Ward 3", "Ward 4", "Ward 5")
    private val thanaNames = arrayOf("Thana 1", "Thana 2", "Thana 3", "Thana 4", "Thana 5")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setup banner
        GetData.showBanner(this, binding.banner.imageTitle, binding.banner.mainImage)
        // Setup toolbar
        // Initialize Views
        binding.apply {
            drawerLayoutVar = drawerLayout
            navigationView = navMenu
            toolbar = customToolbar
        }


//        side navigation view start here ================================================

        // Setup Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu) // menu icon

        // Handle the back button using OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayoutVar.isDrawerOpen(GravityCompat.START)) {
                    drawerLayoutVar.closeDrawer(GravityCompat.START)
                } else {
                    // If you need to handle back press for fragments or other UI components, do it here.
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        // Set Navigation Listener
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null

//        navigation header listener
        val headerView = binding.navMenu.getHeaderView(0)
        val userNameTv = headerView.findViewById<TextView>(R.id.textViewUserName)

//        Side navigation view end here ================================================

        setupInputs()

        binding.dataSubmitButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val fatherName = binding.fatherNameInput.text.toString().trim()
            val motherName = binding.motherNameInput.text.toString().trim()
            val nid = binding.nidInput.text.toString().trim()
            val dateOfBirth = binding.dateOfBirthInput.text.toString().trim()
            val address = binding.addressInput.text.toString().trim()
            val wardName = binding.wardNameInput.text.toString().trim()
            val thanaName = binding.thanaNameInput.text.toString().trim()
            val mobile = binding.mobileInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()

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
                    binding.dateOfBirthInput.error = "Date of Birth cannot be empty"
                    return@setOnClickListener
                }
                address.isEmpty() -> {
                    binding.addressInput.error = "Address cannot be empty"
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
                email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailInput.error = "Enter a valid email address"
                    return@setOnClickListener
                }
                else -> {
                    SendData.dataCollection(name, fatherName, motherName, nid, dateOfBirth, address, wardName, thanaName, mobile, email, this, binding.progressBar)
                }
            }
        }



    }

    // Handle Navigation Item Clicks
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Add appropriate action for "Home"
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_about -> {
                // Add appropriate action for "About Us"
                val intent = Intent(this, AboutUs::class.java)
                startActivity(intent)
            }
            R.id.nav_contact -> {
                // Add appropriate action for "Contact Me"
                val intent = Intent(this, ContactMe::class.java)
                startActivity(intent)
            }
            R.id.nav_complain -> {
                // Add appropriate action for "Complain Us"
                val intent = Intent(this, ComplainUs::class.java)
                startActivity(intent)
            }


        }
        drawerLayoutVar.closeDrawer(GravityCompat.START)
        return true
    }



    // Handle Toolbar Menu Clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayoutVar.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupInputs() {
        // Set up dropdown inputs
        setupDropdown(binding.wardNameInput, wardNames)
        setupDropdown(binding.thanaNameInput, thanaNames)

        // Set up date picker for Date of Birth field
        binding.dateOfBirthInput.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupDropdown(inputView: AutoCompleteTextView, items: Array<String>) {
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, items)
        inputView.setAdapter(adapter)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            binding.dateOfBirthInput.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
        }, year, month, day)

        datePickerDialog.show()
    }

}