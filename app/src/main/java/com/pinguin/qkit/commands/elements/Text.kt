package com.pinguin.qkit.commands.elements

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.pinguin.qkit.*
import com.pinguin.qkit.commands.Command
import com.pinguin.qkit.activities.MainActivity
import com.pinguin.qkit.commands.Element

class Text : Element() {
    override val type: String = "text"
    override val resource: Int = R.layout.edit_text

    init{
        params = Array(5){""}
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.apply{
            color = super.color
            textSize = params[2].toFloat()
            textAlign = Paint.Align.CENTER
            typeface = Typeface.createFromAsset(MainActivity.ASSETS, "fonts/${params[4]}.ttf")
        }
        canvas.drawText(
            params[3],
            params[0].toFloat(),
            params[1].toFloat(),
            paint
        )
    }

    override fun edit(context: Context) {
        val f = arrayListOf("allerta", "great_vibes", "montserrat", "open_sans", "pacifico", "quicksand", "raleway")
        LayoutInflater.from(context).inflate(resource, null).apply {
            findViewById<EditText>(R.id.x).setText(params[0])
            findViewById<EditText>(R.id.y).setText(params[1])
            findViewById<EditText>(R.id.s).setText(params[2])
            findViewById<EditText>(R.id.t).setText(params[3])
            findViewById<Spinner>(R.id.fonts).apply{
                adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, f)
                setSelection(f.indexOf(params[4]))
                onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        params[4] = f[position]
                    }

                }
            }
            super.dialog(context, this) {
                params[0] = findViewById<EditText>(R.id.x).text.toString()
                params[1] = findViewById<EditText>(R.id.y).text.toString()
                params[2] = findViewById<EditText>(R.id.s).text.toString()
                params[3] = findViewById<EditText>(R.id.t).text.toString()
            }
        }
    }

    override fun clone(): Command = Text().also {
        it.params = params.copyOf()
        it.color = color
    }

}