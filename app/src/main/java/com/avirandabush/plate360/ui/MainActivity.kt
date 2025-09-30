package com.avirandabush.plate360.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.avirandabush.plate360.R

class MainActivity : AppCompatActivity() {

    private lateinit var inputPlate: EditText
    private lateinit var btnSearch: Button
    private lateinit var textResult: TextView
    private lateinit var progressLoader: ProgressBar
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        progressLoader = findViewById(R.id.progress_loader)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputPlate = findViewById(R.id.input_plate)
        btnSearch = findViewById(R.id.btn_search)
        textResult = findViewById(R.id.text_result)

        btnSearch.setOnClickListener {
            handleSearch()
        }

        inputPlate.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handleSearch()
                true
            } else false
        }

        observeViewModel()
    }

    private fun handleSearch() {
        val plate = inputPlate.text.toString().trim()
        if (plate.isNotEmpty()) {
            viewModel.searchCar(plate)
            hideKeyboard()
        } else {
            textResult.text = "אנא הזן מספר רכב"
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            progressLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.vehicle.observe(this) { record ->
            if (record != null) {
                val history = viewModel.history.value
                val resultScreen = ResultScreen.newInstance(record, history)
                resultScreen.show(supportFragmentManager, "ResultScreen")

                supportFragmentManager.setFragmentResultListener(
                    "RESULT_SCREEN_CLOSED",
                    this
                ) { _, _ ->
                    inputPlate.text.clear()
                }
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            textResult.text = errorMsg
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(inputPlate.windowToken, 0)
    }
}
