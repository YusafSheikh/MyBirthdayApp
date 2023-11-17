package com.example.mybirthdayapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mybirthdayapp.R
import com.example.mybirthdayapp.databinding.FragmentFirstBinding
import com.example.mybirthdayapp.models.PersonViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val personViewModel: PersonViewModel by activityViewModels()
    private val handler = Handler(Looper.getMainLooper())
    private val sessionTimeoutInSeconds = 60L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.trim().toString()
            val password = binding.editTextPassword.text.trim().toString()

            if (email.isEmpty()) {
                binding.editTextEmail.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.editTextPassword.error = "Password is required"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d("APPLE", "signInWithEmailAndPassword:success")
                        val user = auth.currentUser
                        val userEmail = user?.email
                        if (userEmail != null) {
                            personViewModel.setUserEmail(userEmail)
                            val savedEmail = userEmail
                            personViewModel.getPersonByUserId(savedEmail)
                            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                        } else {
                            Log.d("APPLE", "User Email is empty or null")
                        }
                    } else {
                        Log.w("APPLE", "signInWithEmailAndPassword:failure", task.exception)
                        binding.textViewRegister.text =
                            "Authentication failed: " + task.exception?.message
                    }
                }
        }

        binding.buttonRegister.setOnClickListener {
            val email = binding.editTextEmail.text.trim().toString()
            val password = binding.editTextPassword.text.trim().toString()

            if (email.isEmpty()) {
                binding.editTextEmail.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.editTextPassword.error = "Password is required"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign up success, update UI with the signed-in user's information
                        Log.d("APPLE", "createUserWithEmailAndPassword:success")
                        val user = auth.currentUser
                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                    } else {
                        // If sign up fails, display a message to the user.
                        Log.w("APPLE", "createUserWithEmailAndPassword:failure", task.exception)
                        binding.textViewRegister.text =
                            "Registration failed: " + task.exception?.message
                    }
                }
        }
    }

    fun extendSession() {
        auth.currentUser?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("APPLE", "Extended session successfully")
                } else {
                    Log.e("APPLE", "Failed to extend session", task.exception)
                }
            }
    }

    fun startSessionTimeout() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            auth.signOut()
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }, sessionTimeoutInSeconds * 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
