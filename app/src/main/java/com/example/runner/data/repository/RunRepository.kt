package com.example.runner.data.repository

import androidx.annotation.WorkerThread
import com.example.runner.data.db.RunDao
import com.example.runner.data.model.Run

class RunRepository (
    private val runDao: RunDao) {

    init {
        runDao.getAllRuns()
    }

    suspend fun insert(run: Run) {
        runDao.insertRun(run)
    }

    suspend fun delete(run: Run) {
        runDao.deleteRun(run)
    }

    fun getAllRuns() = runDao.getAllRuns()
}


