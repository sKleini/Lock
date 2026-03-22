package de.kleini.lock

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "lock_prefs")

class LockViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val SHOULD_EXIT_KEY = booleanPreferencesKey("should_exit")
    }

    private val _shouldExit = MutableStateFlow(false)
    val shouldExit: StateFlow<Boolean> = _shouldExit

    init {
        viewModelScope.launch {
            val prefs = getApplication<Application>().dataStore.data.first()
            _shouldExit.value = prefs[SHOULD_EXIT_KEY] ?: false
        }
    }

    fun setShouldExit(value: Boolean) {
        _shouldExit.value = value
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[SHOULD_EXIT_KEY] = value
            }
        }
    }
}
