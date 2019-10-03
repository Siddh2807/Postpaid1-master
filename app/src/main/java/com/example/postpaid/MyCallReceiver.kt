package com.example.postpaid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast

class MyCallReceiver : BroadcastReceiver() {

    internal var call_date: String? = null
    internal var call_time: String? = null
    internal var parts: Array<String>? = null

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_RINGING) {
              val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            Toast.makeText(context, "Call from:$incomingNumber", Toast.LENGTH_LONG).show()

        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE || intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK) {

           Toast.makeText(context, "Detected call hangup event", Toast.LENGTH_LONG).show()

/*            			int total_Minutes=0;

            			String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
            			*//* Query the CallLog Content Provider *//*
            			Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,null, null, strOrder);
            			int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            			int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            			int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            			int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            			//sb.append("Call Log :");
            			while (managedCursor.moveToNext()) {
            				String phNum = managedCursor.getString(number);
            				String callTypeCode = managedCursor.getString(type);
            				String strcallDate = managedCursor.getString(date);
            				Date callDate = new Date(Long.valueOf(strcallDate));
            				SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            				String dateText = df2.format(callDate);

            				parts = dateText.split(" ");
            				call_date=parts[0];
            				call_time=parts[1];
            				String callDuration = managedCursor.getString(duration);
            				String callType = null;
            				int callcode = Integer.parseInt(callTypeCode);
            				switch (callcode) {
            				case CallLog.Calls.OUTGOING_TYPE:
            					callType = "Outgoing";
            					break;
            				case CallLog.Calls.INCOMING_TYPE:
            					callType = "Incoming";
            					break;
            				case CallLog.Calls.MISSED_TYPE:
            					callType = "Missed";
            					break;
            				}
            				total_Minutes += managedCursor.getInt(duration);;
            				//		   sb.append("\nPhone Number:--- " + phNum + " \nCall Type:--- "
            				//		     + callType + " \nCall Date:--- " + callDate
            				//		     + " \nCall duration in sec :--- " + callDuration);
            				//sb.append("\n----------------------------------");
            				//Log.d("date", startDate.getText().toString());
            				//db.addContact(new Contact( phNum, callType,callDuration,call_date,call_time));

            				db.addContact(new Contact( phNum, callType,callDuration,call_date,call_time));

            			}
            			//managedCursor.close();*/

            Toast.makeText(context, "done call history reading", Toast.LENGTH_LONG).show()

        }
    }
}