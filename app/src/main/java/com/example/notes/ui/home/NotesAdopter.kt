package com.example.notes.ui.home

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.constants.Constants
import com.example.notes.databinding.ItemNoteBinding
import com.example.notes.databinding.TransparentLayoutBinding
import com.example.notes.models.NoteModel
import com.example.notes.utils.CallBacks
import com.example.notes.utils.Utils
import org.apache.commons.lang3.StringUtils

class NotesAdopter(
    private val users: MutableList<NoteModel?>,
    private val listener: CallBacks.NoteCallBack
) : RecyclerView.Adapter<NotesAdopter.ViewHolder>() {
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        return if (viewType == Constants.TYPE_ITEM) {
            val navigationView: View = LayoutInflater.from(context)
                .inflate(R.layout.item_note, parent, false)
            ViewHolder(navigationView, viewType)
        } else {
            val navHeaderView: View = LayoutInflater.from(context)
                .inflate(R.layout.transparent_layout, parent, false)
            ViewHolder(navHeaderView, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.viewTypeId == Constants.TYPE_ITEM) {
            holder.binding?.item = users[position]
            if (users[position]?.url != null && !StringUtils.isEmpty(users[position]?.url)) {
                holder.binding?.imgNote?.setImageURI(Uri.parse(users[position]?.url))
                holder.binding?.imgNote?.visibility = View.VISIBLE
            } else {
                holder.binding?.imgNote?.visibility = View.GONE
            }
            holder.binding?.txtDate?.text =
                users[position]?.createdAt?.let { Utils.setDateFormat(it) }
            holder.binding?.editTask?.setOnClickListener {
                users[position]?.let { it1 -> listener.onEditClick(position, it1) }
            }
            holder.binding?.rowFG?.setOnClickListener {
                users[position]?.let { it1 -> listener.onItemClick(position, it1) }
            }
            holder.binding?.deleteTask?.setOnClickListener {

                users[position]?.let { it1 -> listener.onDeleteClick(position, it1) }
                users.remove(users[position])
                notifyDataSetChanged()

            }
        }
    }


    override fun getItemCount(): Int {
        return users.size.plus(1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            Constants.TYPE_FOOTER
        } else Constants.TYPE_ITEM
    }

    class ViewHolder(itemLayoutView: View?, ViewType: Int) :
        RecyclerView.ViewHolder(itemLayoutView!!) {
        var viewTypeId = 0
        var binding: ItemNoteBinding? = null
        private var footerLayoutBinding: TransparentLayoutBinding? =
            null

        init {
            if (ViewType == Constants.TYPE_ITEM) {
                binding = DataBindingUtil.bind(itemView)
                viewTypeId = Constants.TYPE_ITEM
            } else if (ViewType == Constants.TYPE_FOOTER) {
                footerLayoutBinding = DataBindingUtil.bind(itemView)
                viewTypeId = Constants.TYPE_FOOTER
            }
        }
    }


}