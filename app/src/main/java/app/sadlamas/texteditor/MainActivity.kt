package app.sadlamas.texteditor

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
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

@Suppress("DEPRECATION")
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

    override fun onResume() {
        super.onResume()
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(this)

        if (mPrefs.getBoolean(getString(R.string.pref_openmode), false)) {
            openFile(FILENAME)
        }

        val size = mPrefs.getString(getString(R.string.pref_size), "20")?.toFloat()
        mEditor.textSize = size!!

        val regular = mPrefs.getString(getString(R.string.pref_style), "")
        var typeface = Typeface.NORMAL
        if (regular!!.contains("Полужирный")) {
            typeface += Typeface.BOLD
        }
        if (regular.contains("Курсив")) {
            typeface += Typeface.ITALIC
        }
        mEditor.setTypeface(null, typeface)

        val colorPref = mPrefs.getString(getString(R.string.pref_color), "Черный")
        var color = Color.BLACK
        when (colorPref) {
            "Черный" -> color = Color.BLACK
            "Красный" -> color = Color.RED
            "Синий" -> color = Color.BLUE
            "Зеленый" -> color = Color.GREEN
        }
        mEditor.setTextColor(color)
    }

    override fun onPause() {
        super.onPause()
        saveFile(FILENAME)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
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
                        builder.append("$it\n")
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