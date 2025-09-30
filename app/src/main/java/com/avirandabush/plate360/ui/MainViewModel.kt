package com.avirandabush.plate360.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avirandabush.plate360.model.*
import com.avirandabush.plate360.data.VehicleRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val repository = VehicleRepository()

    private val _vehicle = MutableLiveData<VehicleRecord?>()
    val vehicle: LiveData<VehicleRecord?> get() = _vehicle

    private val _history = MutableLiveData<VehicleHistory?>()
    val history: LiveData<VehicleHistory?> get() = _history

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun searchCar(plate: String) {
        _loading.postValue(true)

        repository.searchVehicle(plate).enqueue(object : Callback<ApiResponse<VehicleRecord>> {
            override fun onResponse(
                call: Call<ApiResponse<VehicleRecord>>,
                response: Response<ApiResponse<VehicleRecord>>
            ) {
                _loading.postValue(false)

                if (response.isSuccessful) {
                    val record = response.body()?.result?.records?.firstOrNull()
                    _vehicle.postValue(record)

                    // אם מצאנו רכב – נמשוך גם היסטוריה
                    if (record != null) {
                        loadHistory(plate)
                    }
                } else {
                    _error.postValue("שגיאה בקבלת נתוני רכב: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<VehicleRecord>>, t: Throwable) {
                _loading.postValue(false)
                _error.postValue("שגיאה: ${t.message}")
            }
        })
    }

    private fun loadHistory(plate: String) {
        _loading.postValue(true)

        repository.getVehicleHistory(plate).enqueue(object : Callback<ApiResponse<VehicleHistory>> {
            override fun onResponse(
                call: Call<ApiResponse<VehicleHistory>>,
                response: Response<ApiResponse<VehicleHistory>>
            ) {
                _loading.postValue(false)

                if (response.isSuccessful) {
                    val history = response.body()?.result?.records?.firstOrNull()
                    _history.postValue(history)
                } else {
                    _error.postValue("שגיאה בטעינת היסטוריה: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<VehicleHistory>>, t: Throwable) {
                _loading.postValue(false)
                _error.postValue("שגיאה בטעינת היסטוריה: ${t.message}")
            }
        })
    }
}
