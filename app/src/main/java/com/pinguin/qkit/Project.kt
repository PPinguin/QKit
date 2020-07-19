package com.pinguin.qkit

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.view.drawToBitmap
import com.pinguin.qkit.commands.*
import com.pinguin.qkit.commands.actions.*
import com.pinguin.qkit.commands.elements.*
import com.pinguin.qkit.fragments.CanvasFragment
import java.io.*

object Project {
    var name = ""
    val commands = mutableListOf<Command>()
    var bgColor = Color.WHITE
    var path:String? = null

    fun new(context: Context, name: String): Boolean {
        commands.clear()
        return try {
            path = context.getExternalFilesDir("Projects")?.absolutePath
            File(path, "$name.qk").also {
                it.createNewFile()
            }.outputStream()
            this.name = name
            true
        } catch (e: Exception) {
            Log.e("creating", e.message!!)
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
            Log.e("OPENED", e.message ?: "!!!")
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
            Log.e("SAVED", e.message ?: "!!!")
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
        val location = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath, "QKit")
        location.mkdir()
        val file = File(location.absolutePath, "$name.png")
        lateinit var bitmap:Bitmap
        CanvasFragment.canvas.apply {
            layout(left, top, right, bottom)
            isDrawingCacheEnabled = true
            bitmap = CanvasFragment.canvas.drawToBitmap(Bitmap.Config.ARGB_8888)
        }
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos)
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                mediaScanIntent.data = Uri.fromFile(file)
                context.sendBroadcast(mediaScanIntent)
            }
            return true
        } catch (e: Exception) {
            Log.e("EXPO", e.message!!)
            return false
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

    fun createCommand(type:String, params: List<String>? = null):Boolean{
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
        }.let{
            if(params != null) {
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