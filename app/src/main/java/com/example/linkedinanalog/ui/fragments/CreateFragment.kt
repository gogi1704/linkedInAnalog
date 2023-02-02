package com.example.linkedinanalog.ui.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.R
import com.example.linkedinanalog.data.models.*
import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.data.media.mediaModels.PhotoModel
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.databinding.FragmentCreateBinding
import com.example.linkedinanalog.exceptions.EventErrorType
import com.example.linkedinanalog.exceptions.JobErrorType
import com.example.linkedinanalog.exceptions.PostErrorType
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

//        val resultA =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val data = result.data?.data
//                    postViewModel.changeAudio(data, data?.toFile())
//                }
//
//            }
//
//        val audioResult =
//            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//                postViewModel.changeAudio(uri, uri?.toFile())
//            }
//
//        val pickRingtone = registerForActivityResult(PickRingtone()) {
//            val data = it
//            postViewModel.changeAudio(data, data?.toFile())
//        }

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
            POST_OPEN -> {
                binding.postGroup.visibility = View.VISIBLE
                binding.buttonPanel.visibility = View.VISIBLE
                when (requireArguments().getString(JOB_KEY)) {
                    CREATE -> {
                        with(binding) {
                            buttonCreatingComplete.setOnClickListener {
                                if (checkCreatingPost()) {
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
                                } else showToast("field with,,*,, must not be empty")
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
                                if (checkCreatingJob()) {
                                    val job = JobModel(
                                        id = 0,
                                        name = inputJobName.text.toString(),
                                        position = inputPosition.text.toString(),
                                        start = inputStart.text.toString(),
                                        finish = inputFinish.text.toString(),
                                        link = inputLink.text.toString()
                                    )
                                    jobViewModel.addJob(job)
                                } else showToast(getString(R.string.fieldWith))
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

                            eventDateCalendar.setOnClickListener {
                                showDateView(textDate)
                            }
                            eventTimeCalendar.setOnClickListener {
                                showTimeView(textTime)
                            }

                            btChooseSpeakers.setOnClickListener {
                                if (usersRecyclerContainer.isVisible) {
                                    btChooseSpeakers.setText(R.string.choose_speakers_hint)
                                    usersRecyclerContainer.visibility = View.GONE

                                } else {
                                    btChooseSpeakers.setText(R.string.close)
                                    usersRecyclerContainer.visibility = View.VISIBLE
                                }
                                authViewModel.getAllUsers()
                            }

                            buttonCreatingComplete.setOnClickListener {
                                if (checkCreatingEvent()) {
                                    val isMediaContains =
                                        postViewModel.photoLiveData.value != PhotoModel()
                                    val upLoad =
                                        if (isMediaContains) MediaUpload(postViewModel.photoLiveData.value?.file!!) else null
                                    val type =
                                        if (btOnline.background.alpha == 100) EventType.ONLINE else
                                            EventType.OFFLINE
                                    val date = "${textDate.text}T${textTime.text}:12.641746Z"
                                    eventViewModel.createEvent(
                                        EventCreateRequest(
                                            -1, textContentEvent.text.toString(),
                                            date, null, type,
                                            if (isMediaContains) Attachment(
                                                "",
                                                AttachmentType.IMAGE
                                            ) else null,
                                            textLinkEvent.text.toString(),
                                            listOf()
                                        ), mediaUpload = upLoad
                                    )
                                } else showToast(getString(R.string.fieldWith))

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

//            addAudio.setOnClickListener {
//                lifecycleScope.launchWhenCreated {
//                    pickRingtone.launch(RingtoneManager.TYPE_RINGTONE)
//                }
//
//
//            }

            imageViewDeletePost.setOnClickListener {
                postViewModel.changePhoto(null, null)
            }
        }

        postViewModel.postErrorStateLiveData.observe(viewLifecycleOwner) {
            when (it.errorType) {
                PostErrorType.AddPostComplete -> {
                    findNavController().navigateUp()
                }
                PostErrorType.AddPostError -> {
                    showToast(getString(R.string.CreatingError))
                }
                PostErrorType.NetworkError -> {
                    showToast(getString(R.string.CheckInternet))
                }
                else -> {}
            }
        }

        eventViewModel.eventErrorStateLiveData.observe(viewLifecycleOwner) {
            when (it.errorType) {
                EventErrorType.CreateComplete -> {
                    findNavController().navigateUp()
                }
                EventErrorType.CreateError -> {
                    showToast(getString(R.string.CreatingError))
                }
                EventErrorType.NetworkError -> {
                    showToast(getString(R.string.CheckInternet))
                }
                else -> {}
            }
        }

        jobViewModel.jobErrorStateLiveData.observe(viewLifecycleOwner) {
            when (it.errorType) {
                JobErrorType.AddJobError -> {
                    showToast(getString(R.string.CreatingError))
                }
                JobErrorType.AddJobComplete -> {
                    showToast(getString(R.string.complete))
                    findNavController().navigateUp()
                }
                JobErrorType.NetworkError -> {
                    showToast(getString(R.string.CheckInternet))
                }
                else -> {}
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

    private fun checkCreatingJob(): Boolean {
        with(binding) {
            return inputJobName.text.toString().isNotEmpty()
                    && inputPosition.text.toString().isNotEmpty()
                    && inputStart.text.toString().isNotEmpty()
        }
    }

    private fun checkCreatingEvent(): Boolean {
        with(binding) {
            return textDate.text.toString().isNotEmpty() && textTime.text.toString().isNotEmpty()
                    && textContentEvent.text.toString().isNotEmpty()
                    && textLinkEvent.text.toString().isNotEmpty()
        }
    }

    private fun checkCreatingPost(): Boolean {
        with(binding) {
            return textContentPost.text.toString()
                .isNotEmpty() && textLinkPost.text.toString().isNotEmpty()
        }
    }


    private fun showDateView(textInput: TextView) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val editedMonth = if (month < 10) "0${month + 1}" else "${month + 1}"
                val editedDay = if (dayOfMonth < 10) "0${dayOfMonth}" else "${dayOfMonth}"
                val date = "$year-$editedMonth-$editedDay"
                textInput.text = date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()


    }

    private fun showTimeView(textInput: TextView) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val time = "$hourOfDay:$minute"
                textInput.text = time
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        ).show()
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }


}


