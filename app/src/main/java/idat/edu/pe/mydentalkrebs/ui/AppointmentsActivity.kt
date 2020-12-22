package idat.edu.pe.mydentalkrebs.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import idat.edu.pe.mydentalkrebs.R
import idat.edu.pe.mydentalkrebs.io.ApiService
import idat.edu.pe.mydentalkrebs.model.Appointment
import idat.edu.pe.mydentalkrebs.util.PreferenceHelper
import idat.edu.pe.mydentalkrebs.util.PreferenceHelper.get
import idat.edu.pe.mydentalkrebs.util.toast
import kotlinx.android.synthetic.main.activity_appointments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentsActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val appointmentAdapter = AppointmentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        loadAppointments()

        rvAppointments.layoutManager = LinearLayoutManager(this) //GridLayoutManager
        rvAppointments.adapter = appointmentAdapter
    }

    private fun loadAppointments() {
        val jwt = preferences["jwt",""]
        val call = apiService.getAppointments("Bearer $jwt")

        call.enqueue(object: Callback<ArrayList<Appointment>> {
            override fun onResponse(call: Call<ArrayList<Appointment>>, response: Response<ArrayList<Appointment>>) {
                if (response.isSuccessful) {
                    response.body()?.let{
                        appointmentAdapter.appointments = it
                        appointmentAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Appointment>>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }
}