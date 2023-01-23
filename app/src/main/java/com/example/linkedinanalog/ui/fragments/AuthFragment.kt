package com.example.linkedinanalog.ui.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.data.models.mediaModels.PhotoModel
import com.example.linkedinanalog.data.models.user.UserRequestModel
import com.example.linkedinanalog.databinding.FragmentAuthBinding
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_KEY
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_VALUE_REG
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_VALUE_SIGN_IN
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    private val pickPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                ImagePicker.RESULT_ERROR -> {
                    Snackbar.make(
                        binding.root,
                        ImagePicker.getError(it.data),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                Activity.RESULT_OK -> {
                    val uri: Uri? = it.data?.data
                    authViewModel.changePhoto(uri, uri?.toFile())
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater, container, false)

        if (requireArguments().containsKey(AUTH_BUNDLE_KEY)) {
            when (requireArguments().getString(AUTH_BUNDLE_KEY)) {
                AUTH_BUNDLE_VALUE_REG -> {
                    binding.groupRegister.visibility = View.VISIBLE
                    binding.authTitle.text = "Registration"
                }
                AUTH_BUNDLE_VALUE_SIGN_IN -> {
                    binding.groupRegister.visibility = View.GONE
                    binding.authTitle.text = "Sign in"
                }
            }

        }



        with(binding) {
            buttonSignIn.setOnClickListener {
                val file =
                    if (authViewModel.photoLiveData.value == PhotoModel()) null else authViewModel.photoLiveData.value?.file
                val userRequest = UserRequestModel(
                    inputLogin.text.toString().trim(), inputPassword.text.toString().trim(),
                    inputName.text.toString().trim(), file
                )
                if (groupRegister.isVisible) {
                    authViewModel.registerUser(userRequest)

                    findNavController().navigateUp()
                } else {
                    authViewModel.authenticationUser(
                        inputLogin.text.toString().trim(),
                        inputPassword.text.toString().trim()
                    )
                    findNavController().navigateUp()
                }
            }

            addPhotoAvatar.setOnClickListener {
                ImagePicker.with(this@AuthFragment)
                    .crop()
                    .compress(2048)
                    .provider(ImageProvider.CAMERA)
                    .createIntent { pickPhotoLauncher.launch(it) }
            }

            addImageAvatar.setOnClickListener {
                ImagePicker.with(this@AuthFragment)
                    .crop()
                    .compress(2048)
                    .provider(ImageProvider.GALLERY)
                    .createIntent { pickPhotoLauncher.launch(it) }
            }

            deleteImage.setOnClickListener {
                authViewModel.changePhoto(null, null)
            }


        }

        authViewModel.photoLiveData.observe(viewLifecycleOwner) {
            binding.imageAvatar.setImageURI(it.uri)


        }




        return binding.root
    }


}