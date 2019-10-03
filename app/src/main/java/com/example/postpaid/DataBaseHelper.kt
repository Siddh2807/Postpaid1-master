
package com.example.postpaid

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

internal class DataBaseHelper(context: Context) : SQLiteOpenHelper
    (context, DATA_BASE_NAME, null, 1) {

    companion object {

        private var db: SQLiteDatabase? = null
        private val DATA_BASE_NAME = "Broadcast"
        private val TABLE_NAME = "receiver"

        private val KEY_ID = "id"
        private val KEY_PH_NO = "phone_number"
        private val KEY_TYPE = "type"
        private val KEY_DURATION = "duration"
        private val KEY_DATE = "date"
        private val KEY_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val TableCreate = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY , " +
                KEY_PH_NO + " TEXT , " + KEY_TYPE + " TEXT , " + KEY_DURATION + " TEXT ," +
                KEY_DATE + "TEXT, " + KEY_TIME+ "TEXT UNIQUE " + ")"
        db.execSQL(TableCreate)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(contact: Contact): Long {
        db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_PH_NO, contact.phoneNumber)
        values.put(KEY_TYPE, contact.type)        // Contact Type
        values.put(KEY_DURATION, contact.duration)   // Contact Duration
        values.put(KEY_DATE, contact.date)
        values.put(KEY_TIME, contact.time)

        val success = db!!.insert(TABLE_NAME, null, values)
        db!!.close()
        return success
    }

/*    fun viewData():List<Contact>{
        val empList:ArrayList<Contact> = ArrayList<Contact>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db!!.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db!!.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var userName: String
        var userMobile:String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("ID"))
                userName = cursor.getString(cursor.getColumnIndex("Name"))
                userMobile = cursor.getString(cursor.getColumnIndex("Mobile"))
                userEmail = cursor.getString(cursor.getColumnIndex("Email"))
                val emp= Contact(userId = userId, userName = userName, userMobile = userMobile,
                    userEmail = userEmail)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    fun updateData(emp: Registration): Int {
        db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Id, emp.userId)
        contentValues.put(COL_Name, emp.userName)
        contentValues.put(COL_Mobile, emp.userMobile)
        contentValues.put(COL_Email, emp.userEmail)

        val success = db!!.update(TABLE_NAME, contentValues,"ID="+emp.userId,null)
        db!!.close()
        return success
    }

    fun deleteData(emp: Registration): Int {
        db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Id, emp.userId)

        val success = db!!.delete(TABLE_NAME,"ID="+emp.userId,null)

        db!!.close()
        return success
    }*/
}