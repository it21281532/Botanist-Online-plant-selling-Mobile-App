package com.example.feedback.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback.R
import com.example.feedback.models.EmployeeModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etEmpName: EditText
    private lateinit var etEmpEmail: EditText
    private lateinit var etEmpFeedback: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etEmpName = findViewById(R.id.etEmpName)
        etEmpEmail = findViewById(R.id.etEmpEmail)
        etEmpFeedback = findViewById(R.id.etEmpFeedback)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Feedbacks")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {

        //getting values
        val empName = etEmpName.text.toString()
        val empEmail = etEmpEmail.text.toString()
        val empFeedback = etEmpFeedback.text.toString()

        if (empName.isEmpty()) {
            etEmpName.error = "Please enter name"
        }
        if (empEmail.isEmpty()) {
            etEmpEmail.error = "Please enter email"
        }
        if (empFeedback.isEmpty()) {
            etEmpFeedback.error = "Please enter feedback"
        }

        val empId = dbRef.push().key!!

        val employee = EmployeeModel(empId, empName, empEmail, empFeedback)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etEmpName.text.clear()
                etEmpEmail.text.clear()
                etEmpFeedback.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}

