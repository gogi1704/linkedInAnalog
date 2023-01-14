package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.databinding.FragmentAuthBinding
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_KEY
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_VALUE_REG
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_VALUE_SIGN_IN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private val viewModel: AuthViewModel by activityViewModels()


    override

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater, container, false)

        if (requireArguments().containsKey(AUTH_BUNDLE_KEY)) {
            when (requireArguments().getString(AUTH_BUNDLE_KEY)) {
                AUTH_BUNDLE_VALUE_REG -> {
                    binding.groupRegister.visibility = View.VISIBLE
                }
                AUTH_BUNDLE_VALUE_SIGN_IN -> {
                    binding.groupRegister.visibility = View.GONE
                }
            }

        }

        with(binding) {
            buttonSignIn.setOnClickListener {
                if (groupRegister.isVisible) {
                    viewModel.registerUser(
                        inputLogin.text.toString().trim(),
                        inputPassword.text.toString().trim(),
                        inputName.text.toString().trim(),
                        null
                    )
                    inputLogin.text
                    findNavController().navigateUp()
                } else {
                    viewModel.authenticationUser(
                        inputLogin.text.toString().trim(),
                        inputPassword.text.toString().trim()
                    )
                    findNavController().navigateUp()
                }
            }

        }




        return binding.root
    }


}