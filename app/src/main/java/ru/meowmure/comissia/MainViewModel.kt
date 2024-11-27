package ru.meowmure.comissia

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.meowmure.comissia.RetrofitInstance.API_KEY
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {
    private val _rates = MutableLiveData<List<Pair<String, Double>>>()
    val rates: LiveData<List<Pair<String, Double>>> get() = _rates

    private val _historicalRates = MutableLiveData<Map<String, Double>>()
    val historicalRates: LiveData<Map<String, Double>> get() = _historicalRates


    fun fetchRates() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getLatestRates(API_KEY)
                _rates.postValue(response.rates.toList())
                println("latest")
                println(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchHistoricalRates(startDate: String, endDate: String, symbol: String) {
        viewModelScope.launch {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val start = LocalDate.parse(startDate, dateFormatter)
            val end = LocalDate.parse(endDate, dateFormatter)

            val rates = mutableMapOf<String, Double>()


            try {
                var currentDate = start
                while (!currentDate.isAfter(end)) {
                    val response = RetrofitInstance.api.getHistoricalRates(
                        date = currentDate.toString(),
                        accessKey = API_KEY,
                        symbols = symbol
                    )
                    print(response.toString())

                    response.rates[symbol]?.let { rate ->
                        rates[currentDate.toString()] = rate
                    }

                    currentDate = currentDate.plusDays(1)
                }

                _historicalRates.postValue(rates)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}