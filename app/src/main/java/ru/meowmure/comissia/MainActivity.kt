package ru.meowmure.comissia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CurrencyAdapter(emptyList()) { currencyCode ->
            startActivity(GraphActivity.newIntent(this, currencyCode))
        }

        recyclerView.adapter = adapter

        viewModel.rates.observe(this, Observer { rates ->
            adapter.updateData(rates)
        })

        viewModel.fetchRates()

        val converterButton: Button = findViewById(R.id.buttonConvert)
        converterButton.setOnClickListener {
            val intent = Intent(this, CurrencyConverterActivity::class.java)
            startActivity(intent)
        }

    }
}
