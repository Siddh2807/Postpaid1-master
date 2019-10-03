package com.example.postpaid

import java.util.ArrayList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomListAdapter(context: Context, private val itemname: ArrayList<String>, private val imgid: ArrayList<String>)// TODO Auto-generated constructor stub
    : ArrayAdapter<String>(context, R.layout.mylist, itemname) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val rowView = inflater.inflate(R.layout.mylist, null, true)

        val txtTitle = rowView.findViewById<View>(R.id.Phone_Number) as TextView
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        val extratxt = rowView.findViewById<View>(R.id.Call_Date) as TextView

        txtTitle.text = itemname[position]
        //imageView.setImageResource(imgid[position]);
        extratxt.text = imgid[position]
        return rowView

    }
}