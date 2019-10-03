package com.example.postpaid

import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log

class StartDate : Activity() {

    private val calendarUriBase: String?
        get() {
            var calendarUriBase: String? = null
            var calendars = Uri.parse("content://calendar/calendars")
            var managedCursor: Cursor? = null
            try {
                managedCursor = managedQuery(calendars, null, null, null, null)
            } catch (e: Exception) {
            }

            if (managedCursor != null) {
                calendarUriBase = "content://calendar/"
            } else {
                calendars = Uri.parse("content://com.android.calendar/calendars")
                try {
                    managedCursor = managedQuery(calendars, null, null, null, null)
                } catch (e: Exception) {
                }

                if (managedCursor != null) {
                    calendarUriBase = "content://com.android.calendar/"
                }

            }

            return calendarUriBase
        }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_date)

        try {
            Log.i(DEBUG_TAG, "Starting Calendar Test")

           val iTestCalendarID = ListSelectedCalendars()

            if (iTestCalendarID != 0) {

                val newEvent2 = MakeNewCalendarEntry2(iTestCalendarID)
                val eventID2 = Integer.parseInt(newEvent2!!.lastPathSegment!!)
                ListCalendarEntry(eventID2)

                val newEvent = MakeNewCalendarEntry(iTestCalendarID)
                val eventID = Integer.parseInt(newEvent!!.lastPathSegment!!)
                ListCalendarEntry(eventID)

            } else {
                Log.i(DEBUG_TAG, "No 'Test' calendar found.")
            }

            Log.i(DEBUG_TAG, "Ending Calendar Test")


        } catch (e: Exception) {
            Log.e(DEBUG_TAG, "General failure", e)
        }
    }

    private fun ListSelectedCalendars(): Int {
        var result = 0
        val projection = arrayOf("_id", "name")
        val selection = "selected=1"
        val path = "calendars"

        val managedCursor = getCalendarManagedCursor(projection, selection,
                path)

        if (managedCursor != null && managedCursor.moveToFirst()) {

            Log.i(DEBUG_TAG, "Listing Selected Calendars Only")

            val nameColumn = managedCursor.getColumnIndex("name")
            val idColumn = managedCursor.getColumnIndex("_id")

            do {
                val calName = managedCursor.getString(nameColumn)
                val calId = managedCursor.getString(idColumn)
                Log.i(DEBUG_TAG, "Found Calendar '" + calName + "' (ID="
                        + calId + ")")
                if (calName != null && calName.contains("Test")) {
                    result = Integer.parseInt(calId)
                }
            } while (managedCursor.moveToNext())
        } else {
            Log.i(DEBUG_TAG, "No Calendars")
        }
        return result
    }

    private fun ListAllCalendarDetails() {
        val managedCursor = getCalendarManagedCursor(null, null, "calendars")

        if (managedCursor != null && managedCursor.moveToFirst()) {

            Log.i(DEBUG_TAG, "Listing Calendars with Details")

            do {

                Log.i(DEBUG_TAG, "**START Calendar Description**")

                for (i in 0 until managedCursor.columnCount) {
                    Log.i(DEBUG_TAG, managedCursor.getColumnName(i) + "="
                            + managedCursor.getString(i))
                }
                Log.i(DEBUG_TAG, "**END Calendar Description**")
            } while (managedCursor.moveToNext())
        } else {
            Log.i(DEBUG_TAG, "No Calendars")
        }

    }

    private fun ListAllCalendarEntries(calID: Int) {

        val managedCursor = getCalendarManagedCursor(null, "calendar_id=$calID", "events")

        if (managedCursor != null && managedCursor.moveToFirst()) {

            Log.i(DEBUG_TAG, "Listing Calendar Event Details")

            do {

                Log.i(DEBUG_TAG, "**START Calendar Event Description**")

                for (i in 0 until managedCursor.columnCount) {
                    Log.i(DEBUG_TAG, managedCursor.getColumnName(i) + "="
                            + managedCursor.getString(i))
                }
                Log.i(DEBUG_TAG, "**END Calendar Event Description**")
            } while (managedCursor.moveToNext())
        } else {
            Log.i(DEBUG_TAG, "No Calendars")
        }
    }

    private fun ListCalendarEntry(eventId: Int) {
        val managedCursor = getCalendarManagedCursor(null, null, "events/$eventId")

        if (managedCursor != null && managedCursor.moveToFirst()) {

            Log.i(DEBUG_TAG, "Listing Calendar Event Details")

            do {

                Log.i(DEBUG_TAG, "**START Calendar Event Description**")

                for (i in 0 until managedCursor.columnCount) {
                    Log.i(DEBUG_TAG, managedCursor.getColumnName(i) + "="
                            + managedCursor.getString(i))
                }
                Log.i(DEBUG_TAG, "**END Calendar Event Description**")
            } while (managedCursor.moveToNext())
        } else {
            Log.i(DEBUG_TAG, "No Calendar Entry")
        }

    }

    private fun ListCalendarEntrySummary(eventId: Int) {
        val projection = arrayOf("_id", "title", "dtstart")
        val managedCursor = getCalendarManagedCursor(projection, null, "events/$eventId")

        if (managedCursor != null && managedCursor.moveToFirst()) {

            Log.i(DEBUG_TAG, "Listing Calendar Event Details")

            do {

                Log.i(DEBUG_TAG, "**START Calendar Event Description**")

                for (i in 0 until managedCursor.columnCount) {
                    Log.i(DEBUG_TAG, managedCursor.getColumnName(i) + "="
                            + managedCursor.getString(i))
                }
                Log.i(DEBUG_TAG, "**END Calendar Event Description**")
            } while (managedCursor.moveToNext())
        } else {
            Log.i(DEBUG_TAG, "No Calendar Entry")
        }

    }

    private fun MakeNewCalendarEntry(calId: Int): Uri? {
        val event = ContentValues()

        event.put("calendar_id", calId)
        event.put("title", "Today's Event [TEST]")
        event.put("description", "2 Hour Presentation")
        event.put("eventLocation", "Online")

        val startTime = System.currentTimeMillis() + 1000 * 60 * 60
        val endTime = System.currentTimeMillis() + 1000 * 60 * 60 * 2

        event.put("dtstart", startTime)
        event.put("dtend", endTime)

        event.put("allDay", 0) // 0 for false, 1 for true
        event.put("eventStatus", 1)
        event.put("visibility", 0)
        event.put("transparency", 0)
        event.put("hasAlarm", 0) // 0 for false, 1 for true

        val eventsUri = Uri.parse(calendarUriBase!! + "events")

        return contentResolver.insert(eventsUri, event)
    }

    private fun MakeNewCalendarEntry2(calId: Int): Uri? {
        val event = ContentValues()

        event.put("calendar_id", calId)
        event.put("title", "Birthday [TEST]")
        event.put("description", "All Day Event")
        event.put("eventLocation", "Worldwide")

        val startTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24

        event.put("dtstart", startTime)
        event.put("dtend", startTime)

        event.put("allDay", 1) // 0 for false, 1 for true
        event.put("eventStatus", 1)
        event.put("visibility", 0)
        event.put("transparency", 0)
        event.put("hasAlarm", 0) // 0 for false, 1 for true

        val eventsUri = Uri.parse(calendarUriBase!! + "events")
        return contentResolver.insert(eventsUri, event)

    }

    private fun UpdateCalendarEntry(entryID: Int): Int {
        var iNumRowsUpdated = 0

        val event = ContentValues()

        event.put("title", "Changed Event Title")
        event.put("hasAlarm", 1) // 0 for false, 1 for true

        val eventsUri = Uri.parse(calendarUriBase!! + "events")
        val eventUri = ContentUris.withAppendedId(eventsUri, entryID.toLong())

        iNumRowsUpdated = contentResolver.update(eventUri, event, null, null)

        Log.i(DEBUG_TAG, "Updated $iNumRowsUpdated calendar entry.")

        return iNumRowsUpdated
    }

    private fun DeleteCalendarEntry(entryID: Int): Int {
        var iNumRowsDeleted = 0

        val eventsUri = Uri.parse(calendarUriBase!! + "events")
        val eventUri = ContentUris.withAppendedId(eventsUri, entryID.toLong())
        iNumRowsDeleted = contentResolver.delete(eventUri, null, null)

        Log.i(DEBUG_TAG, "Deleted $iNumRowsDeleted calendar entry.")

        return iNumRowsDeleted
    }

    private fun getCalendarManagedCursor(projection: Array<String>?,
                                         selection: String?, path: String): Cursor? {
        var calendars = Uri.parse("content://calendar/$path")

        var managedCursor: Cursor? = null
        try {
            managedCursor = managedQuery(calendars, projection, selection, null, null)
        } catch (e: IllegalArgumentException) {
            Log.w(DEBUG_TAG, "Failed to get provider at ["
                    + calendars.toString() + "]")
        }

        if (managedCursor == null) {
            // try again
            calendars = Uri.parse("content://com.android.calendar/$path")
            try {
                managedCursor = managedQuery(calendars, projection, selection, null, null)
            } catch (e: IllegalArgumentException) {
                Log.w(DEBUG_TAG, "Failed to get provider at ["
                        + calendars.toString() + "]")
            }

        }
        return managedCursor
    }

    companion object {

        private val DEBUG_TAG = "CalendarActivity"
    }
}