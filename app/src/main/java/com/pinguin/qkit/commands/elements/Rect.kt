package com.pinguin.qkit.commands.elements

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.pinguin.qkit.*
import com.pinguin.qkit.commands.Command
import com.pinguin.qkit.commands.Element

class Rect: Element() {
    override val type = "rect"
    override val resource: Int = R.layout.edit_rect

    init{
        params = Array(6){""}
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        if (params[5].isNotEmpty()) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = params[5].toFloat()
        }
        if (params[4].isEmpty()){
            canvas.drawRect(
                params[0].toFloat() - params[2].toFloat()/2,
                params[1].toFloat() - params[3].toFloat()/2,
                params[0].toFloat() + params[2].toFloat()/2,
                params[1].toFloat() + params[3].toFloat()/2,
                paint
            )
        } else {
            canvas.drawRoundRect(
                params[0].toFloat() - params[2].toFloat() / 2,
                params[1].toFloat() - params[3].toFloat() / 2,
                params[0].toFloat() + params[2].toFloat() / 2,
                params[1].toFloat() + params[3].toFloat() / 2,
                params[4].toFloat(),
                params[4].toFloat(),
                paint
            )
        }
        paint.style = Paint.Style.FILL
    }

    override fun edit(context: Context) {
        LayoutInflater.from(context).inflate(resource, null).apply {
            findViewById<EditText>(R.id.x).setText(params[0])
            findViewById<EditText>(R.id.y).setText(params[1])
            findViewById<EditText>(R.id.w).setText(params[2])
            findViewById<EditText>(R.id.h).setText(params[3])
            findViewById<EditText>(R.id.a).setText(params[4])
            findViewById<CheckBox>(R.id.stroke).also{
                it.setOnClickListener { _ ->
                    findViewById<EditText>(R.id.s).visibility = if (it.isChecked) View.VISIBLE else View.GONE
                }
                it.isChecked = params[5].isNotEmpty()
                findViewById<EditText>(R.id.s).visibility = if (it.isChecked) View.VISIBLE else View.GONE
            }
            findViewById<EditText>(R.id.s).setText(params[5])
            super.dialog(context, this) {
                params[0] = findViewById<EditText>(R.id.x).text.toString()
                params[1] = findViewById<EditText>(R.id.y).text.toString()
                params[2] = findViewById<EditText>(R.id.w).text.toString()
                params[3] = findViewById<EditText>(R.id.h).text.toString()
                params[4] = findViewById<EditText>(R.id.a).text.toString()
                params[5] = if (findViewById<CheckBox>(R.id.stroke).isChecked)
                    findViewById<EditText>(R.id.s).text.toString()
                else ""
            }
        }
    }

    override fun clone(): Command = Rect().also {
        it.params = params.copyOf()
        it.color = color
    }
}