package com.example.Catatan

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "MapsGPS"

        private val TABLE_CONTACTS = "employeTable"

        private val KEY_ID = "_id"
        private val KEY_NAMA_KEGIATAN = "nama_kegiatan"
        private val KEY_WAKTU_KEGIATAN = "waktu_kegiatan"
        private val KEY_LOKASI_KEGIATAN = "lokasi_kegiatan"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAMA_KEGIATAN + " TEXT,"
                + KEY_WAKTU_KEGIATAN + " TEXT,"
                + KEY_LOKASI_KEGIATAN + " TEXT)")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }


    // Methot untuk memasukkan DATA / Record

    fun addEmployee(emp: EmpModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAMA_KEGIATAN, emp.nama_kegiatan)
        contentValues.put(KEY_WAKTU_KEGIATAN, emp.waktu_kegiatan)
        contentValues.put(KEY_LOKASI_KEGIATAN, emp.lokasi_kegiatan)


        // Memasukkan detail karyawan menggunakan kueri sisipkan
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    // metode untuk membaca catatan
    fun viewEmployee(): ArrayList<EmpModel> {
        val empList: ArrayList<EmpModel> = ArrayList<EmpModel>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var nama_kegiatan: String
        var waktu_kegiatan: String
        var lokasi_kegiatan: String


        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                nama_kegiatan = cursor.getString(cursor.getColumnIndex(KEY_NAMA_KEGIATAN))
                waktu_kegiatan = cursor.getString(cursor.getColumnIndex(KEY_WAKTU_KEGIATAN))
                lokasi_kegiatan = cursor.getString(cursor.getColumnIndex(KEY_LOKASI_KEGIATAN))

                val emp = EmpModel(id = id, nama_kegiatan = nama_kegiatan, waktu_kegiatan = waktu_kegiatan, lokasi_kegiatan = lokasi_kegiatan)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    // method untuk menghapus data/record dalam database
    fun deleteEmployee(emp: EmpModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)

        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + emp.id, null)
        db.close()
        return success
    }

    // method untuk mengupdate data/record
//    fun updateEmployee(emp: EmpModel): Int {
//        val db = this.writableDatabase
//        val contentvalues = ContentValues()
//        contentvalues.put(KEY_NAME, emp.nama)
//        contentvalues.put(KEY_EMAIL, emp.email)
//
//        contentvalues.put(KEY_PHONE, emp.phone)
//        contentvalues.put(KEY_ALAMAT, emp.alamat)
//
//
//        val success = db.update(TABLE_CONTACTS, contentvalues, KEY_ID + "=" + emp.id,null)
//        db.close()
//        return success
//    }
}