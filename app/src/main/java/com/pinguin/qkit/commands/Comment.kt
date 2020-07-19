package com.pinguin.qkit.commands

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import com.pinguin.qkit.Project
import com.pinguin.qkit.R
import com.pinguin.qkit.fragments.CodeFragment

class Comment : Command {
    var text: String = ""
    override val resource: Int = R.layout.edit_comment

    override fun save(): String = "comment:$text\n"
    override fun clone(): Command = Comment().also{ it.text = text}

    override fun edit(context: Context) {
        val view = LayoutInflater.from(context).inflate(resource, null)
        view.findViewById<EditText>(R.id.commentText).setText(text)
        AlertDialog.Builder(context, R.style.CustomDialog)
            .setView( view )
            .setPositiveButton("Ок"){ _:DialogInterface, _:Int ->
                text = view.findViewById<EditText>(R.id.commentText).text.toString()
            }
            .setNegativeButton("Отмена", null)
            .setOnDismissListener { CodeFragment.listAdapter.notifyItemChanged(Project.commands.indexOf(this)) }
            .show()
    }
}