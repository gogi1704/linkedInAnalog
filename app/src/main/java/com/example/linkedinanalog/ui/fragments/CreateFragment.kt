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
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.AttachmentType
import com.example.linkedinanalog.data.models.mediaModels.PhotoModel
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.databinding.FragmentCreateBinding
import com.example.linkedinanalog.ui.constans.*
import com.example.linkedinanalog.ui.extensions.loadImage
import com.example.linkedinanalog.viewModels.PostViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding
    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(layoutInflater, container, false)

        val pickPhotoLauncher =
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
                        postViewModel.changePhoto(uri, uri?.toFile())
                    }
                }
            }

        when (requireArguments().getString(OPEN_FRAGMENT_KEY)) {
            //todo
            POST_OPEN -> {
                when (requireArguments().getString(JOB_KEY)) {
                    CREATE -> {
                        with(binding) {
                            buttonCreatingComplete.setOnClickListener {
                                val postRequest = PostCreateRequest(
                                    content = textContent.text.toString(),
                                    coords = null,
                                    link = textLink.text.toString(),
                                    attachment = if (postViewModel.photoLiveData.value != PhotoModel()) Attachment(
                                        "",
                                        AttachmentType.IMAGE
                                    ) else null,
                                    mentionIds = listOf()
                                )
                                postViewModel.addPost(postRequest)
                                findNavController().navigateUp()
                            }
                        }
                    }
                    UPDATE -> {
                        val post = Gson().fromJson(
                            requireArguments().getString(ITEM_KEY),
                            PostCreateRequest::class.java
                        )
                        with(binding) {
                            textContent.setText(post.content)
                            textLink.setText(post.link)
                            if (post.attachment != null) {
                                postViewModel.changePhoto(uri = post.attachment.url?.toUri(), null)
                                image.loadImage(post.attachment.url!!)
                            }

                            buttonCreatingComplete.setOnClickListener {
                                var attachment = Attachment(null, null)
                                if (post.attachment?.url == postViewModel.photoLiveData.value?.uri.toString()) {
                                    attachment = post.attachment
                                } else if (postViewModel.photoLiveData.value != PhotoModel()) {
                                    attachment = Attachment(
                                        null,
                                        AttachmentType.IMAGE
                                    )
                                }
                                val postRequest = PostCreateRequest(
                                    id = post.id,
                                    content = textContent.text.toString(),
                                    coords = null,
                                    link = textLink.text.toString(),
                                    attachment = if (postViewModel.photoLiveData.value == PhotoModel()) null else attachment,
                                    mentionIds = listOf()
                                )
                                postViewModel.addPost(postRequest)
                                findNavController().navigateUp()
                            }

                        }
                    }
                }
            }

            EVENT_OPEN -> {


            }


        }

        with(binding) {
            takeImage.setOnClickListener {
                ImagePicker.with(this@CreateFragment)
                    .crop()
                    .compress(2048)
                    .provider(ImageProvider.GALLERY)
                    .galleryMimeTypes(
                        arrayOf(
                            "image/png",
                            "image/jpeg",
                        )
                    )
                    .createIntent { pickPhotoLauncher.launch(it) }
            }

            createImage.setOnClickListener {
                ImagePicker.with(this@CreateFragment)
                    .crop()
                    .compress(2048)
                    .provider(ImageProvider.CAMERA)
                    .createIntent { pickPhotoLauncher.launch(it) }
            }

            imageViewDelete.setOnClickListener {
                postViewModel.changePhoto(null, null)
            }
        }



        postViewModel.photoLiveData.observe(viewLifecycleOwner) {
            if (it.uri == null) {
                binding.photoContainer.visibility = View.GONE
            } else
                binding.photoContainer.visibility = View.VISIBLE
            binding.image.setImageURI(it.uri)
        }




        return binding.root
    }


}