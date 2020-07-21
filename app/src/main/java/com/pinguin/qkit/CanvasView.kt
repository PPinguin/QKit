package com.pinguin.qkit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.widget.Toast
import com.pinguin.qkit.commands.Action
import com.pinguin.qkit.commands.Element

class CanvasView(context: Context?) : View(context) {

    companion object{
        lateinit var canvas: CanvasView
    }

    init {
        canvas = this
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun setBG(color: Int){
        setBackgroundColor(color)
        if (Project.bgColor != color) Project.bgColor = color
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.scale(width/500f, height/500f)
        var i = 0
        try {
            canvas!!.save()
            Project.commands.forEach {
                i++
                when (it) {
                    is Element -> it.draw(canvas, paint)
                    is Action -> it.act(canvas)
                }
            }
            canvas.restore()
        } catch(e:Exception) {
            Toast.makeText(
                context,
                "Error in line: $i",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
