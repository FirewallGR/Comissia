package ru.meowmure.comissia

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.google.gson.Gson
import java.sql.Timestamp

interface ExchangeRatesApi {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") accessKey: String
    ): ExchangeRatesResponse

    @GET("{date}")
    suspend fun getHistoricalRates(
        @Path("date") date: String,
        @Query("access_key") accessKey: String,
        @Query("symbols") symbols: String
    ): HistoricalRatesResponse
}


data class ExchangeRatesResponse(
    val success: Boolean,
    val historical: Boolean,
    val date: String,
    val timestamp: String,
    val base: String,
    val rates: Map<String, Double>
)

data class HistoricalRatesResponse(
    val success: Boolean,
    val historical: Boolean,
    val date: String,
    val timestamp: String,
    val base: String,
    val rates: Map<String, Double>
)

