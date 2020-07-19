package com.pinguin.qkit.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.AssetManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pinguin.qkit.Project
import com.pinguin.qkit.R
import com.pinguin.qkit.fragments.CanvasFragment
import com.pinguin.qkit.fragments.CodeFragment
import java.io.*

class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var ASSETS: AssetManager
    }
    private lateinit var toolbar: Toolbar
    private lateinit var button: FloatingActionButton
    private var isCode = false
    private val codeFragment = CodeFragment()
    private val canvasFragment = CanvasFragment()
    private val fv = R.id.fragmentView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ASSETS = assets
        Project.path = getExternalFilesDir("Projects")?.absolutePath // path to projects
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        button = findViewById(R.id.button)
        button.setOnClickListener { changeFragment() }
        new()
    }

    private fun changeFragment() {
        if (Project.name == "") return
        isCode = !isCode
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                if (isCode) R.anim.translation_out else R.anim.fade_in,
                if (isCode) R.anim.fade_out else R.anim.translation_in
            )
            .replace(R.id.fragmentView, if (isCode) codeFragment else canvasFragment)
            .addToBackStack(null)
            .commit()
        button.setImageResource(if (isCode) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_ -> { new() }
            R.id.open -> {
                supportFragmentManager.beginTransaction().remove(canvasFragment).commit()
                open()
            }
            R.id.save -> {
                if (Project.save())
                    Toast.makeText(this, "Проект сохранен", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Неудалось сохранить проект", Toast.LENGTH_SHORT).show()
            }
            R.id.expo -> {
                if (Project.expo(this))
                    Toast.makeText(this, "Изображение экспортировано", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Неудалось экспортировать изображение", Toast.LENGTH_SHORT)
                        .show()
            }
            R.id.help -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/PPinguin/QKit/wiki")
                    )
                )
            }
        }
        return true
    }

    override fun onStop() {
        Project.save()
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        changeFragment()
    }

    private fun new(){
        supportFragmentManager.beginTransaction().remove(canvasFragment).commit()
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout_new, null)
        AlertDialog.Builder(this, R.style.CustomDialog)
            .setTitle("Новый проект")
            .setView(dialogView)
            .setPositiveButton("Создать") { _: DialogInterface, _: Int ->
                if (Project.new(this, dialogView.findViewById<EditText>(R.id.path).text.toString()))
                    supportFragmentManager.beginTransaction().replace(fv, canvasFragment).commit()
                else
                    Toast.makeText(this, "Не удалось создать проект", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Выйти") { _: DialogInterface, _: Int ->
                finish()
            }
            .setNeutralButton("Открыть") { _: DialogInterface, _: Int ->
                open()
            }
            .show()
    }

    private fun open(){
        var choice = ""
        val files = (getExternalFilesDir("Projects")?.list() ?: arrayOf()).toList().toTypedArray()
        AlertDialog.Builder(this, R.style.CustomDialog)
            .setSingleChoiceItems(
                files,
                -1
            ) { _: DialogInterface, i: Int ->
                choice = files[i]
            }
            .setPositiveButton("Открыть") { _: DialogInterface, _: Int -> // Opening project
                if (Project.open(choice))
                    supportFragmentManager.beginTransaction().replace(fv, canvasFragment).commit()
                else
                    Toast.makeText(this, "Неудалось загрузить проект", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .setNeutralButton("Удалить") { _: DialogInterface, _: Int ->
                if (File("${getExternalFilesDir("Projects")?.absolutePath}/$choice").delete())
                    Toast.makeText(this, "Проект удален", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Неудалось удалить проект", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}