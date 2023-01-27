package com.example.linkedinanalog.ui.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.data.models.*
import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.data.models.mediaModels.PhotoModel
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.databinding.FragmentCreateBinding
import com.example.linkedinanalog.ui.constans.*
import com.example.linkedinanalog.ui.extensions.loadImage
import com.example.linkedinanalog.ui.recyclerAdapters.userAdapter.UserAdapter
import com.example.linkedinanalog.ui.recyclerAdapters.userAdapter.UserListener
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.EventViewModel
import com.example.linkedinanalog.viewModels.JobViewModel
import com.example.linkedinanalog.viewModels.PostViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding
    private val postViewModel: PostViewModel by activityViewModels()
    private val jobViewModel: JobViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val eventViewModel: EventViewModel by activityViewModels()


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
                binding.postGroup.visibility = View.VISIBLE
                binding.buttonPanel.visibility = View.VISIBLE
                when (requireArguments().getString(JOB_KEY)) {
                    CREATE -> {
                        with(binding) {
                            buttonCreatingComplete.setOnClickListener {
                                val postRequest = PostCreateRequest(
                                    content = textContentPost.text.toString(),
                                    coords = null,
                                    link = textLinkPost.text.toString(),
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
                            textContentPost.setText(post.content)
                            textLinkPost.setText(post.link)
                            if (post.attachment != null) {
                                postViewModel.changePhoto(uri = post.attachment.url?.toUri(), null)
                                imagePost.loadImage(post.attachment.url!!)
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
                                    content = textContentPost.text.toString(),
                                    coords = null,
                                    link = textLinkPost.text.toString(),
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

            JOB_OPEN -> {
                binding.jobGroup.visibility = View.VISIBLE
                binding.buttonPanel.visibility = View.GONE
                when (requireArguments().getString(JOB_KEY)) {
                    CREATE -> {
                        with(binding) {

                            calendarStart.setOnClickListener {
                                showDateView(inputStart)
                            }

                            calendarFinish.setOnClickListener {
                                showDateView(inputFinish)
                            }


                            buttonCreatingComplete.setOnClickListener {
                                val job = JobModel(
                                    id = 0,
                                    name = inputJodName.text.toString(),
                                    position = inputPosition.text.toString(),
                                    start = inputStart.text.toString(),
                                    finish = inputFinish.text.toString(),
                                    link = inputLink.text.toString()
                                )
                                jobViewModel.addJob(job)
                                findNavController().navigateUp()
                            }

                        }

                    }
                }

            }

            EVENT_OPEN -> {
                with(binding) {
                    eventGroup.visibility = View.VISIBLE
                    val adapter = UserAdapter(object : UserListener {
                        override fun clickCheckBox(user: UserModel) {
                            eventViewModel.addToChooseList(user.id)
                        }
                    })
                    usersRecyclerView.adapter = adapter

                    authViewModel.usersLiveData.observe(viewLifecycleOwner) {
                        adapter.submitList(it)
                    }
                }

                when (requireArguments().getString(JOB_KEY)) {
                    CREATE -> {
                        with(binding) {
                            btOnline.setOnClickListener {
                                it.background.alpha = 100
                                btOffline.background.alpha = 0
                            }
                            btOffline.setOnClickListener {
                                it.background.alpha = 100
                                btOnline.background.alpha = 0
                            }

                            imageViewDeleteEvent.setOnClickListener {
                                postViewModel.changePhoto(null, null)
                            }

                            btChooseSpeakers.setOnClickListener {
                                if (usersRecyclerContainer.isVisible) {
                                    btChooseSpeakers.text = "Choose speakers"
                                    usersRecyclerContainer.visibility = View.GONE

                                } else {
                                    btChooseSpeakers.text = "CLOSE"
                                    usersRecyclerContainer.visibility = View.VISIBLE
                                }
                                authViewModel.getAllUsers()
                            }

                            buttonCreatingComplete.setOnClickListener {
                                val isMediaContains =
                                    postViewModel.photoLiveData.value != PhotoModel()
                                val upLoad =
                                    if (isMediaContains) MediaUpload(postViewModel.photoLiveData.value?.file!!) else null
                                val type =
                                    if (btOnline.background.alpha == 100) EventType.ONLINE else
                                        EventType.OFFLINE
                                eventViewModel.createEvent(
                                    EventCreateRequest(
                                        -1, textContentEvent.text.toString(),
                                        dateTime.text.toString(), null, type,
                                        if (isMediaContains) Attachment(
                                            "",
                                            AttachmentType.IMAGE
                                        ) else null,
                                        textLinkEvent.text.toString(),
                                        listOf()
                                    ), mediaUpload = upLoad
                                )
                            }
                        }

                    }
                }


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

            imageViewDeletePost.setOnClickListener {
                postViewModel.changePhoto(null, null)
            }
        }



        postViewModel.photoLiveData.observe(viewLifecycleOwner) {
            if (binding.eventGroup.isVisible) {
                if (it.uri == null) {
                    binding.photoContainerEvent.visibility = View.GONE
                } else {
                    binding.photoContainerEvent.visibility = View.VISIBLE
                    binding.imageEvent.setImageURI(it.uri)
                }
            } else if (binding.postGroup.isVisible) {
                if (it.uri == null) {
                    binding.photoContainerPost.visibility = View.GONE
                } else {
                    binding.photoContainerPost.visibility = View.VISIBLE
                    binding.imagePost.setImageURI(it.uri)

                }
            }

        }




        return binding.root
    }

    private fun showDateView(textInput: TextView) {
        val cal = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val editedMonth = if (month < 10) "0${month + 1}" else "${month + 1}"
                val editedDay = if (dayOfMonth < 10) "0${dayOfMonth + 1}" else "${dayOfMonth + 1}"
                val date = "$year-$editedMonth-$editedDay"
                textInput.text = date
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show()

    }


}