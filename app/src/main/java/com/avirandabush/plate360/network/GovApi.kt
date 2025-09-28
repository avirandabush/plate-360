package com.avirandabush.plate360.network

import com.avirandabush.plate360.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GovApi {
    @GET("api/3/action/datastore_search")
    fun searchVehicle(
        @Query("resource_id") resourceId: String,
        @Query("q") query: String,
        @Query("limit") limit: Int = 1
    ): Call<ApiResponse<VehicleRecord>>

    @GET("api/3/action/datastore_search")
    fun getVehicleHistory(
        @Query("resource_id") resourceId: String,
        @Query("q") query: String
    ): Call<ApiResponse<VehicleHistory>>
}
