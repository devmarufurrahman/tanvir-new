package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.adapter.GalleryAdapter
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityGallery31DofaBinding
import com.adaptixinnovate.tanvirahmedrobin.network.api.ApiService
import com.adaptixinnovate.tanvirahmedrobin.network.repository.GalleryRepository
import com.adaptixinnovate.tanvirahmedrobin.ui.viewmodel.GalleryViewModel
import com.adaptixinnovate.tanvirahmedrobin.ui.viewmodel.GalleryViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Gallery31DofaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGallery31DofaBinding
    private lateinit var viewModel: GalleryViewModel
    private lateinit var adapter: GalleryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGallery31DofaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Setup the toolbar
        setSupportActionBar(binding.customToolbar)
        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        setupViewModel()
        setupRecyclerView()
        observeData()

    }

    private fun setupViewModel() {
        val apiService = Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val repository = GalleryRepository(apiService)
        val factory = GalleryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[GalleryViewModel::class.java]
    }



    private fun setupRecyclerView() {
        adapter = GalleryAdapter(emptyList()) { item ->
            val intent = Intent(this, FullscreenActivity::class.java)
            intent.putExtra("GALLERY_ITEMS", ArrayList(viewModel.galleryItems.value))
            intent.putExtra("SELECTED_ITEM_ID", item.id)
            startActivity(intent)
        }
        binding.gallery31RecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.gallery31RecyclerView.adapter = adapter
    }


    private fun observeData() {
        viewModel.galleryItems.observe(this) { items ->
            adapter = GalleryAdapter(items) { item ->
                val intent = Intent(this, FullscreenActivity::class.java)
                intent.putParcelableArrayListExtra("GALLERY_ITEMS", ArrayList(items))
                intent.putExtra("SELECTED_ITEM_ID", item.id)
                startActivity(intent)
            }
            binding.gallery31RecyclerView.adapter = adapter
        }

        viewModel.fetchGallery31Items()
    }

    override fun onSupportNavigateUp(): Boolean {
        // This method is called when the up button is pressed. Just finish the activity
        finish()
        return true
    }


}