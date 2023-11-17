package com.example.mybirthdayapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mybirthdayapp.R
import com.example.mybirthdayapp.databinding.FragmentEditBinding
import com.example.mybirthdayapp.models.Person
import com.example.mybirthdayapp.models.PersonViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val personViewModel: PersonViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = requireArguments()
        val modifyFragmentArgs: EditFragmentArgs = EditFragmentArgs.fromBundle(bundle)
        val position = modifyFragmentArgs.position
        val person = personViewModel[position]

        if (person == null) {
            binding.textviewMessage.text = "No such Friend!"
            return
        }

        binding.Name.setText(person.name)
        //binding.editTextAge.setText(person.age.toString())
        binding.birthDate.setText(person.birthDayOfMonth.toString())
        binding.birthMonth.setText(person.birthMonth.toString())
        binding.birthYear.setText(person.birthYear.toString())
        binding.eMail.setText(person.userId.toString())

        binding.buttonDeleteFriend.setOnClickListener {
            val user_id = FirebaseAuth.getInstance().currentUser?.email
            if (user_id != null) {
                personViewModel.delete(person.id)
                personViewModel.reload(user_id)
            }
            findNavController().navigate(R.id.action_editFragment_to_SecondFragment)
        }

        binding.buttonPrevious.setOnClickListener {
            findNavController().navigate(R.id.action_editFragment_to_SecondFragment)
        }


        binding.buttonEditNewFriend.setOnClickListener {
            val name = binding.Name.text.toString().trim()
            if (name == null) {
                binding.Name.error = "Name field is Empty"
            }
            val birthYear = binding.birthYear.text.toString().trim().toInt()
            val birthMonth = binding.birthMonth.text.toString().trim().toInt()
            val birthDayOfMonth = binding.birthDate.text.toString().trim().toInt()
            val Email = binding.eMail.text.toString().trim()
            val updatePerson =
                Person(
                    person.id,
                    name,
                    person.age,
                    birthYear,
                    birthMonth,
                    birthDayOfMonth,
                    Email
                )
            personViewModel.update(updatePerson)
            val user_id = FirebaseAuth.getInstance().currentUser?.email
            if (user_id != null) {
                personViewModel.reload(user_id)
            }
            findNavController().navigate(R.id.action_editFragment_to_SecondFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
