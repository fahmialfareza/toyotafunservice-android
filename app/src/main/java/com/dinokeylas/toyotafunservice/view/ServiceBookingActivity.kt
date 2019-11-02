package com.dinokeylas.toyotafunservice.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.dinokeylas.toyotafunservice.R
import com.dinokeylas.toyotafunservice.adapter.PlusServiceAdapter
import com.dinokeylas.toyotafunservice.model.Bookings
import com.dinokeylas.toyotafunservice.model.EmergencyCall
import com.dinokeylas.toyotafunservice.model.PlusService
import com.dinokeylas.toyotafunservice.model.User
import com.dinokeylas.toyotafunservice.util.Constant.Collection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_service_booking.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ServiceBookingActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var toolbar: Toolbar

    //need to upload data
    private var province: String = "Pilih Provinsi"
    private var city: String = "Pilih Kota/Kabupaten"
    private var garage: String = "Pilih Bengkel"
    private var hourOfDays = 0
    private var minutes = 0
    private lateinit var newDates: Calendar
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var additionalService: String
    private lateinit var officer: String
    private lateinit var complaint: String
    private var estimation: Int = 0
    private lateinit var component: String
    private var waitingApproval: Boolean = false
    private var totalCost: Double = 0.0
    private lateinit var status: String

    private lateinit var plusServiceAdapter: PlusServiceAdapter
    private lateinit var dateFormatter: SimpleDateFormat
    private lateinit var timeFormatter: SimpleDateFormat
    private var isDateAssigned: Boolean = false
    private lateinit var userModel: User
    private var mUser: FirebaseUser? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_booking)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setTitle("Booking Service")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initSpinner()

        newDates = Calendar.getInstance()

        val DATE_FORMAT = "dd/MM/yyyy"
        val TIME_FORMAT = "HH:mm:ss"
        dateFormatter = SimpleDateFormat(DATE_FORMAT)
        timeFormatter = SimpleDateFormat(TIME_FORMAT)

        btn_select_date.setOnClickListener { showDateDialog() }
        btn_select_time.setOnClickListener { showTimeDialog() }

        val arr = ArrayList<PlusService>()
        arr.add(PlusService("Soda", false))
        arr.add(PlusService("Sprite", false))
        arr.add(PlusService("Fanta", false))

        plusServiceAdapter = PlusServiceAdapter(this, R.layout.row_item_plus_service, arr)
        lv_plus_service.adapter = plusServiceAdapter

        btn_book.setOnClickListener {

            mUser = FirebaseAuth.getInstance().currentUser

            val userDB = FirebaseDatabase.getInstance().reference
            userDB.child(Collection.USER).child(mUser!!.uid).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            userModel = dataSnapshot.getValue(User::class.java)!!
                            uploadToFirebse()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.d("DATA-KU", "Failed to read value.", error.toException())
                    }
                }
            )

        }
    }

    private fun uploadToFirebse(){
        time = "$hourOfDays:$minutes"
        date = dateFormatter.format(newDates.time)
        additionalService = getAdditionalService()
        officer = "Officer"
        complaint = et_problem.text.toString().trim()
        estimation = 0
        component = "Component"
        waitingApproval = false
        totalCost = 0.0
        status = "Booked"

        val bookings = Bookings(
            mUser!!.uid,
            userModel.email,
            userModel.userName,
            date,
            time,
            province,
            city,
            garage,
            additionalService,
            officer,
            complaint,
            estimation,
            component,
            waitingApproval,
            totalCost,
            status
        )

        val db = FirebaseDatabase.getInstance().reference
        db.child(Collection.BOOKINGS).push().setValue(bookings).addOnSuccessListener {
            val toast = Toast.makeText(this, "Berhasil", Toast.LENGTH_LONG)
            toast.show()
            startActivity(Intent(this, HomeActivity::class.java))
        }. addOnFailureListener {
            val toast = Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private fun getAdditionalService(): String {
        additionalService = ""
        val data = plusServiceAdapter.getServicePlusList()
            for (i in data){
                if(i.checked){
                    additionalService += i.name + ","
                }
            }
        return additionalService
    }

    private fun initSpinner(){
        val spinnerProvince: Spinner = findViewById(R.id.spinner_province)
        val adapterProvince = ArrayAdapter.createFromResource(this, R.array.province_array, android.R.layout.simple_spinner_item)
        adapterProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProvince.adapter = adapterProvince
        spinnerProvince.onItemSelectedListener = this

        val spinnerCity: Spinner = findViewById(R.id.spinner_city)
        val adapterCity = ArrayAdapter.createFromResource(this, R.array.city_array, android.R.layout.simple_spinner_item)
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = adapterCity
        spinnerCity.onItemSelectedListener = this

        val spinnerLocation: Spinner = findViewById(R.id.spinner_location)
        val adapterLocation = ArrayAdapter.createFromResource(this, R.array.location_array, android.R.layout.simple_spinner_item)
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLocation.adapter = adapterLocation
        spinnerLocation.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val item: String = parent?.getItemAtPosition(position).toString()
        when(parent?.id){
            R.id.spinner_province -> province = item
            R.id.spinner_city -> city = item
            R.id.spinner_location -> garage = item
        }
        val temp: String = ("$province $city $garage")
        val toast = Toast.makeText(this, temp, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun showDateDialog(){
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                newDates = newDate
                isDateAssigned = true

                val formatDate = dateFormatter.format(newDate.time)
                btn_select_date.text = formatDate
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimeDialog(){
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                hourOfDays = hourOfDay
                minutes = minute
                val time = "$hourOfDay:$minute"
                btn_select_time.text = time
            },
            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
