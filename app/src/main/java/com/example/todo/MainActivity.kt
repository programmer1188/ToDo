package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.ActivityMainBinding
import org.apache.commons.io.FileUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //remove the item from the list
                listOfTasks.removeAt(position)

                //notify the adapter that item is deleted
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()
        //lookup the recycler view in the layout
        //binding.recyclerView

        //create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        //attach the adapter to the recycler view to populate items
        binding.recyclerView.adapter = adapter

        //set layout manager to position the items
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //set up a button to add a task
        binding.button.setOnClickListener(){
            //grab the input string from the user
            val userInputTask = binding.addTaskField.text.toString()

            //add the string to the list of tasks
            listOfTasks.add(userInputTask)

            //notify the adapter to update the tasks
            adapter.notifyItemInserted(listOfTasks.size-1)

            //reset the text field to enter the new task
            binding.addTaskField.setText("")

            saveItems()
        }
    }

    //save the list tasks

    //create a method to get the list of tasks
    fun getDataFile() : File {

        //every line in the file represents a task
        return File(filesDir,"data.txt")
    }

    //load the items by reading every line
    fun loadItems(){
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //save items
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
                ioException.printStackTrace()
        }
    }
}