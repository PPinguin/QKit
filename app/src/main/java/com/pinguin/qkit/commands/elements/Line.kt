package com.pinguin.qkit.commands.elements

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.LayoutInflater
import android.widget.EditText
import com.pinguin.qkit.*
import com.pinguin.qkit.commands.Command
import com.pinguin.qkit.commands.Element

class Line : Element() {
    override val type: String = "line"
    override val resource: Int = R.layout.edit_line

    init{
        params = Array(5){""}
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        paint.strokeWidth = params[4].toFloat()
        canvas.drawLine(
            params[0].toFloat(),
            params[1].toFloat(),
            params[2].toFloat(),
            params[3].toFloat(),
            paint
        )
    }

    override fun edit(context: Context) {
        LayoutInflater.from(context).inflate(resource, null).apply {
            findViewById<EditText>(R.id.x1).setText(params[0])
            findViewById<EditText>(R.id.y1).setText(params[1])
            findViewById<EditText>(R.id.x2).setText(params[2])
            findViewById<EditText>(R.id.y2).setText(params[3])
            findViewById<EditText>(R.id.w).setText(params[4])
            super.dialog(context, this) {
                params[0] = findViewById<EditText>(R.id.x1).text.toString()
                params[1] = findViewById<EditText>(R.id.y1).text.toString()
                params[2] = findViewById<EditText>(R.id.x2).text.toString()
                params[3] = findViewById<EditText>(R.id.y2).text.toString()
                params[4] = findViewById<EditText>(R.id.w).text.toString()
            }
        }
    }

    override fun clone(): Command = Line().also {
        it.params = params.copyOf()
        it.color = color
    }
}