package com.example.mybirthdayapp

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mybirthdayapp.R
import com.example.mybirthdayapp.databinding.FragmentAddBinding
import com.example.mybirthdayapp.models.Person
import com.example.mybirthdayapp.models.PersonViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val personViewModel: PersonViewModel by activityViewModels()
    private var selectedDate = Calendar.getInstance()
    val auth = FirebaseAuth.getInstance()
    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameEditText = binding.Name
        binding.buttonBirthdate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentYear = calendar[Calendar.YEAR]
            val currentMonth = calendar[Calendar.MONTH]
            val currentDayOfMonth = calendar[Calendar.DAY_OF_MONTH]

            val datePicker = DatePickerDialog(
                requireContext(),
                dateSetListener,
                currentYear,
                currentMonth,
                currentDayOfMonth
            )
            datePicker.show()
        }
        val user_id = auth.currentUser
        binding.buttonAddNewFriend.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val today = Calendar.getInstance()
            val age = today.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < selectedDate?.get(Calendar.DAY_OF_YEAR) ?: 0) 1 else 0

            val userEmail = personViewModel.getUserEmail()
            val birthYear = selectedDate?.get(Calendar.YEAR) ?: 0
            val birthMonth = selectedDate?.get(Calendar.MONTH) ?: 0
            val birthDayOfMonth = selectedDate?.get(Calendar.DAY_OF_MONTH) ?: 0
            val newFriend = Person(name, age, birthYear, birthMonth, birthDayOfMonth, userEmail)
            personViewModel.add(newFriend)
            findNavController().navigate(R.id.action_addFragment_to_SecondFragment)
        }
        binding.buttonPrevious.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}