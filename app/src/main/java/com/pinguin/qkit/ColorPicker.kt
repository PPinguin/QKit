package com.pinguin.qkit

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment

object ColorPicker {
    private var color = Color.WHITE
    private var r = 0
    private var g = 0
    private var b = 0
    private var a = 0

    fun show(context: Context, c: Int, lambda: (Int) -> Unit) {
        val view = LayoutInflater.from(context).inflate(R.layout.edit_color, null)
        view.apply {
            findViewById<SeekBar>(R.id.a).setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    a = progress
                    findViewById<TextView>(R.id.a_t).text = a.toString()
                    (findViewById<View>(R.id.color).background as GradientDrawable).setColor(
                        Color.argb(
                            a,
                            r,
                            g,
                            b
                        )
                    )
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            findViewById<SeekBar>(R.id.r).setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    r = progress
                    findViewById<TextView>(R.id.r_t).text = r.toString()
                    (findViewById<View>(R.id.color).background as GradientDrawable).setColor(
                        Color.argb(
                            a,
                            r,
                            g,
                            b
                        )
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            findViewById<SeekBar>(R.id.g).setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    g = progress
                    findViewById<TextView>(R.id.g_t).text = g.toString()
                    (findViewById<View>(R.id.color).background as GradientDrawable).setColor(
                        Color.argb(
                            a,
                            r,
                            g,
                            b
                        )
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            findViewById<SeekBar>(R.id.b).setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    b = progress
                    findViewById<TextView>(R.id.b_t).text = b.toString()
                    (findViewById<View>(R.id.color).background as GradientDrawable).setColor(
                        Color.argb(
                            a,
                            r,
                            g,
                            b
                        )
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            findViewById<SeekBar>(R.id.a).progress = Color.alpha(c); a = Color.alpha(c)
            findViewById<SeekBar>(R.id.r).progress = Color.red(c); r = Color.red(c)
            findViewById<SeekBar>(R.id.g).progress = Color.green(c); g = Color.green(c)
            findViewById<SeekBar>(R.id.b).progress = Color.blue(c); b = Color.blue(c)
            findViewById<TextView>(R.id.a_t).text = Color.alpha(c).toString()
            findViewById<TextView>(R.id.r_t).text = Color.red(c).toString()
            findViewById<TextView>(R.id.g_t).text = Color.green(c).toString()
            findViewById<TextView>(R.id.b_t).text = Color.blue(c).toString()
            (findViewById<View>(R.id.color).background as GradientDrawable).setColor(
                Color.argb(
                    a,
                    r,
                    g,
                    b
                )
            )
        }
        AlertDialog.Builder(context, R.style.CustomDialog)
            .setView(view)
            .setPositiveButton("Ок") { _, _ ->
                view.apply {
                    color = Color.argb(a, r, g, b)
                    lambda(color)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}