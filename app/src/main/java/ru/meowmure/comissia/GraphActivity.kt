package ru.meowmure.comissia

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GraphActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val chart: LineChart = findViewById(R.id.line_chart)
        val currencyCode = intent.getStringExtra(EXTRA_CURRENCY) ?: return

        val today = LocalDate.now()
        val tenDaysAgo = today.minusDays(10)
        val formatter = DateTimeFormatter.ISO_DATE

        val startDate = tenDaysAgo.format(formatter)
        val endDate = today.format(formatter)

        viewModel.historicalRates.observe(this, { rates ->
            setupChart(chart, rates)
        })
        println("ONCREATE")
        viewModel.fetchHistoricalRates(startDate, endDate, currencyCode)

        setupDatePicker(chart, currencyCode)
    }

    private fun setupDatePicker(chart: LineChart, currencyCode: String) {
        val startDatePicker: Button = findViewById(R.id.start_date_picker)
        val endDatePicker: Button = findViewById(R.id.end_date_picker)

        var selectedStartDate = LocalDate.now().minusDays(10)
        var selectedEndDate = LocalDate.now()

        val formatter = DateTimeFormatter.ISO_DATE

        startDatePicker.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    selectedStartDate = LocalDate.of(year, month + 1, day)
                    startDatePicker.text = selectedStartDate.toString()

                    if (selectedStartDate <= selectedEndDate) {
                        viewModel.fetchHistoricalRates(
                            selectedStartDate.format(formatter),
                            selectedEndDate.format(formatter),
                            currencyCode
                        )
                    } else {
                        Toast.makeText(this, "Start date cannot be after end date", Toast.LENGTH_SHORT).show()
                    }
                },
                selectedStartDate.year,
                selectedStartDate.monthValue - 1,
                selectedStartDate.dayOfMonth
            ).show()
        }

        endDatePicker.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    selectedEndDate = LocalDate.of(year, month + 1, day)
                    endDatePicker.text = selectedEndDate.toString()

                    if (selectedStartDate <= selectedEndDate) {
                        viewModel.fetchHistoricalRates(
                            selectedStartDate.format(formatter),
                            selectedEndDate.format(formatter),
                            currencyCode
                        )
                    } else {
                        Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                    }
                },
                selectedEndDate.year,
                selectedEndDate.monthValue - 1,
                selectedEndDate.dayOfMonth
            ).show()
        }

        startDatePicker.text = selectedStartDate.toString()
        endDatePicker.text = selectedEndDate.toString()
    }

    private fun setupChart(chart: LineChart, rates: Map<String, Double>) {
        val entries = rates.entries.mapIndexed { index, entry ->
            Entry(index.toFloat(), entry.value.toFloat())
        }

        val lineDataSet = LineDataSet(entries, "Exchange Rate History").apply {
            color = ContextCompat.getColor(this@GraphActivity, R.color.purple_500)
            valueTextColor = ContextCompat.getColor(this@GraphActivity, R.color.black)
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(ContextCompat.getColor(this@GraphActivity, R.color.teal_700))
            valueTextSize = 10f
            setDrawFilled(true)
            fillColor = ContextCompat.getColor(this@GraphActivity, R.color.teal_200)
        }

        val lineData = LineData(lineDataSet)
        chart.apply {
            data = lineData
            description.text = "Currency Rate Over Time"
            setPinchZoom(true)
            animateX(1000)
            invalidate()
        }
    }

    companion object {
        private const val EXTRA_CURRENCY = "extra_currency"

        fun newIntent(context: Context, currencyCode: String): Intent {
            return Intent(context, GraphActivity::class.java).apply {
                putExtra(EXTRA_CURRENCY, currencyCode)
            }
        }
    }
}