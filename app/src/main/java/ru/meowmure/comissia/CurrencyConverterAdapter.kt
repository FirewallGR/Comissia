package ru.meowmure.comissia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class CurrencyConverterAdapter(
    context: Context,
    private val currencyList: List<Pair<String, String>>
) : ArrayAdapter<Pair<String, String>>(context, R.layout.item_currency_spinner, currencyList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_currency_spinner, parent, false)

        val currencyCode = view.findViewById<TextView>(R.id.currency_code)
        val currencyFlag = view.findViewById<ImageView>(R.id.currency_flag)

        val (code, flagUrl) = currencyList[position]
        currencyCode.text = code
        Glide.with(context)
            .load(flagUrl)
            .into(currencyFlag)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}