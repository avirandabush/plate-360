package com.avirandabush.plate360.data

import com.avirandabush.plate360.model.*
import com.avirandabush.plate360.network.RetrofitClient
import retrofit2.Call

class VehicleRepository {
    private val govApi = RetrofitClient.govApi

    private val vehicleResourceId = "053cea08-09bc-40ec-8f7a-156f0677aff3"
    private val historyResourceId = "56063a99-8a3e-4ff4-912e-5966c0279bad"

    fun searchVehicle(plate: String): Call<ApiResponse<VehicleRecord>> {
        return govApi.searchVehicle(vehicleResourceId, plate)
    }

    fun getVehicleHistory(plate: String): Call<ApiResponse<VehicleHistory>> {
        return govApi.getVehicleHistory(historyResourceId, plate)
    }
}
