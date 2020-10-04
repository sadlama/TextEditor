package app.sadlamas.texteditor

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import app.sadlamas.texteditor.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    private val FILENAME = "sample.txt"

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mToolbar: Toolbar
    private lateinit var mEditor: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mToolbar = mBinding.mainToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.app_name)

        mEditor = mBinding.editor
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open -> {
                openFile(FILENAME)
                true
            }
            R.id.action_save -> {
                saveFile(FILENAME)
                true
            }
            else -> true
        }
    }

    private fun openFile(fileName: String) {
        try {
            val inputStream = openFileInput(fileName)

            inputStream?.let {
                val iStreamReader = InputStreamReader(inputStream)
                val reader = BufferedReader(iStreamReader)
                val builder = StringBuilder()

                while (true) {
                    val line = reader.readLine() ?: break
                    line.let {
                        builder.append("$it\n");
                    }
                }
                inputStream.close()
                mEditor.setText(builder.toString())
            }
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "Exception ${e.printStackTrace()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveFile(fileName: String) {
        try {
            val fileOutputStream = openFileOutput(fileName, 0)
            val oStreamWriter = OutputStreamWriter(fileOutputStream)
            oStreamWriter.write(mEditor.text.toString())
            oStreamWriter.close()
        } catch (e: java.lang.Exception) {
            Toast.makeText(
                applicationContext,
                "Exception ${e.printStackTrace()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}