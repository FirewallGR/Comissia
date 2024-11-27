package ru.meowmure.comissia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Retrofit

class CurrencyAdapter(
    private var currencies: List<Pair<String, Double>>,
    private val onCurrencyClick: (String) -> Unit
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyName: TextView = itemView.findViewById(R.id.currency_name)
        val currencyValue: TextView = itemView.findViewById(R.id.currency_value)
        val currencyIcon: ImageView = itemView.findViewById(R.id.currency_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val (currency, value) = currencies[position]
        holder.currencyName.text = currency
        holder.currencyValue.text = value.toString()
        

        val flagUrl = "https://flagcdn.com/w320/${currency.substring(0, 2).lowercase()}.png"

        Glide.with(holder.itemView.context)
            .load(flagUrl)
            .into(holder.currencyIcon)

        holder.itemView.setOnClickListener { onCurrencyClick(currency) }
    }

    override fun getItemCount() = currencies.size

    fun updateData(newCurrencies: List<Pair<String, Double>>) {
        currencies = newCurrencies
        notifyDataSetChanged()
    }
}