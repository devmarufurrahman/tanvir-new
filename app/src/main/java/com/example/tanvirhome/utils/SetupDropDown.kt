package com.example.tanvirhome.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.tanvirhome.R
import com.example.tanvirhome.model.LocationModel

class SetupDropDown {

    // Set up dropdown inputs
    fun setupDropdown(inputView: AutoCompleteTextView, items: List<LocationModel>, context: Context, onItemSelected: (Int) -> Unit ) {
        val adapter = ArrayAdapter(context, R.layout.dropdown_item, items.map { it.name })
        inputView.setAdapter(adapter)

        inputView.setOnItemClickListener { adapterView, view, position, id ->

            val selectedId = items[position].id
            onItemSelected(selectedId)
        }

    }

}