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

class Oval: Element() {
    override val type: String = "oval"
    override val resource: Int = R.layout.edit_oval

    init{
        params = Array(8){""}
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        if (params[7].isNotEmpty()) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = params[7].toFloat()
        }
        if (params[4].isEmpty() || params[5].isEmpty()){
            if (params[2] == params[3]){
                canvas.drawCircle(
                    params[0].toFloat(),
                    params[1].toFloat(),
                    params[2].toFloat()/2,
                    paint
                )
            } else {
                canvas.drawOval(
                    params[0].toFloat() - params[2].toFloat()/2,
                    params[1].toFloat() - params[3].toFloat()/2,
                    params[0].toFloat() + params[2].toFloat()/2,
                    params[1].toFloat() + params[3].toFloat()/2,
                    paint
                )
            }
        } else {
            canvas.drawArc(
                params[0].toFloat() - params[2].toFloat() / 2,
                params[1].toFloat() - params[3].toFloat() / 2,
                params[0].toFloat() + params[2].toFloat() / 2,
                params[1].toFloat() + params[3].toFloat() / 2,
                params[4].toFloat(),
                params[5].toFloat(),
                params[6].toBoolean(),
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
            findViewById<EditText>(R.id.b).setText(params[4])
            findViewById<EditText>(R.id.a).setText(params[5])
            findViewById<CheckBox>(R.id.center).isChecked = params[6].toBoolean()
            findViewById<CheckBox>(R.id.stroke).also{
                it.setOnClickListener {v ->
                    findViewById<EditText>(R.id.s).visibility = if (it.isChecked) View.VISIBLE else View.GONE
                }
                it.isChecked = params[7].isNotEmpty()
                findViewById<EditText>(R.id.s).visibility = if (it.isChecked) View.VISIBLE else View.GONE
            }
            findViewById<EditText>(R.id.s).setText(params[7])
            super.dialog(context, this) {
                params[0] = findViewById<EditText>(R.id.x).text.toString()
                params[1] = findViewById<EditText>(R.id.y).text.toString()
                params[2] = findViewById<EditText>(R.id.w).text.toString()
                params[3] = findViewById<EditText>(R.id.h).text.toString()
                params[4] = findViewById<EditText>(R.id.b).text.toString()
                params[5] = findViewById<EditText>(R.id.a).text.toString()
                params[6] = findViewById<CheckBox>(R.id.center).isChecked.toString()
                params[7] = if (findViewById<CheckBox>(R.id.stroke).isChecked)
                    findViewById<EditText>(R.id.s).text.toString()
                else ""
            }
        }
    }

    override fun clone(): Command = Oval().also {
        it.params = params.copyOf()
        it.color = color
    }
}