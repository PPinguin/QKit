package com.pinguin.qkit.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pinguin.qkit.*

import java.util.*

class CodeFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var code: LinearLayout
    private lateinit var layout: LinearLayout
    private lateinit var addButton: ImageButton
    private lateinit var recyclerListView: RecyclerView
    private lateinit var touchHelper: ItemTouchHelper
    companion object{
        lateinit var listAdapter: CommandsAdapter
    }

    private val base = arrayOf("comment", "elements", "actions")
    private val e = arrayOf("rect", "line", "oval", "text", "poly")
    private val a = arrayOf("rotate", "scale", "move", "skew", "reset")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::fragmentView.isInitialized) {
            fragmentView = inflater.inflate(R.layout.fragment_code, container, false)

            code = fragmentView.findViewById(R.id.code)
            layout = fragmentView.findViewById(R.id.code_layout)
            addButton = fragmentView.findViewById(R.id.add)
            recyclerListView = fragmentView.findViewById(R.id.list)

            addButton.setOnClickListener {
                AlertDialog.Builder(context, R.style.CustomDialog)
                    .setItems(base) { d, i ->
                        when(i){
                            0 -> {
                                if (!Project.createCommand(base[i]))
                                    Toast.makeText(context, "Не удалось создать комментарий", Toast.LENGTH_SHORT).show()
                                else {
                                    Project.commands.last().edit(context!!)
                                }
                            }
                            1 -> choose(e)
                            2 -> choose(a)
                        }
                        d.dismiss()
                    }
                    .show()
            }

            listAdapter = CommandsAdapter(context!!, Project.commands)
            touchHelper = ItemTouchHelper( object: ItemTouchHelper.SimpleCallback( ItemTouchHelper.DOWN + ItemTouchHelper.UP, 0){
                override fun onMove(
                    recyclerView: RecyclerView,
                    draged: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val posD = draged.adapterPosition
                    val posT = target.adapterPosition
                    Collections.swap(Project.commands, posD, posT)
                    listAdapter.notifyItemMoved(posD, posT)
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {  }
            } )
            touchHelper.attachToRecyclerView(recyclerListView)
            recyclerListView.layoutManager = LinearLayoutManager(context!!)
            recyclerListView.adapter = listAdapter
            recyclerListView.addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                ).apply {
                    setDrawable(resources.getDrawable(R.drawable.divider8, null))
                }
            )
        }
        return fragmentView
    }

    private fun choose(list:Array<String>){
        AlertDialog.Builder(context, R.style.CustomDialog)
            .setItems(list) { d, i ->
                if (!Project.createCommand(list[i]))
                    Toast.makeText(context, "Не удалось создать команду", Toast.LENGTH_SHORT).show()
                else {
                    Project.commands.last().edit(context!!)
                }
                d.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        listAdapter.notifyDataSetChanged()
    }
}
