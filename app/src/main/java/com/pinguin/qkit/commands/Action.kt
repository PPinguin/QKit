package com.pinguin.qkit.commands

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.pinguin.qkit.Project
import com.pinguin.qkit.R
import com.pinguin.qkit.fragments.CodeFragment

abstract class Action : Command {
    abstract val type: String
    abstract fun act(canvas: Canvas)

    lateinit var params: Array<String>

    override fun save(): String = "$type:${params.joinToString(separator = "/")}\n"

    fun dialog(context: Context, view: View, lambda: () -> Unit) {
        view.apply {
            findViewById<TextView>(R.id.title).text = type
            findViewById<ImageButton>(R.id.info).setOnClickListener {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/PPinguin/QKit/wiki/${type.capitalize()}")
                    )
                )
            }
        }
        AlertDialog.Builder(context, R.style.CustomDialog)
            .setView(view)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Ок") { _, _ ->
                lambda()
            }
            .setOnDismissListener {
                CodeFragment.listAdapter.notifyItemChanged(
                    Project.commands.indexOf(
                        this
                    )
                )
            }
            .show()
    }
}