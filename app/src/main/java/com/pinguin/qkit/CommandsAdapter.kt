package com.pinguin.qkit

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pinguin.qkit.commands.Action
import com.pinguin.qkit.commands.Command
import com.pinguin.qkit.commands.Comment
import com.pinguin.qkit.commands.Element

class CommandsAdapter(val context: Context, private val list: MutableList<Command>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    open inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.findViewById(R.id.content)
        private val btn: ImageButton = view.findViewById(R.id.menu)
        private val menu =
            PopupMenu(context, view.findViewById(R.id.menu), R.style.CustomMenu).apply {
                inflate(R.menu.element_menu)
            }

        init {
            content.setOnClickListener {
                list[adapterPosition].edit(context)
            }
            btn.setOnClickListener { menu.show() }
            menu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.copy -> {
                        Project.commands.add(list[adapterPosition].clone())
                        notifyItemInserted(list.lastIndex)
                        true
                    }
                    R.id.delete -> {
                        list.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    inner class ElementViewHolder(view: View) : Holder(view) {
        private val colorStroke: View = view.findViewById(R.id.color)

        init {
            colorStroke.setOnClickListener {
                ColorPicker.show(context, (list[adapterPosition] as Element).color) { c: Int ->
                    (list[adapterPosition] as Element).color = c
                    notifyItemChanged(adapterPosition)
                }
            }
        }

        fun bind(element: Element) {
            val s = "${element.type} : ${element.params.joinToString()}"
            content.text = s
            (colorStroke.background as GradientDrawable).setColor(element.color)
        }
    }


    inner class CommentViewHolder(view: View) : Holder(view) {
        fun bind(comment: Comment) {
            content.text = comment.text
        }
    }

    inner class ActionViewHolder(view: View) : Holder(view) {
        fun bind(action: Action) {
            val s = "${action.type} : ${action.params.joinToString()}"
            content.text = s
        }
    }

    override fun getItemViewType(position: Int): Int = when (list[position]) {
        is Element -> 0
        is Action -> 1
        else -> 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                ElementViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.element, parent, false)
                )
            }
            1 -> {
                ActionViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.action, parent, false)
                )
            }
            else -> {
                CommentViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.comment, parent, false)
                )
            }
        }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as ElementViewHolder).bind((list[position] as Element))
            1 -> (holder as ActionViewHolder).bind(list[position] as Action)
            else -> (holder as CommentViewHolder).bind((list[position] as Comment))
        }
    }
}
