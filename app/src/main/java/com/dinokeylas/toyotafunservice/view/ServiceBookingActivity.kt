package com.dinokeylas.toyotafunservice.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.dinokeylas.toyotafunservice.R
import kotlinx.android.synthetic.main.activity_service_booking.*
import java.text.SimpleDateFormat
import java.util.*

class ServiceBookingActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var toolbar: Toolbar
    private var province: String = "Pilih Provinsi"
    private var city: String = "Pilih Kota/Kabupaten"
    private var location: String = "Pilih Bengkel"

    private var hourOfDays = 0
    private var minutes = 0

    private lateinit var newDates: Calendar
    private lateinit var dateFormatter: SimpleDateFormat
    private lateinit var timeFormatter: SimpleDateFormat
    private var isDateAssigned: Boolean = false

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
            R.id.spinner_location -> location = item
        }
        val temp: String = ("$province $city $location")
        val toast = Toast.makeText(this, temp, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    fun showTimeDialog(){
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
