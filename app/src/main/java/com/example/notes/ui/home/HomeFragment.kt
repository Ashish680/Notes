package com.example.notes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.notes.R
import com.example.notes.constants.NoteType
import com.example.notes.constants.Status
import com.example.notes.database.DatabaseBuilder
import com.example.notes.database.DatabaseHelperImpl
import com.example.notes.databinding.FragmentHomeBinding
import com.example.notes.models.NoteModel
import com.example.notes.ui.BaseFragment
import com.example.notes.ui.NoteViewModel
import com.example.notes.utils.Alerts
import com.example.notes.utils.CallBacks
import com.example.notes.utils.ViewModelFactory
import com.example.notes.utils.recyclerView.RecyclerTouchListener
import org.apache.commons.collections4.CollectionUtils

class HomeFragment : BaseFragment(), View.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private var noteViewModel: NoteViewModel? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var touchListener: RecyclerTouchListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // observer for view model data set
        observer
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatAdd.setOnClickListener(this)
        noteViewModel?.fetchActiveNoteItems()
    }

    private val observer: Unit
        get() {
            noteViewModel = ViewModelProvider(
                this,
                ViewModelFactory(
                    DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
                )
            )[NoteViewModel::class.java]


            noteViewModel?.getActiveItemList()?.observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.root.visibility = View.GONE
                        it.data?.let { users ->
                            if (CollectionUtils.isNotEmpty(users)) {
                                setAdopterToView(users)
                            }
                        }
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                    Status.LOADING -> {
                        binding.progressBar.root.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        //Handle Error
                        binding.progressBar.root.visibility = View.GONE
                        Alerts.showToast(requireContext(), it.message ?: "")
                    }
                }
            }
        }

    private fun setAdopterToView(users: List<NoteModel?>) {
        val list = users as MutableList<NoteModel?>?
        val cardTypeAdapter =
            list?.let {
                NotesAdopter(it, object : CallBacks.NoteCallBack {
                    override fun onItemClick(position: Int, item: NoteModel) {
                        view?.let {
                            Navigation.findNavController(it)
                                .navigate(
                                    HomeFragmentDirections.actionNavigationHomeToNavigationViewNote(
                                        item.id
                                    )
                                )
                        }
                    }

                    override fun onEditClick(position: Int, item: NoteModel) {
                        view?.let {
                            Navigation.findNavController(it)
                                .navigate(
                                    HomeFragmentDirections.actionNavigationHomeToNavigationAddEdit(
                                        item.id, NoteType.EDITED
                                    )
                                )
                        }
                    }

                    override fun onDeleteClick(position: Int, item: NoteModel) {
                        noteViewModel?.deleteNoteItem(item)
                    }


                })
            }
        binding.recyclerView.adapter = cardTypeAdapter

        touchListener = RecyclerTouchListener(requireActivity(), binding.recyclerView)
        touchListener!!.setClickable(object : RecyclerTouchListener.OnRowClickListener {
            override fun onRowClicked(position: Int) {

            }

            override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
        })
            .setSwipeOptionViews(R.id.delete_task, R.id.edit_task)
            .setSwipeable(
                R.id.rowFG, R.id.rowBG
            ) { viewID, position ->
                when (viewID) {
                    R.id.delete_task -> {
                        //taskList.remove(position)
                        // recyclerviewAdapter.setTaskList(taskList)

                    }
                    R.id.edit_task -> {

                    }
                }
            }
        binding.recyclerView.addOnItemTouchListener(touchListener!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.floatAdd -> {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(
                            HomeFragmentDirections.actionNavigationHomeToNavigationAddEdit(
                                0L, NoteType.NEW
                            )
                        )
                }
            }
        }
    }
}