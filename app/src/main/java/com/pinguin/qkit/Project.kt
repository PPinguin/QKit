package com.pinguin.qkit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Environment
import androidx.core.view.drawToBitmap
import com.pinguin.qkit.commands.Action
import com.pinguin.qkit.commands.Command
import com.pinguin.qkit.commands.Comment
import com.pinguin.qkit.commands.Element
import com.pinguin.qkit.commands.actions.*
import com.pinguin.qkit.commands.elements.*
import java.io.*

object Project {
    var name = ""
    val commands = mutableListOf<Command>()
    var bgColor = Color.WHITE
    var path: String? = null

    fun new(context: Context, name: String): Boolean {
        commands.clear()
        bgColor = Color.WHITE
        return try {
            path = context.getExternalFilesDir("Projects")?.absolutePath
            File(path, "$name.qk").also {
                it.createNewFile()
            }.outputStream()
            this.name = name
            save()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun open(name: String): Boolean {
        commands.clear()
        var fis: FileInputStream? = null
        return try {
            fis = File(path, name).inputStream()
            val br = BufferedReader(InputStreamReader(fis))
            bgColor = br.readLine().toInt()
            var s: List<String>
            br.forEachLine {
                s = it.split(":")
                createCommand(s[0], s.subList(1, s.size))
            }
            this.name = name.takeWhile { it != '.' }
            true
        } catch (e: Exception) {
            false
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                }
            }
        }
    }

    fun save(): Boolean {
        if (name.isEmpty()) return false
        var fos: FileOutputStream? = null
        var text = "${bgColor}\n"
        commands.forEach {
            text += it.save()
        }
        return try {
            fos = File(path, "$name.qk").outputStream()
            fos.write(text.toByteArray())
            true
        } catch (e: Exception) {
            false
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                }
            }
        }
    }

    fun expo(context: Context): Boolean {
        if (name == "") return false
        val location =
            if (Build.VERSION.SDK_INT >= 29)
                File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath,
                    "QKit"
                )
            else
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath,
                    "QKit"
                )
        location.mkdir()
        val file = File(location.absolutePath, "$name.png")
        lateinit var bitmap: Bitmap
        CanvasView.canvas.apply {
            layout(left, top, right, bottom)
            bitmap = CanvasView.canvas.drawToBitmap(Bitmap.Config.ARGB_8888)
        }
        var fos: FileOutputStream? = null
        return try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos)
            true
        } catch (e: Exception) {
            false
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                }
            }
        }
    }

    fun rename(name: String) {
        File(path, "${this.name}.qk").renameTo(File(path, "$name.qk"))
        this.name = name
    }

    fun createCommand(type: String, params: List<String>? = null): Boolean {
        when (type) {
            "rect" -> Rect()
            "text" -> Text()
            "oval" -> Oval()
            "line" -> Line()
            "poly" -> Poly()
            "rotate" -> Rotate()
            "scale" -> Scale()
            "reset" -> Reset()
            "move" -> Move()
            "skew" -> Skew()
            else -> Comment()
        }.let {
            if (params != null) {
                when (it) {
                    is Element -> {
                        it.params = params[0].split("/").toTypedArray()
                        it.color = params[1].toInt()
                        if (it is Poly) it.createPath()
                    }
                    is Action -> {
                        it.params = params[0].split("/").toTypedArray()
                    }
                    else -> (it as Comment).text = params[0]
                }
            }
            commands.add(it)
        }
        return true
    }
}