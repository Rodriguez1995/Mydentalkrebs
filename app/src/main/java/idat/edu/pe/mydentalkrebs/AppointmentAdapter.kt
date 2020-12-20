package idat.edu.pe.mydentalkrebs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import idat.edu.pe.mydentalkrebs.model.Appointment
import kotlinx.android.synthetic.main.item_appointment.view.*

class AppointmentAdapter(private val appointments: ArrayList<Appointment>)
    : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

       fun bind(appointment: Appointment) = with(itemView) {
               tvAppointmentId.text = context.getString(R.string.item_appointment_id, appointment.id)
               tvDoctorName.text = appointment.doctorName
               tvScheduledDate.text = context.getString(R.string.item_appointment_date, appointment.scheduledDate)
               tvScheduledTime.text = context.getString(R.string.item_appointment_time, appointment.scheduledTime)
           }

    }

    // Inflar el xml o de crear la lista del xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        )
    }

    // Asosia la data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]

        holder.bind(appointment)
    }

    // Devuelve la cantidad de elmentos
    override fun getItemCount() = appointments.size

}