package com.example.moviemagnet

import android.os.*
import android.util.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import com.example.moviemagnet.databinding.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var type_of_file: Array<String> = emptyArray()
    var type_of_file_selected: String = ""
    var query_name: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        type_of_file = resources.getStringArray(R.array.spinner_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, type_of_file)
        binding.typeOfFile.adapter = adapter
        binding.typeOfFile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) type_of_file_selected = ""
                if (position >= 1) type_of_file_selected = type_of_file[position]
                Log.d("is_this_ok", "the selected type is --> $type_of_file_selected")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // todo: do something when nothing is selected
            }
        }
        if (binding.queryName != null) query_name = binding.queryName.text.toString()
    }

}