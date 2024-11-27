package ru.meowmure.comissia

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class CurrencyConverterActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        val spinnerFromCurrency: Spinner = findViewById(R.id.spinner_from_currency)
        val spinnerToCurrency: Spinner = findViewById(R.id.spinner_to_currency)
        val inputAmount: EditText = findViewById(R.id.input_amount)
        val convertButton: Button = findViewById(R.id.convert_button)
        val conversionResult: TextView = findViewById(R.id.conversion_result)

        viewModel.rates.observe(this) { rates ->

            val currencyList = rates.map {
                val (currencyCode, _) = it
                val flagUrl = "https://flagcdn.com/w320/${currencyCode.substring(0, 2).lowercase()}.png"
                currencyCode to flagUrl
            }

            val adapter = CurrencyConverterAdapter(this, currencyList)

            spinnerFromCurrency.adapter = adapter
            spinnerToCurrency.adapter = adapter
        }

        viewModel.fetchRates()

        convertButton.setOnClickListener {
            val fromCurrency = (spinnerFromCurrency.selectedItem as Pair<String, String>).first
            val toCurrency = (spinnerToCurrency.selectedItem as Pair<String, String>).first
            val amountText = inputAmount.text.toString()

            if (amountText.isEmpty()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val convertedValue = convertCurrency(fromCurrency, toCurrency, amount)
            conversionResult.text = "$amount $fromCurrency = $convertedValue $toCurrency"
            conversionResult.visibility = TextView.VISIBLE
        }
    }

    private fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double): Double {
        val rates = viewModel.rates.value?.toMap() ?: return 0.0
        val fromRate = rates[fromCurrency] ?: return 0.0
        val toRate = rates[toCurrency] ?: return 0.0
        val roundedResult = String.format("%.2f", (amount / fromRate) * toRate).toDouble()
        return roundedResult
    }
}