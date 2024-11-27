package ru.meowmure.comissia

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.exchangeratesapi.io/v1/"
    const val API_KEY = "7a81e147edd88bba8e211d8040ec81cf"
    private const val SYMBOLS_URL = "https://restcountries.com/v3.1/"

    val api: ExchangeRatesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRatesApi::class.java)
    }
}