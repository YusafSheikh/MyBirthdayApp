package com.example.mybirthdayapp.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mybirthdayapp.models.Person
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PersonsRepository {
    private val url = "https://birthdaysrest.azurewebsites.net/api/"
    private val personsService: PersonsService
    val personLiveData: MutableLiveData<List<Person>> = MutableLiveData<List<Person>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val Build: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        personsService = Build.create(PersonsService::class.java)
    }

    fun getPersonByUserId(user_id:String) {
        personsService.GetByUserId(user_id).enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if (response.isSuccessful) {
                    val b: List<Person>? = response.body()
                    personLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }
            fun initRepository(user_id: String){
                getPersonByUserId(user_id)
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun add(person: Person) {
        personsService.savePersons(person).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Added:" + response.body())
                    updateMessageLiveData.postValue("Added:" + response.body())
                    val UpdatedList = personLiveData.value?.toMutableList()
                    UpdatedList?.add(response.body()!!)
                    personLiveData.postValue(UpdatedList!!)
                    getPersonByUserId(person.userId)
                } else {
                    val message = response.code().toString() + "" + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }


    fun Delete(id: Int) {
        personsService.delete(id).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated:" + response.body())
                    updateMessageLiveData.postValue("Deleted:" + response.body())
                } else {
                    val message = response.code().toString() + "" + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun update(person: Person) {
        personsService.updatePerson(person.id, person).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated:" + response.body())
                    updateMessageLiveData.postValue("Updated" + response.body())
                } else {
                    val message = response.code().toString() + "" + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }


    fun sortByName() {
        personLiveData.value = personLiveData.value?.sortedBy { it.name }
    }

    fun sortByAge() {
        personLiveData.value = personLiveData.value?.sortedBy { it.age }

    }

    fun sortByBirthdayDay() {
        personLiveData.value = personLiveData.value?.sortedBy { it.birthYear }
    }

    fun filterByName(name: String) {
        if (name.isBlank()) {
            getPersonByUserId(name)
        } else {
            personLiveData.value =
                personLiveData.value?.filter { person -> person.name.contains(name) }
        }
    }
}