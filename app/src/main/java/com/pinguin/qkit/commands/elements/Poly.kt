package com.pinguin.qkit.commands.elements

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.pinguin.qkit.*
import com.pinguin.qkit.commands.Command
import com.pinguin.qkit.commands.Element
import kotlin.math.*

class Poly : Element() {
    override val type: String = "poly"
    override val resource: Int = R.layout.edit_poly
    var path: Path = Path()

    init{
        params = Array(5){""}
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        if (params[4].isNotEmpty()) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = params[4].toFloat()
        }
        canvas.drawPath(
            path,
            paint
        )
        paint.style = Paint.Style.FILL
    }

    override fun edit(context: Context) {
        val f = arrayOf<Byte>(3, 4, 5, 6, 7, 8)
        LayoutInflater.from(context).inflate(resource, null).apply {
            findViewById<EditText>(R.id.x).setText(params[0])
            findViewById<EditText>(R.id.y).setText(params[1])
            findViewById<EditText>(R.id.w).setText(params[2])
            findViewById<Spinner>(R.id.n).apply{
                adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, f)
                setSelection(f.indexOf(params[3].toByteOrNull() ?: 0))
                onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        params[3] = f[position].toString()
                    }

                }
            }
            findViewById<CheckBox>(R.id.stroke).also{
                it.setOnClickListener { _ ->
                    findViewById<EditText>(R.id.s).visibility = if (it.isChecked) View.VISIBLE else View.GONE
                }
                it.isChecked = params[4].isNotEmpty()
                findViewById<EditText>(R.id.s).visibility = if (it.isChecked) View.VISIBLE else View.GONE
            }
            findViewById<EditText>(R.id.s).setText(params[4])
            super.dialog(context, this) {
                params[0] = findViewById<EditText>(R.id.x).text.toString()
                params[1] = findViewById<EditText>(R.id.y).text.toString()
                params[2] = findViewById<EditText>(R.id.w).text.toString()
                params[4] = if (findViewById<CheckBox>(R.id.stroke).isChecked)
                    findViewById<EditText>(R.id.s).text.toString()
                else ""
                createPath()
            }
        }
    }

    override fun clone(): Command = Poly().also{
        it.params = params.copyOf()
        it.path = path
    }

    fun createPath() {
        var x = params[0].toDouble(); var y = params[1].toDouble()
        var degrees = 0.0
        val n = params[3].toInt()
        val w =  params[2].toDouble()
        path.reset()
        x -= w/2
        y += w/(2 * tan(PI/n))
        path.moveTo(x.toFloat(), y.toFloat())
        for(i in 0 until n-1){
            x += cos(degrees*PI/180)*w
            y -= sin(degrees*PI/180)*w
            degrees += 180-((n-2)*180)/n
            path.lineTo(x.toFloat(),y.toFloat())
        }
        path.close()
    }
}