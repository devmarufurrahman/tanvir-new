package com.adaptixinnovate.tanvirahmedrobin.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.adapter.Service
import com.adaptixinnovate.tanvirahmedrobin.adapter.ServiceAdapter
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityMainBinding
import com.adaptixinnovate.tanvirahmedrobin.services.FirebaseService
import com.adaptixinnovate.tanvirahmedrobin.services.GetData
import com.adaptixinnovate.tanvirahmedrobin.services.HomeService
import com.adaptixinnovate.tanvirahmedrobin.services.SharedPrefereneService
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }


//        setup banner
        GetData.showBanner(this, binding.banner.imageSlider)
        // Setup toolbar
        val settings = SharedPrefereneService.getSettingsFromPreferences(this)
        binding.customToolbar.title = resources.getString(R.string.app_name, settings.name)
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
        val headerName = headerView.findViewById<TextView>(R.id.textViewUserName)
        val headerEmail = headerView.findViewById<TextView>(R.id.textViewUserEmail)
        val headerPhone = headerView.findViewById<TextView>(R.id.textViewUserPhone)
        val headerImage = headerView.findViewById<CircleImageView>(R.id.imageViewProfile)

        Picasso.get()
            .load("${AppConfig.IMG_URL}uploads/${settings.logo}")
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.load_error)
            .into(headerImage)


        headerName.text = settings.name
        headerEmail.text = settings.email
        headerPhone.text = settings.phone


        val icons = listOf("facebook", "x", "youtube")
        val urls = listOf(
            settings.facebook,
            settings.x,
            settings.youtube
        )
        val drawables = listOf(R.drawable.facebook, R.drawable.x_logo, R.drawable.youtube)

        binding.footer.fbLink.setOnClickListener {
            openUrl("https://www.facebook.com/ssnazmusshakib")
        }



        icons.zip(urls.zip(drawables)).forEach { (icon, pair) ->
            val (url, drawable) = pair
            val imageView = createImageView(drawable)
            binding.footer.frameContainer.addView(imageView)
            imageView.setOnClickListener { openUrl(url) }
        }

//        Side navigation view end here ================================================

        setupRecyclerView()

    }


    private fun createImageView(drawableRes: Int): ImageView {
        return ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.image_size),
                resources.getDimensionPixelSize(R.dimen.image_size)
            ).apply {
                marginEnd = resources.getDimensionPixelSize(R.dimen.image_margin)
            }
            setImageResource(drawableRes)
            isClickable = true
            isFocusable = true
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e : Exception){
            e.printStackTrace()
            Toast.makeText(this, "No URL Found to Open", Toast.LENGTH_SHORT).show()
        }

    }



    private fun setupRecyclerView() {
        val services = listOf(
            Service(getString(R.string.complain_us), R.drawable.ic_complain),
            Service(getString(R.string.extortion), R.drawable.extortion),
            Service(getString(R.string.gallery31), R.drawable.gallery),
            Service(getString(R.string.gallery), R.drawable.gallery31),
            Service(getString(R.string.contact_me), R.drawable.ic_contact),
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
            R.id.nav_data_collection -> {
                // Add appropriate action for "Complain Us"
                val intent = Intent(this, DataCollectionActivity::class.java)
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