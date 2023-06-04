package com.example.moviemagnet.ui.activity

import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import com.example.moviemagnet.*
import com.example.moviemagnet.databinding.*
import com.google.android.material.snackbar.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var type_of_file_array: Array<String> = emptyArray()
    var type_of_single_file_selected: String = ""
    var query_name: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        type_of_file_array = resources.getStringArray(R.array.spinner_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, type_of_file_array)
        binding.typeOfFile.adapter = adapter
        binding.typeOfFile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) type_of_single_file_selected = ""
                if (position >= 1) type_of_single_file_selected = type_of_file_array[position]
                /*Log.d("is_this_ok", "the selected type is --> $type_of_file_selected")*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // todo: do something when nothing is selected
            }
        }

        binding.findYourFile.setOnClickListener {
            query_name = binding.queryName.text.toString()
            /*Log.d("is_this_ok", "query name --> $query_name")*/
            if (query_name != "") {
                val intent = Intent(this, FileListActivity::class.java)
                intent.putExtra("query_name", query_name)
                intent.putExtra("type_of_single_file_selected", type_of_single_file_selected)
                startActivity(intent)
            } else {
                Snackbar.make(binding.root, "File name must not be empty", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}