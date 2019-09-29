package com.example.runner.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.runner.data.db.RunDatabase
import com.example.runner.data.model.Run
import com.example.runner.data.repository.RunRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var runRepository: RunRepository? = null
    var allRuns: LiveData<List<Run>>? = null

    init {
        val runDao = RunDatabase.getDatabase(application).runDao()
        runRepository = RunRepository(runDao)
        allRuns = runRepository!!.getAllRuns()
    }

    fun deleteRun(run: Run) {
        CoroutineScope(Dispatchers.IO).launch {
            runRepository?.delete(run)
        }
    }

     //fun getAllRuns() = runRepository?.getAllRuns()
}