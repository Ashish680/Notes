package com.example.notes.ui.addedit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.notes.R
import com.example.notes.constants.ActionType
import com.example.notes.constants.NoteType
import com.example.notes.constants.Status
import com.example.notes.database.DatabaseBuilder
import com.example.notes.database.DatabaseHelperImpl
import com.example.notes.databinding.FragmentAddEditBinding
import com.example.notes.models.NoteModel
import com.example.notes.ui.BaseFragment
import com.example.notes.ui.NoteViewModel
import com.example.notes.utils.Alerts
import com.example.notes.utils.ContentViewUtil
import com.example.notes.utils.ViewModelFactory
import org.apache.commons.lang3.StringUtils

class AddEditFragment : BaseFragment(), View.OnClickListener {
    private var noteViewModel: NoteViewModel? = null
    private var _binding: FragmentAddEditBinding? = null
    private var type = NoteType.NEW
    private val binding get() = _binding!!
    private var updateId = 0L
    private var imageUrl: String? = null

    private var isWriteStoragePermission = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditBinding.inflate(inflater, container, false)
        observer
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: AddEditFragmentArgs by navArgs()
        type = args.actionType

        if (type == NoteType.EDITED) {
            binding.toolbarTitle.text = getString(R.string.edit_note)
            binding.btnSubmit.text = getString(R.string.update)
            noteViewModel?.singleNoteById(args.noteId)
        } else {
            binding.toolbarTitle.text = getString(R.string.add_note)
            binding.btnSubmit.text = getString(R.string.submit)
        }

        binding.btnImage.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
    }

    private val observer: Unit
        get() {
            noteViewModel = ViewModelProvider(
                this,
                ViewModelFactory(
                    DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
                )
            )[NoteViewModel::class.java]


            noteViewModel?.getItem()?.observe(this) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {

                            if (it.action == ActionType.ADD || it.action == ActionType.EDIT)
                                it.data?.let { _ ->
                                    requireActivity().onBackPressed()
                                }
                            else if (it.action == ActionType.VIEW)
                                methodSetDataToView(it.data)

                        }
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
                            //Handle Error
                            Alerts.showToast(requireContext(), it.message ?: "")
                        }
                    }
                }
            }
        }

    private fun methodSetDataToView(data: NoteModel?) {
        if (data != null) {
            updateId = data.id
            imageUrl = data.url
            if (imageUrl != null && !StringUtils.isEmpty(imageUrl))
                binding.imgNote.setImageURI(Uri.parse(imageUrl))
            binding.etTitle.setText(data.title)
            binding.etDescription.setText(data.description)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnSubmit -> {
                val title = binding.etTitle.text.toString().trim()
                val desc = binding.etDescription.text.toString().trim()
                if (validation(title, desc)) {
                    val value = NoteModel(
                        id = updateId,
                        title = title,
                        description = desc,
                        noteType = type,
                        url = imageUrl
                    )
                    noteViewModel?.insertNote(value)
                }
            }
            R.id.imgBack -> requireActivity().onBackPressed()
            R.id.btnImage -> {
                requestPermission.launch(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private fun validation(title: String, desc: String): Boolean {
        if (StringUtils.isEmpty(title)) {
            showToastMethod(getString(R.string.enter_title))
            return false
        } else if (StringUtils.isEmpty(desc)) {
            showToastMethod(getString(R.string.enter_desc))
            return false
        }
        return true
    }

    //---------------------Image from gallery-----------------//
    @RequiresApi(Build.VERSION_CODES.M)
    val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // returns Map<String, Boolean> where String represents the
            // permission requested and boolean represents the
            // permission granted or not
            // iterate over each entry of map and take action needed for
            // each permission requested
            permissions.forEach { actionMap ->
                when (actionMap.key) {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        if (actionMap.value) {
                            isWriteStoragePermission = true
                        } else {
                            isWriteStoragePermission = false
                            showToastMethod(getString(R.string.external_storage_denied))
                        }
                    }
                }
            }

            if (isWriteStoragePermission) {
                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/* video/*"
                someActivityResultLauncher.launch(pickIntent)
            }
        }

    private var someActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            if (result != null && result.data != null) {
                imageUrl =
                    ContentViewUtil.getContentFromGallery(requireActivity(), result.data!!)
                if (imageUrl != null)
                    binding.imgNote.setImageURI(Uri.parse(imageUrl))
            }
        }
    }
}