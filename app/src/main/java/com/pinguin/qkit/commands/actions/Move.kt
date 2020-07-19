package com.pinguin.qkit.commands.actions

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.widget.EditText
import com.pinguin.qkit.*
import com.pinguin.qkit.commands.Action
import com.pinguin.qkit.commands.Command

class Move : Action() {
    override val type: String = "move"
    override val resource: Int = R.layout.value_xy

    init{
        params = Array(2){""}
    }

    override fun act(canvas: Canvas) {
        canvas.translate(params[0].toFloat(), params[1].toFloat())
    }

    override fun edit(context: Context) {
        LayoutInflater.from(context).inflate(resource, null).apply {
            findViewById<EditText>(R.id.x).setText(params[0])
            findViewById<EditText>(R.id.y).setText(params[1])
            super.dialog(context, this) {
                params[0] = findViewById<EditText>(R.id.x).text.toString()
                params[1] = findViewById<EditText>(R.id.y).text.toString()
            }
        }
    }

    override fun clone(): Command = Move().also{
        it.params = params.copyOf()
    }
}