package com.example.mybirthdayapp.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybirthdayapp.Repository.PersonsRepository
import com.google.firebase.auth.FirebaseAuth

class PersonViewModel : ViewModel() {
    private val repository = PersonsRepository()
    val personsLiveData: LiveData<List<Person>> = repository.personLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        val email = FirebaseAuth.getInstance().currentUser?.email
        email?.let{
            reload(it)
        }
    }

    fun reload(user_id: String) {
        Log.d("APPLE", "Reloading Data for user:$user_id")
        repository.getPersonByUserId(user_id)
    }

    operator fun get(index: Int): Person? {
        return personsLiveData.value?.get(index)
    }
fun getPersonByUserId(user_id: String) {
    repository.getPersonByUserId(user_id)
}
    fun add(person: Person) {
        repository.add(person)
    }

    fun delete(id: Int) {
        repository.Delete(id)
    }

    fun update(person: Person) {
        repository.update(person)
    }

    fun sortByName() {
        repository.sortByName()
    }

    fun sortByAge() {
        repository.sortByAge()
    }

    fun sortByBirthday() {
        repository.sortByBirthdayDay()
    }

    fun filterByName(name: String) {
        repository.filterByName(name)
    }
    private var userEmail: String = ""
    fun getUserEmail(): String {
        return userEmail
    }
    fun setUserEmail(email: String) {
        userEmail = email
    }
}