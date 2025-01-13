package com.adaptixinnovate.tanvirahmedrobin.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.adapter.FullscreenImageAdapter
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityFullscreenBinding
import com.adaptixinnovate.tanvirahmedrobin.model.GalleryModel

class FullscreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var adapter: FullscreenImageAdapter
    private var galleryItems: List<GalleryModel> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Setup the toolbar
        setSupportActionBar(binding.toolbar)
        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        galleryItems = intent.getParcelableArrayListExtra("GALLERY_ITEMS") ?: listOf()
        val selectedItemId = intent.getIntExtra("SELECTED_ITEM_ID", -1)

        setupViewPager(selectedItemId)
    }


    private fun setupViewPager(selectedItemId: Int) {
        adapter = FullscreenImageAdapter(galleryItems)
        binding.viewPager.adapter = adapter

        val initialPosition = galleryItems.indexOfFirst { it.id == selectedItemId }
        binding.viewPager.setCurrentItem(initialPosition, false)
    }


    override fun onSupportNavigateUp(): Boolean {
        // This method is called when the up button is pressed. Just finish the activity
        finish()
        return true
    }

}