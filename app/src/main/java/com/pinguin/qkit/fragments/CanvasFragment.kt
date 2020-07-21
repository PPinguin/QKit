package com.pinguin.qkit.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pinguin.qkit.CanvasView
import com.pinguin.qkit.ColorPicker
import com.pinguin.qkit.Project
import com.pinguin.qkit.R

class CanvasFragment : Fragment() {

    lateinit var canvas: CanvasView
    private lateinit var fragmentView: View
    private lateinit var bgButton: ImageButton
    private lateinit var name: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::fragmentView.isInitialized) {
            fragmentView = inflater.inflate(R.layout.fragment_canvas, container, false)

            canvas = CanvasView(context)
            canvas.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            fragmentView.findViewById<FrameLayout>(R.id.frame).addView(canvas)
            bgButton = fragmentView.findViewById(R.id.bg_button)
            name = fragmentView.findViewById(R.id.nameText)
            bgButton.setOnClickListener {
                ColorPicker.show(context!!, Project.bgColor) { c:Int -> canvas.setBG(c)}
            }
            name.setOnClickListener {
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout_new, null)
                dialogView.findViewById<EditText>(R.id.path).setText(Project.name)
                AlertDialog.Builder(context, R.style.CustomDialog)
                    .setTitle("Переименовать проект")
                    .setView(dialogView)
                    .setPositiveButton("Ок"){_:DialogInterface, _:Int ->
                        Project.rename(dialogView.findViewById<EditText>(R.id.path).text.toString())
                        name.text = Project.name
                    }
                    .setNegativeButton("Отмена", null)
                    .show()
            }
            name.text = Project.name
        }
        return fragmentView
    }

    override fun onResume() {
        super.onResume()
        canvas.setBG(Project.bgColor)
        name.text = Project.name
    }
}
