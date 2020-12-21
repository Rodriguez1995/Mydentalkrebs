package idat.edu.pe.mydentalkrebs.io.response

import idat.edu.pe.mydentalkrebs.model.User

data class LoginResponse (val success: Boolean, val user: User, val jwt: String)