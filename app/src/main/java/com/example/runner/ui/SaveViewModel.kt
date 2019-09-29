package com.example.runner.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.runner.data.db.RunDatabase
import com.example.runner.data.model.Run
import com.example.runner.data.repository.RunRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveViewModel(
    application: Application
): AndroidViewModel(application) {

    private var runRepository: RunRepository? = null

    init {
        val runDao = RunDatabase.getDatabase(application).runDao()
        runRepository = RunRepository(runDao)
    }

    fun addRun(run: Run) {
        CoroutineScope(Dispatchers.IO).launch {
            runRepository?.insert(run)
        }
    }


}