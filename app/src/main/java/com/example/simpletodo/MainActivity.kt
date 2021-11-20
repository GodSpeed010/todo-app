package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class MainActivity : AppCompatActivity() {

    val KEY_ITEM_TEXT: String = "item_text"
    val KEY_ITEM_POSITION: String = "item position"
    val EDIT_TEXT_CODE: Int = 20

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadItems()

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //stores the task to be deleted in case user wants to undo
                val deletedTask = listOfTasks.elementAt(position)

                //remove the item from the list
                listOfTasks.removeAt(position)
                //notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                //shows a snackbar that informs the user they deleted a task and has an undo button
                showUndoSnackbar(deletedTask, position)

                saveItems()
            }

        }

        val onClickListener = object: TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int) {
                //create the new activity
                val i = Intent(this@MainActivity, EditActivity::class.java)

                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, listOfTasks.get(position))
                i.putExtra(KEY_ITEM_POSITION, position)

                //display the activity
                startActivityForResult(i, EDIT_TEXT_CODE)
            }
        }
        //look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)
        //Attach the adapter to the recyclerView to populate items
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Setup the button and input field, so that the user can enter a task and add it

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Get a reference to the button
        // and then set an onclicklistener
        findViewById<Button>(R.id.button).setOnClickListener {
            //1. Grab the text the user entered into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()
			
			//if task is not an empty String
			if (userInputtedTask.isNotEmpty()) {

                //2. Add the String to the listOfTasks
                listOfTasks.add(userInputtedTask)

                //Notify the adapter that our data has been updated
                adapter.notifyItemInserted(listOfTasks.size - 1)

                //3. Reset the text field
                inputTextField.setText("")

                saveItems()
			}
        }
    }

    //handle the result of the edit activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the updated text value
            val itemText: String? = data?.getStringExtra(KEY_ITEM_TEXT)

            //extract the original position of the edited item from the position key
            val position: Int? = data?.getIntExtra(KEY_ITEM_POSITION, 0)

            //update the model at the right position with new item text
            if (position != null) {
                if (itemText != null) {
                    listOfTasks.set(position, itemText)
                }
            }

            //notify the adapter
            if (position != null) {
                adapter.notifyItemChanged(position)
            }

            //save the changes
            saveItems()

            //show confirmation Toast
            Toast.makeText(applicationContext, "Item updated successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Log.w("my_tag", "Unknown call to onActivityResult")
        }
//        val editedText: String = intent.getStringExtra(EditActivity().KEY_ITEM_TEXT)
//
//        adapter.notifyDataSetChanged()
//
//        saveItems()
    }
    //Save the data that the user has inputted
    //Save data by writing and reading from a file

    //Get the file we need
    fun getDataFile(): File {

        //Every line is going te represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
     }

    //Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }


    //Save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }

    private fun showUndoSnackbar(deletedTask: String, position: Int): Boolean {
        val deletedText = "Deleted a task"
        val deletedTaskSnackbar = Snackbar.make(
            findViewById(R.id.constraint_Layout),
            deletedText,
            Snackbar.LENGTH_SHORT)

        //create the undo button
        deletedTaskSnackbar.setAction("UNDO", View.OnClickListener {
            //add the task back into its original position
            listOfTasks.add(position, deletedTask)

            //notify the adapter to display the change on screen
            adapter.notifyDataSetChanged()

            //save the list
            saveItems()
        })

        //show the SnackBar
        deletedTaskSnackbar.show()


        return true
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        when (item.getItemId()) {
            R.id.miTheme -> {
                Log.d("my_tag", "Theme button pressed")
                //TODO launch ThemeActivity
                //create the new activity
                val i = Intent(this@MainActivity, ThemeActivity::class.java)

                //launch the activity
                startActivity(i)

                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }
}