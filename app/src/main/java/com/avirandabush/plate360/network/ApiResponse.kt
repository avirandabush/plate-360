package com.avirandabush.plate360.network

data class ApiResponse<T>(
    val result: ResultData<T>
)

data class ResultData<T>(
    val records: List<T>
)
