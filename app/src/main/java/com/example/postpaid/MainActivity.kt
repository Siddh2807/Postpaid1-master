package com.example.postpaid

import android.app.*
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.CallLog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var list: ListView
    private lateinit var levelDialog: AlertDialog
    private var db = DatabaseHandler(this)

    internal val context: Context = this
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private lateinit var parts: Array<String>
    private lateinit var call_date: String
    private lateinit var call_time: String
    private var itemname = ArrayList<String>()
    private var imgid = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       //getCallDetails()

        val button = findViewById(R.id.Search_Logs) as TextView
        button.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            //you should edit this to fit your needs
            builder.setTitle("Enter Date Cycle")

            val one = EditText(context)
            one.hint = "Start Date"
            val two = EditText(context)
            two.hint = "End Date"

            val lay = LinearLayout(context)
            lay.orientation = LinearLayout.VERTICAL
            lay.addView(one)
            lay.addView(two)
            builder.setView(lay)
            one.setOnClickListener {

                calender(one)
            }
            two.setOnClickListener {

                calender(two)
            }

            builder.setPositiveButton("Ok") { dialog, whichButton ->
                getDatabaseCallDetails(one.text.toString(), two.text.toString())
            }

            builder.setNegativeButton("Cancel") { dialog, whichButton -> dialog.cancel() }
            builder.show()

        }

        val button1 = findViewById(R.id.all_logs) as TextView
        button1.setOnClickListener {

            val items = arrayOf<CharSequence>(" Outgoing ", " Incoming ", " Missed ")

            // Creating and Building the Dialog
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Select the Call Type")
            builder.setSingleChoiceItems(items, -1) { dialog, item ->
                when (item) {
                    0 -> {
                        // Your code when first option seletced

                        val intent = Intent(this@MainActivity, AllCallLogs::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra("Call Type", "Outgoing")
                        startActivity(intent)
                    }
                    1 -> {
                        // Your code when 2nd  option seletced

                        val intent1 = Intent(this@MainActivity, AllCallLogs::class.java)
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent1.putExtra("Call Type", "Incoming")
                        startActivity(intent1)
                    }
                    2 -> {
                        // Your code when 3rd option seletced

                        val intent2 = Intent(this@MainActivity, AllCallLogs::class.java)
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent2.putExtra("Call Type", "Missed")
                        startActivity(intent2)
                    }
                }
                levelDialog.dismiss()
            }
            levelDialog = builder.create()
            levelDialog.show()
            // finish();
            //getDatabaseCallDetails();
        }


    }

    fun getCallDetails() {
        var total_Minutes = 0

        val strOrder = CallLog.Calls.DATE + " DESC"
        /* Query the CallLog Content Provider */
        val managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, strOrder)
        val number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER)
        val type = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
        val date = managedCursor.getColumnIndex(CallLog.Calls.DATE)
        val duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION)
        //sb.append("Call Log :");
        while (managedCursor.moveToNext()) {
            val phNum = managedCursor.getString(number)
            val callTypeCode = managedCursor.getString(type)
            val strcallDate = managedCursor.getString(date)
            val callDate = Date(java.lang.Long.valueOf(strcallDate))
            val df2 = SimpleDateFormat("yyyy-M-d HH:mm:ss")
            val dateText = df2.format(callDate)
            parts = dateText.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            call_date = parts[0]
            call_time = parts[1]
            val callDuration = managedCursor.getString(duration)
            var callType: String? = null
            val callcode = Integer.parseInt(callTypeCode)
            when (callcode) {
                CallLog.Calls.OUTGOING_TYPE -> callType = "Outgoing"
                CallLog.Calls.INCOMING_TYPE -> callType = "Incoming"
                CallLog.Calls.MISSED_TYPE -> callType = "Missed"
            }
            total_Minutes += managedCursor.getInt(duration)

            db.addContact(Contact(phNum, callType!!, callDuration, call_date, call_time))
        }
    }

    private fun getDatabaseCallDetails(S_Date: String, E_Date: String) {

        var OutGoing = 0
        var Incoming = 0
        var count_calls = 0

        val sb = StringBuffer()

        val contacts = db.getAllContactsByDate(Contact(S_Date, E_Date))

        Log.d("data accessed", "11111111111111111111")

        for (cn in contacts) {

            if (cn.type.contains("Outgoing")) {

                val Out_minute_call_wise: Int
                if (Integer.parseInt(cn.duration) / 60 < java.lang.Float.parseFloat(cn.duration) / 60) {
                    OutGoing += Integer.parseInt(cn.duration) / 60 + 1
                    Out_minute_call_wise = Integer.parseInt(cn.duration) / 60 + 1
                } else {
                    OutGoing += Integer.parseInt(cn.duration) / 60
                    Out_minute_call_wise = Integer.parseInt(cn.duration) / 60
                }

                sb.append(cn.phoneNumber + "  " + cn.date + " " + cn.time + " " + Out_minute_call_wise + "\n")
                itemname.add(cn.phoneNumber + "    " + Out_minute_call_wise)
                imgid.add(cn.date + "   " + cn.time)

            }

            if (cn.type.contains("Incoming")) {
               if (Integer.parseInt(cn.duration) / 60 < java.lang.Float.parseFloat(cn.duration) / 60) {
                    Incoming += Integer.parseInt(cn.duration) / 60 + 1

                } else {
                    Incoming += Integer.parseInt(cn.duration) / 60

                }
            }
            count_calls++

        }

        AlertDialog.Builder(this)
                .setTitle("All Call In Minutes")
                .setMessage("\n Call Log :$count_calls\nOutgoing In Minutes :$OutGoing\nIncoming In Minutes :$Incoming")
                .setPositiveButton(android.R.string.yes) { dialog, which ->

                    val adapter = CustomListAdapter(this@MainActivity, itemname, imgid)
                    list = findViewById(R.id.list) as ListView
                    list.adapter = adapter

                    list.onItemClickListener = OnItemClickListener { parent, view, position, id ->
                                            }
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    // do nothing
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    fun calender(any: EditText) {

        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // TODO Auto-generated method stub
                    any.setText(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
                    //if(monthOfYear!=10 || monthOfYear!=10)
                }, mYear, mMonth, mDay)
        dpd.show()
    }
}
