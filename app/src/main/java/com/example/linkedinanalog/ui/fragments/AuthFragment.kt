package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.R
import com.example.linkedinanalog.databinding.FragmentAuthBinding
import com.example.linkedinanalog.viewModels.AuthViewModel


class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private val viewModel: AuthViewModel by activityViewModels()

    override

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater, container, false)

        with(binding) {
            buttonSignIn.setOnClickListener {
                viewModel.registerUser(
                    inputLogin.text.toString(),
                    inputPassword.text.toString(),
                    inputName.text.toString(),
                    null
                )
            }

        }




        return binding.root
    }


}