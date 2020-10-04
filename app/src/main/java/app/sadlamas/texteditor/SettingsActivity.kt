package app.sadlamas.texteditor

import android.os.Bundle
import android.preference.PreferenceActivity

@Suppress("DEPRECATION")
class SettingsActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }

}