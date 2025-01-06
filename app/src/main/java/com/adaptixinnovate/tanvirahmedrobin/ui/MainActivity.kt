package com.adaptixinnovate.tanvirahmedrobin.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.adapter.Service
import com.adaptixinnovate.tanvirahmedrobin.adapter.ServiceAdapter
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityMainBinding
import com.adaptixinnovate.tanvirahmedrobin.services.FirebaseService
import com.adaptixinnovate.tanvirahmedrobin.services.GetData
import com.adaptixinnovate.tanvirahmedrobin.services.HomeService
import com.adaptixinnovate.tanvirahmedrobin.services.SendData
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.Calendar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Lazy initialization of binding object
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayoutVar: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Firebase Analytics Instance
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle().apply {
            putString("open_home", "Opening Home")
        }
        firebaseAnalytics.logEvent("open_home", bundle)


//        setup banner
        GetData.showBanner(this, binding.banner.imageSlider)
        // Setup toolbar
        // Initialize Views
        binding.apply {
            drawerLayoutVar = drawerLayout
            navigationView = navMenu
            toolbar = customToolbar
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.progressBar.visibility = View.VISIBLE
            recreate()
            // Code to refresh the content goes here

            binding.swipeRefreshLayout.isRefreshing = false
            binding.progressBar.visibility = View.GONE
        }


//        side navigation view start here ================================================

        // Setup Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu) // menu icon
        setupRecyclerView()

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

        binding.footer.copyTv.text = "© 2024 Tanvir Ahmed Robin. All rights reserved."
        val icons = listOf("facebook", "whatsapp", "youtube")
        val packageNames = listOf("com.facebook.katana", "com.whatsapp", "com.google.android.youtube")
        val drawables = listOf(R.drawable.facebook, R.drawable.whatsapp, R.drawable.youtube)


        icons.zip(packageNames.zip(drawables)).forEach { (icon, pair) ->
            val (packageName, drawable) = pair
            val imageView = createImageView(drawable)
            binding.footer.frameContainer.addView(imageView)
            imageView.setOnClickListener { openApp(packageName) }
        }

//        Side navigation view end here ================================================

        setupRecyclerView()

    }


    private fun createImageView(drawableRes: Int): ImageView {
        return ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.image_size),  // 30dp
                resources.getDimensionPixelSize(R.dimen.image_size)
            ).apply {
                marginEnd = resources.getDimensionPixelSize(R.dimen.image_margin)  // 8dp
            }
            setImageResource(drawableRes)
            isClickable = true
            isFocusable = true
        }
    }

    private fun openApp(packageName: String) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(packageName) ?: throw Exception()
            startActivity(intent)
        } catch (e: Exception) {
            // App not installed, maybe open a browser link or show a message
            Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show()
        }
    }



    private fun setupRecyclerView() {
        val services = listOf(
            Service(getString(R.string.data_collection_form), R.drawable.splash_image),
            Service(getString(R.string.contact_me), R.drawable.ic_contact),
            Service(getString(R.string.complain_us), R.drawable.ic_complain),
            Service(getString(R.string.about_me), R.drawable.ic_about),
            // Add more services here
        )

        binding.servicesRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = ServiceAdapter(services) { service ->
                HomeService.handleServiceClick(service, this@MainActivity)
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


    override fun onStart() {
        super.onStart()
        // Call the FirebaseUtils function to check the status
        FirebaseService.checkStatus(this)
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


}