package com.example.notes.ui.viewnote

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.notes.constants.ActionType
import com.example.notes.constants.Status
import com.example.notes.database.DatabaseBuilder
import com.example.notes.database.DatabaseHelperImpl
import com.example.notes.databinding.FragmentViewNoteBinding
import com.example.notes.models.NoteModel
import com.example.notes.ui.NoteViewModel
import com.example.notes.ui.addedit.AddEditFragmentArgs
import com.example.notes.utils.Alerts
import com.example.notes.utils.Utils
import com.example.notes.utils.ViewModelFactory
import org.apache.commons.lang3.StringUtils

class ViewNoteFragment : Fragment() {
    private var noteViewModel: NoteViewModel? = null
    private var _binding: FragmentViewNoteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewNoteBinding.inflate(inflater, container, false)
        observer
        return binding.root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBack.setOnClickListener { requireActivity().onBackPressed() }
        val args: ViewNoteFragmentArgs by navArgs()
        noteViewModel?.singleNoteById(args.noteId)
    }

    private fun methodSetDataToView(data: NoteModel?) {
        if (data != null) {
            binding.item = data
            if (data.url != null && !StringUtils.isEmpty(data.url))
                binding.imgNote.setImageURI(Uri.parse(data.url))
            binding.txtTitle.text = data.title
            binding.txtDescription.text = data.description
            binding.txtDate.text = data.createdAt.let { Utils.setDateFormat(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}