package idat.edu.pe.mydentalkrebs.model

/*
     "id": 2,
        "name": "Paciente Test",
        "lastname": "Rodriguez Cordero",
        "email": "patient@hotmail.com",
        "dni": "06187738",
        "address": null,
        "phone": null,
        "role": "patient"
*/

data class User(
    val id: Int,
    val name: String,
    val lastname: String,
    val email: String,
    val dni: String,
    val address: String,
    val phone: String,
    val role: String
    )