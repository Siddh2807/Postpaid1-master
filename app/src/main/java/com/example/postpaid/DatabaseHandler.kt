package com.example.postpaid

import java.util.ArrayList


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val allContacts: List<Contact>
        get() {
            val contactList = ArrayList<Contact>()
            val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"

            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor != null && cursor.count > 0) {
                Log.d("cursor", "value exist")
            } else
                Log.d("cursor", "value not exist")
            if (cursor!!.moveToFirst()) {
                do {

                    val contact = Contact()
                    contact.id = cursor.getString(0).toInt()
                    contact.phoneNumber = cursor.getString(1)
                    contact.type = cursor.getString(2)
                    contact.duration = cursor.getString(3)
                    Log.d("value", cursor.getString(2) + "  " + cursor.getString(3))
                    contact.date = cursor.getString(4)
                    contact.time = cursor.getString(5)
                    contactList.add(contact)
                } while (cursor.moveToNext())
            }
            return contactList
        }

    val contactsCount: Int
        get() {
            val countQuery = "SELECT  * FROM $TABLE_CONTACTS"
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            cursor.close()
            return cursor.count
        }

    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PH_NO + " TEXT,"
                + KEY_TYPE + " TEXT," + KEY_DURATION + " TEXT," + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT UNIQUE " + ")")
         db.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")

        onCreate(db)
    }

    internal fun addContact(contact: Contact) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_PH_NO, contact.phoneNumber)
        values.put(KEY_TYPE, contact.type)        // Contact Type
        values.put(KEY_DURATION, contact.duration)   // Contact Duration
        values.put(KEY_DATE, contact.date)      // Contact Date
        values.put(KEY_TIME, contact.time)      // Contact Time

        // Inserting Row
        try {
            db.insertOrThrow(TABLE_CONTACTS, null, values)
            Log.d("entered", "value entered ")
        } catch (ex: SQLiteConstraintException) {
            Log.d("Not entered", "entered the exception")
        }
    }

    internal fun getContact(id: Int): Contact {
        val db = this.readableDatabase

        val cursor = db.query(TABLE_CONTACTS, arrayOf(KEY_ID, KEY_TYPE, KEY_PH_NO), "$KEY_ID=?",
                arrayOf(id.toString()), null, null, null, null)
        cursor?.moveToFirst()

        return Contact(Integer.parseInt(cursor!!.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5))
    }

    // Getting All Contacts by date wise
    fun getAllContactsByDate(contact1: Contact): List<Contact> {
        val contactList = ArrayList<Contact>()
   Log.d("data accessed", contact1.startDate + "  " + contact1.endDate)
        val selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_DATE + " BETWEEN '" + contact1.startDate + "' AND '" + contact1.endDate + "'"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null && cursor.count > 0) {
            Log.d("cursor", "value exist")
        } else
            Log.d("cursor", "value not exist")
        if (cursor!!.moveToFirst()) {
            do {

                val contact = Contact()
                contact.id = Integer.parseInt(cursor.getString(0))
                contact.phoneNumber = cursor.getString(1)
                contact.type = cursor.getString(2)
                contact.duration = cursor.getString(3)
                Log.d("value", cursor.getString(2) + "  " + cursor.getString(3))
                contact.date = cursor.getString(4)
                contact.time = cursor.getString(5)
                // Adding contact to list
                contactList.add(contact)
            } while (cursor.moveToNext())
        }

        // return contact list
        return contactList
    }

    fun updateContact(contact: Contact): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_TYPE, contact.type)
        values.put(KEY_PH_NO, contact.phoneNumber)

        // updating row
        return db.update(TABLE_CONTACTS, values, "$KEY_ID = ?",
                arrayOf(contact.id.toString()))
    }

    fun deleteContact(contact: Contact) {
        val db = this.writableDatabase
        db.delete(TABLE_CONTACTS, "$KEY_ID = ?",
                arrayOf(contact.id.toString()))
        // db.close();
    }

    companion object {

        private val DATABASE_VERSION = 2

        private val DATABASE_NAME = "contactsManager"
        private val TABLE_CONTACTS = "contacts"

        private val KEY_ID = "id"
        private val KEY_PH_NO = "phone_number"
        private val KEY_TYPE = "type"
        private val KEY_DURATION = "duration"
        private val KEY_DATE = "date"
        private val KEY_TIME = "time"
    }

}