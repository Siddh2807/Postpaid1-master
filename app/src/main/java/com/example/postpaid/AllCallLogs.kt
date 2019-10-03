package com.example.postpaid

import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class AllCallLogs : Activity() {
    private var textView: TextView? = null
    private var db = DatabaseHandler(this)
    private lateinit var call_type: String
    private var progress: ProgressDialog? = null
    private var sb = StringBuffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_call_logs)

        MyAsyncTaskClass().execute("foo", "baar", "ss")
        textView = findViewById<TextView>(R.id.textview_call_logs)
        //String data = getIntent().getExtras().getString("AnyKeyName");
        val iin = intent
        val b = iin.extras

        if (b != null) {
            call_type = b.get("Call Type") as String
            Log.d("Intent value", call_type)
        }
    }

    private fun getDatabaseCallDetails() {

        val OutGoing = 0
        val Incoming = 0
        val Missed = 0
        var count_calls = 0
        //StringBuffer sb = new StringBuffer();
        // Getting all logs value from database
        //List<Contact> contacts = db.getAllContacts(new Contact( startDate.getText().toString(),endDate.getText().toString()));
        val contacts = db.allContacts

        Log.d("data accessed", "11111111111111111111")


        for (cn in contacts) {

            //if(cn.getType().contains("Outgoing")){
            if (cn.type.contains(call_type)) {

                sb.append("\nPhone Number:--- " + cn.phoneNumber + " \nCall Type:--- "
                        + cn.type + " \nCall Date:--- " + cn.date
                        + " \nCall duration in sec :--- " + cn.duration)
                sb.append("\n----------------------------------")

            }
            count_calls++
        }
    }

    private inner class MyAsyncTaskClass : AsyncTask<String, String, Int>() {

        override fun onPreExecute() {

            Log.d("fsdfas", "11111111111111111111111")
            progress = ProgressDialog(this@AllCallLogs)
            progress!!.setTitle("Loading")
            progress!!.setMessage("Wait while loading...")
            progress!!.show()
            // To dismiss the dialog
            Log.d("fsdfas", "11111111111111111111111----------------")
        }

        override fun doInBackground(vararg params: String): Int? {

            Log.d("fsdfas", "22222222222222222222222222222")
            getDatabaseCallDetails()
            Log.d("fsdfas", "22222222222222222222222222222---------")
            return null
        }

        override fun onPostExecute(result: Int?) {
            // put here everything that needs to be done after your async task finishes
            Log.d("fsdfas", "3333333333333333333333333333")
            progress!!.dismiss()
            textView!!.text = sb
            db.close()
            Log.d("fsdfas", "3333333333333333333333333333-----------")
        }
    }

}