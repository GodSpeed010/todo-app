package com.example.simpletodo

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class MainActivity : AppCompatActivity() {

    val KEY_ITEM_TEXT: String = "item_text"
    val KEY_ITEM_POSITION: String = "item position"
    val EDIT_TEXT_CODE: Int = 20

    val COLOR_THEME: String = "theme"
    var colorTheme = "default"
    val EDIT_THEME_CODE: Int = 30

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadItems()

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                deleteTask(position)
            }

        }

        val onClickListener = object: TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int) {
                //create the new activity
                val i = Intent(this@MainActivity, EditActivity::class.java)

                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, listOfTasks.get(position))
                i.putExtra(KEY_ITEM_POSITION, position)

                //pass the color theme
                i.putExtra(COLOR_THEME, colorTheme)

                //display the activity
                startActivityForResult(i, EDIT_TEXT_CODE)
            }

            override fun onDeleteClicked(position: Int) {
                Log.d("my_tag", "onDeleteClicked: delete button clicked")
                deleteTask(position)
            }

            override fun onEditClicked(position: Int) {
                Log.d("my_tag", "onEditClicked: edit button clicked")

                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Edit Task")

                val viewInflated: View = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.popup_task_edit, findViewById(android.R.id.content), false)

                // Set up the input
                val inputEditText = viewInflated.findViewById(R.id.input) as TextInputEditText

                //populate EditText with task's current text
                inputEditText.setText(listOfTasks[position])

                // Specify the type of input expected
                builder.setView(viewInflated)

                builder.setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()

                        //update task in listOfTasks
                        listOfTasks[position] = inputEditText.text.toString()

                        //make RecyclerView show updated Task
                        adapter.notifyItemChanged(position)

                        saveItems()
                    })

                builder.setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                builder.show()

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

    fun deleteTask(position: Int) {
        //stores the task to be deleted in case user wants to undo
        val deletedTask = listOfTasks.elementAt(position)

        //remove the item from the list
        listOfTasks.removeAt(position)
        //notify the adapter that our data set has changed
        adapter.notifyItemRemoved(position)

        //shows a snackbar that informs the user they deleted a task and has an undo button
        showUndoSnackbar(deletedTask, position)

        saveItems()
    }

    //handle the result of the edit activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)

        //handles for EditActivity
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

        //handles for ThemeActivity
        if (resultCode == RESULT_OK && requestCode == EDIT_THEME_CODE) {
            Log.d("my_tag", "returned from ThemeActivity with ${data?.getStringExtra(COLOR_THEME)}")
            setThemeTo(data?.getStringExtra(COLOR_THEME))
            colorTheme = data?.getStringExtra(COLOR_THEME).toString()
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

                i.putExtra(COLOR_THEME, colorTheme)
                //launch the activity
                startActivityForResult(i, EDIT_THEME_CODE)

                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    fun setThemeTo(theme: String?) {
        when (theme) {
            "red" -> {
                setTheme(R.style.Theme_SimpleToDo_Red)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.red_800)))
                findViewById<Button>(R.id.button).setBackgroundColor(resources.getColor(R.color.red_800))
                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.red_800)
                    window.statusBarColor = resources.getColor(R.color.red_800)
                }
            }
            "green" -> {
                setTheme(R.style.Theme_SimpleToDo_Green)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.green_800)))
                findViewById<Button>(R.id.button).setBackgroundColor(resources.getColor(R.color.green_800))
                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.green_800)
                    window.statusBarColor = resources.getColor(R.color.green_800)
                }
            }
            "blue" -> {
                setTheme(R.style.Theme_SimpleToDo_Blue)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.blue_700)))
                findViewById<Button>(R.id.button).setBackgroundColor(resources.getColor(R.color.blue_700))
                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.blue_700)
                    window.statusBarColor = resources.getColor(R.color.blue_700)
                }
            }
            else -> {
                setTheme(R.style.Theme_SimpleToDo)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.purple_500)))
                findViewById<Button>(R.id.button).setBackgroundColor(resources.getColor(R.color.purple_500))
                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.purple_500)
                    window.statusBarColor = resources.getColor(R.color.purple_500)
                }
            }
        }

    }
}