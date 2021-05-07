package com.example.Catatan

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Catatan.R
import kotlinx.android.synthetic.main.activity_history.*

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setupListOfDataIntoRecyclerView()

        //initialisasi tombol back
        val actionBar : ActionBar?= supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    // Method untuk menampilkan tombol back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    // method untuk mendapatkan jumlah record
    private fun getItemList(): ArrayList<EmpModel>{
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val empList: ArrayList<EmpModel> = databaseHandler.viewEmployee()
        rv_item.layoutManager = LinearLayoutManager(this)
        rv_item.adapter =  ItemAdapter(this,empList)
        return empList
    }
    // method untuk menampilkan emplist ke recycler view
    private fun  setupListOfDataIntoRecyclerView(){
        if (getItemList().size > -1){
            rv_item.visibility = View.VISIBLE
        }else{
            rv_item.visibility = View.GONE
        }
    }
    // method untuk menampilkan dialog konfirmasi delete
    fun deleteRecordAlertDialog(empModel: EmpModel) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Hapus?")
        builder.setMessage("Hapus Data Terpilih?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        // menampilkan tombol yes
        builder.setPositiveButton("Yes") { dialog: DialogInterface, which ->
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteEmployee(EmpModel(empModel.id, "", "",""))

            if (status > -1) {
                Toast.makeText(this, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
                setupListOfDataIntoRecyclerView()
            }

            dialog.dismiss()
        }
        // menampilkan tombol no
        builder.setNegativeButton("No") { dialog: DialogInterface, which ->
            //Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        // menampilkan user menekan tombol yes or no
        alertDialog.setCancelable(false)
        // menampilkan kotak dialog
        alertDialog.show()
    }

}