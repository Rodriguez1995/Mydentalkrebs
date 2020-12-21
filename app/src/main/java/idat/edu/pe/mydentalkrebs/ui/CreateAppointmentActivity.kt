package idat.edu.pe.mydentalkrebs.ui


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import idat.edu.pe.mydentalkrebs.R
import idat.edu.pe.mydentalkrebs.io.ApiService
import idat.edu.pe.mydentalkrebs.model.Specialty
import kotlinx.android.synthetic.main.activity_create_appointment.*
import kotlinx.android.synthetic.main.card_view_step_one.*
import kotlinx.android.synthetic.main.card_view_step_three.*
import kotlinx.android.synthetic.main.card_view_step_two.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import retrofit2.Callback
import retrofit2.Response

class CreateAppointmentActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private var selectedCalendar = Calendar.getInstance()
    private var selectedTimeRadioBtn: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_appointment)

        btnNext.setOnClickListener {
            if (etDescription.text.toString().length < 3) {
                etDescription.error = getString(R.string.validate_appointment_description)
            } else {
                // continue to step 2
                cvStept1.visibility = View.GONE
                cvStept2.visibility = View.VISIBLE
            }

        }

        btnNext2.setOnClickListener {
            when {
                etScheduledDate.text.toString().isEmpty() ->
                    etScheduledDate.error = getString(R.string.validate_appointment_date)
                selectedTimeRadioBtn == null ->
                    Snackbar.make(createAppointmentLinearLayout, R.string.validate_appointment_time, Snackbar.LENGTH_SHORT).show()
                else -> {
                    // continue to step 3
                    showAppointmentDataToConfirm()
                    cvStept2.visibility = View.GONE
                    cvStept3.visibility = View.VISIBLE
                }
            }
        }

        btnConfirmAppointment.setOnClickListener {
            Toast.makeText(this,"Cita registrada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }

        loadSpecialties()

        val doctorOptions = arrayOf("Doctor A", "Doctor B", "Doctor C")
        spinnerDoctors.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doctorOptions)
    }

    private fun loadSpecialties() {
        val call = apiService.getSpecialties()
        // Object Expression
        call.enqueue(object: Callback<ArrayList<Specialty>> {
            override fun onFailure(call: Call<ArrayList<Specialty>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity, getString(R.string.error_loading_specialties), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(call: Call<ArrayList<Specialty>>, response: Response<ArrayList<Specialty>>) {
                if (response.isSuccessful) { // Si el codigo de respuesta es entre 200 ... 300 accedemos a la respuesta
                    val specialties = response.body() // ArrayList de Especialidades (Objetos)
                    val specialtyOptions = ArrayList<String>() // ArrauList en cadena

                    specialties?.forEach {
                        specialtyOptions.add(it.name) // Tomamos la variable nombre de cada especialidad y guardamos en otro array en este caso specialtyOptions
                    }
                    spinnerSpecialties.adapter = ArrayAdapter<String>(this@CreateAppointmentActivity, android.R.layout.simple_list_item_1, specialtyOptions)
                }
            }
        })
    }

    private fun showAppointmentDataToConfirm() {
        tvConfirmDescription.text = etDescription.text.toString()
        tvConfirmSpecialty.text = spinnerSpecialties.selectedItem.toString()

        val selectedRadioBtnId = radioGroupType.checkedRadioButtonId
        val selectedRadioType = radioGroupType.findViewById<RadioButton>(selectedRadioBtnId)

        tvConfirmType.text = selectedRadioType.text.toString()

        tvConfirmDoctorName.text = spinnerDoctors.selectedItem.toString()
        tvConfirmDate.text = etScheduledDate.text.toString()
        tvConfirmTime.text = selectedTimeRadioBtn?.text.toString()
    }

    fun onClickScheduledDate(v: View?) {
        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
            //Toast.makeText(this, "$y-$m-$d", Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y, m, d)
            etScheduledDate.setText(
                    resources.getString(R.string.date_format,
                            y,
                        (m+1).twoDigits(),
                            d.twoDigits()
                    )
            )
            etScheduledDate.error = null
            displayRadioButtons()
        }

        // new dialog
        val datePickerDialog = DatePickerDialog(this, listener, year, month, dayOfMonth)
        val datePicker = datePickerDialog.datePicker

        // set limits
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH,1)
        datePicker.minDate = calendar.timeInMillis// +1 now
        calendar.add(Calendar.DAY_OF_MONTH, 29)
        datePicker.maxDate = calendar.timeInMillis // +30 now

        // show dialog
        datePickerDialog.show()
    }

    private fun displayRadioButtons() {
    //    radioGroup.clearCheck()
    //    radioGroup.removeAllViews()
    //    radioGroup.checkedRadioButtonId
        selectedTimeRadioBtn = null
        radioGroupLeft.removeAllViews()
        radioGroupRight.removeAllViews()

        val hours = arrayOf("3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM")
        var goToLeft = true

        hours.forEach {
            val radioButton = RadioButton(this)
            radioButton.id = View.generateViewId()
            radioButton.text = it

            radioButton.setOnClickListener { view->
                selectedTimeRadioBtn?.isChecked = false

                selectedTimeRadioBtn = view as RadioButton?
                selectedTimeRadioBtn?.isChecked = true
            }

            if (goToLeft)
                radioGroupLeft.addView(radioButton)
            else
                radioGroupRight.addView(radioButton)
            goToLeft = !goToLeft
        }


    }

    private fun Int.twoDigits()
            = if (this>=10) this.toString() else "0$this"

    override fun onBackPressed() {
        when {
            cvStept3.visibility == View.VISIBLE -> {
                cvStept3.visibility = View.GONE
                cvStept2.visibility = View.VISIBLE
            }
            cvStept2.visibility == View.VISIBLE -> {
                cvStept2.visibility = View.GONE
                cvStept1.visibility = View.VISIBLE

            }
            cvStept1.visibility == View.VISIBLE -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.dialog_create_appointment_exit_title))
                builder.setMessage(getString(R.string.dialog_create_appointment_exit_message))
                builder.setPositiveButton(getString(R.string.dialog_create_appointment_exit_possitive_btn)) { _, _ ->
                    finish()
                }
                builder.setNegativeButton(getString(R.string.dialog_create_appointment_exit_negative_btn)) { dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}