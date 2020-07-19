package com.pinguin.qkit.commands.actions

import android.content.Context
import android.graphics.Canvas
import com.pinguin.qkit.commands.Command
import com.pinguin.qkit.Project
import com.pinguin.qkit.commands.Action
import com.pinguin.qkit.fragments.CodeFragment

class Reset : Action() {
    override val type: String = "reset"
    override val resource: Int = 0

    override fun act(canvas: Canvas) {
        canvas.restore()
    }

    override fun clone(): Command = Reset()

    override fun edit(context: Context) {
        CodeFragment.listAdapter.notifyItemChanged(Project.commands.indexOf(this))
    }
}