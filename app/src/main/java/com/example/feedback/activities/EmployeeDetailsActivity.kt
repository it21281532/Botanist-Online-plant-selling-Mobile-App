package com.example.feedback.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback.R
import com.example.feedback.models.EmployeeModel
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var tvEmpId: TextView
    private lateinit var tvEmpName: TextView
    private lateinit var tvEmpEmail: TextView
    private lateinit var tvEmpFeedback: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empName").toString()
            )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("empId").toString()
            )
        }
    }

    private fun initView() {
        tvEmpId = findViewById(R.id.tvEmpId)
        tvEmpName = findViewById(R.id.tvEmpName)
        tvEmpEmail = findViewById(R.id.tvEmpEmail)
        tvEmpFeedback = findViewById(R.id.tvEmpFeedback)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvEmpId.text = intent.getStringExtra("empId")
        tvEmpName.text = intent.getStringExtra("empName")
        tvEmpEmail.text = intent.getStringExtra("empEmail")
        tvEmpFeedback.text = intent.getStringExtra("empFeedback")

    }
//delete record
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Feedbacks").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Feedback data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
//update details
    private fun openUpdateDialog(
        empId: String,
        empName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpName)
        val etEmpEmail = mDialogView.findViewById<EditText>(R.id.etEmpEmail)
        val etEmpFeedback = mDialogView.findViewById<EditText>(R.id.etEmpFeedback)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etEmpName.setText(intent.getStringExtra("empName").toString())
        etEmpEmail.setText(intent.getStringExtra("empEmail").toString())
        etEmpFeedback.setText(intent.getStringExtra("empFeedback").toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                empId,
                etEmpName.text.toString(),
                etEmpEmail.text.toString(),
                etEmpFeedback.text.toString()
            )

            Toast.makeText(applicationContext, "Feedback Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvEmpName.text = etEmpName.text.toString()
            tvEmpEmail.text = etEmpEmail.text.toString()
            tvEmpFeedback.text = etEmpFeedback.text.toString()

            alertDialog.dismiss()
        }
    }
    //update data
    private fun updateEmpData(
        id: String,
        name: String,
        email: String,
        feedback: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Feedbacks").child(id)
        val empInfo = EmployeeModel(id, name, email, feedback)
        dbRef.setValue(empInfo)
    }

}
