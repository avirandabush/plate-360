package com.avirandabush.plate360

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.avirandabush.plate360.network.RetrofitClient
import com.avirandabush.plate360.network.ApiResponse
import com.avirandabush.plate360.model.VehicleRecord
import com.avirandabush.plate360.model.VehicleHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var inputPlate: EditText
    private lateinit var btnSearch: Button
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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

        // לחיצה על חיפוש במקלדת
        inputPlate.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handleSearch()
                true
            } else {
                false
            }
        }
    }

    private fun handleSearch() {
        val plate = inputPlate.text.toString().trim()
        if (plate.isNotEmpty()) {
            searchCar(plate)
            hideKeyboard()
        } else {
            textResult.text = "אנא הזן מספר רכב"
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(inputPlate.windowToken, 0)
    }

    private fun searchCar(plate: String) {
        val vehicleResourceId = "053cea08-09bc-40ec-8f7a-156f0677aff3" // מאגר כלי רכב
        val historyResourceId = "56063a99-8a3e-4ff4-912e-5966c0279bad" // היסטוריית רכבים פרטיים

        // קריאה ראשונה: פרטי הרכב
        RetrofitClient.govApi.searchVehicle(vehicleResourceId, plate)
            .enqueue(object : Callback<ApiResponse<VehicleRecord>> {
                override fun onResponse(
                    call: Call<ApiResponse<VehicleRecord>>,
                    response: Response<ApiResponse<VehicleRecord>>
                ) {
                    if (response.isSuccessful) {
                        val record = response.body()?.result?.records?.firstOrNull()
                        if (record != null) {
                            // מציגים את הנתונים הבסיסיים על המסך
                            textResult.text = """
                            יצרן: ${record.tozeret_nm}
                            דגם: ${record.sug_degem_nm}
                            שנת ייצור: ${record.shnat_yitzur}
                            סוג דלק: ${record.sug_delek_nm}
                        """.trimIndent()

                            // קריאה שנייה: היסטוריית רכב
                            RetrofitClient.govApi.getVehicleHistory(historyResourceId, plate)
                                .enqueue(object : Callback<ApiResponse<VehicleHistory>> {
                                    override fun onResponse(
                                        call: Call<ApiResponse<VehicleHistory>>,
                                        response: Response<ApiResponse<VehicleHistory>>
                                    ) {
                                        if (response.isSuccessful) {
                                            val historyList = response.body()?.result?.records
                                            if (!historyList.isNullOrEmpty()) {
                                                val latest = historyList.first()
                                                textResult.append(
                                                    "\n\nהיסטוריה:" +
                                                            "\nק״מ אחרון: ${latest.kilometer_test_aharon ?: "לא זמין"}" +
                                                            "\nרישום ראשון: ${latest.rishum_rishon_dt ?: "לא זמין"}"
                                                )
                                            } else {
                                                textResult.append("\n\nאין נתוני היסטוריה.")
                                            }
                                        } else {
                                            textResult.append("\n\nשגיאה בטעינת היסטוריה: ${response.code()}")
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ApiResponse<VehicleHistory>>,
                                        t: Throwable
                                    ) {
                                        textResult.append("\n\nשגיאה בטעינת היסטוריה: ${t.message}")
                                    }
                                })
                        } else {
                            textResult.text = "לא נמצאו נתוני רכב."
                        }
                    } else {
                        textResult.text = "שגיאה בקבלת נתוני רכב: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<ApiResponse<VehicleRecord>>, t: Throwable) {
                    textResult.text = "שגיאה: ${t.message}"
                }
            })
    }
}
