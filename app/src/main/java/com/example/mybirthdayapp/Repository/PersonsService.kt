package com.example.mybirthdayapp.Repository

import retrofit2.Call
import com.example.mybirthdayapp.models.Person
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonsService {

    @GET("persons")
    fun GetByUserId(@Query("user_id")userid:String): Call<List<Person>>

    @GET("persons/{personId}")
    fun GetPersonsById(@Path("personId") personId: Int): Call<Person>

    @POST("persons")
    fun savePersons(@Body person: Person): Call<Person>

    @DELETE("persons/{id}")
    fun delete(@Path("id") id: Int): Call<Person>

    @PUT("persons/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person: Person): Call<Person>

}