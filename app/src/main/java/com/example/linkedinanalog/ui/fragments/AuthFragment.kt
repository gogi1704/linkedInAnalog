package com.example.linkedinanalog.ui.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.R
import com.example.linkedinanalog.data.media.mediaModels.PhotoModel
import com.example.linkedinanalog.data.models.user.UserRequestModel
import com.example.linkedinanalog.databinding.FragmentAuthBinding
import com.example.linkedinanalog.exceptions.AuthErrorType
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_KEY
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_VALUE_REG
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_VALUE_SIGN_IN
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

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
                    binding.authTitle.setText(R.string.Registration)
                }
                AUTH_BUNDLE_VALUE_SIGN_IN -> {
                    binding.groupRegister.visibility = View.GONE
                    binding.authTitle.setText(R.string.Sign_in)
                }
            }

        }



        with(binding) {
            buttonSignIn.setOnClickListener {
                if (inputLogin.text.length < 4 && groupRegister.isVisible ||
                    inputPassword.text.length < 4 && groupRegister.isVisible
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_long4Symbols),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (inputPassword.text.toString() != inputRepeatPass.text.toString() && groupRegister.isVisible) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.Passwords_are_different),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val file =
                        if (authViewModel.photoLiveData.value == PhotoModel()) null else authViewModel.photoLiveData.value?.file
                    val userRequest = UserRequestModel(
                        inputLogin.text.toString().trim(), inputPassword.text.toString().trim(),
                        inputName.text.toString().trim(), file
                    )
                    if (groupRegister.isVisible) {
                        authViewModel.registerUser(userRequest)

                    } else {
                        authViewModel.authenticationUser(
                            inputLogin.text.toString().trim(),
                            inputPassword.text.toString().trim()
                        )
                    }
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

        authViewModel.errorStateLiveData.observe(viewLifecycleOwner) {
            when (it.errorType) {
                AuthErrorType.AuthError -> {
                    showToast(getString(R.string.CheckLogPass))
                }
                AuthErrorType.RegisterError -> {
                    showToast(getString(R.string.MaybeNameTaken))
                }
                AuthErrorType.AuthOk -> {
                    findNavController().navigateUp()
                }
                AuthErrorType.NetworkError -> {
                    showToast(getString(R.string.CheckInternet))
                }
                else -> {}
            }
        }




        return binding.root
    }

    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_LONG
        )
            .show()
    }

}