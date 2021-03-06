package idat.edu.pe.mydentalkrebs.io

import idat.edu.pe.mydentalkrebs.io.response.LoginResponse
import idat.edu.pe.mydentalkrebs.model.Appointment
import idat.edu.pe.mydentalkrebs.model.Doctor
import idat.edu.pe.mydentalkrebs.model.Schedule
import idat.edu.pe.mydentalkrebs.model.Specialty
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {

    @GET("specialties")
    fun getSpecialties(): Call<ArrayList<Specialty>>

    @GET("specialties/{specialty}/doctors")
    fun getDoctors(@Path("specialty") specialtyId: Int): Call<ArrayList<Doctor>>

    @GET("schedule/hours")
    fun getHours(@Query("doctor_id") doctorId: Int, @Query("date") date: String):
            Call<Schedule>

    @POST("login")
    fun postLogin(@Query("email") email: String, @Query("password") password: String):
            Call<LoginResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization" ) authHeader: String): Call<Void>

    @GET("appointments")
    fun getAppointments(@Header("Authorization" ) authHeader: String):
            Call<ArrayList<Appointment>>

    companion object Factory {
        private const val BASE_URL = "http://104.236.32.157/api/"

        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

}