package com.pinguin.qkit.commands

import android.content.Context

interface Command{
    val resource: Int

    fun save():String
    fun clone(): Command
    fun edit(context: Context)
}