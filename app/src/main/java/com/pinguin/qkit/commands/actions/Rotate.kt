package com.pinguin.qkit.commands.actions

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.widget.EditText
import com.pinguin.qkit.*
import com.pinguin.qkit.commands.Action
import com.pinguin.qkit.commands.Command

class Rotate : Action() {
    override val type: String = "rotate"
    override val resource: Int = R.layout.value_rotate

    init{
        params = Array(3){""}
    }

    override fun act(canvas: Canvas) {
        canvas.rotate(params[0].toFloatOrNull() ?: 250f, params[1].toFloatOrNull() ?: 250f, params[2].toFloatOrNull() ?: 250f)
    }

    override fun edit(context: Context) {
        LayoutInflater.from(context).inflate(resource, null).apply {
            findViewById<EditText>(R.id.a).setText(params[0])
            findViewById<EditText>(R.id.x).setText(params[1])
            findViewById<EditText>(R.id.y).setText(params[2])
            super.dialog(context, this) {
                params[0] = findViewById<EditText>(R.id.a).text.toString()
                params[1] = findViewById<EditText>(R.id.x).text.toString()
                params[2] = findViewById<EditText>(R.id.y).text.toString()
            }
        }
    }

    override fun clone(): Command = Rotate().also{
        it.params = params.copyOf()
    }
}