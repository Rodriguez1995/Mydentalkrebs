package idat.edu.pe.mydentalkrebs.model

import com.google.gson.annotations.SerializedName

/*
{
   "id": 58,
    "description": "Ut et adipisci id consectetur quos dolore.",
    "scheduled_date": "2020-02-27",
    "type": "Operación",
    "created_at": "2020-12-20 21:13:29",
    "status": "Atendida",
    "scheduled_time_12": "10:27 AM",
    "specialty": {
        "id": 4,
        "name": "Endodoncia"
     },
     "doctor": {
         "id": 73,
          "name": "Fidel Jaskolski"
      }
   }
*/

data class Appointment (
    val id: Int,
    val description: String,
    val type: String,
    val status: String,

    @SerializedName("scheduled_date") val scheduledDate: String,
    @SerializedName("scheduled_time_12") val scheduledTime: String,
    @SerializedName("created_at") val createdAt: String,

    val specialty: Specialty,
    val doctor: Doctor
)