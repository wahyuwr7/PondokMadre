package com.wiwiwi.pondokmadre

import android.app.Application
import com.wiwiwi.pondokmadre.data.db.AppDatabase
import com.wiwiwi.pondokmadre.data.repository.AppRepository

class PondokMadreApp : Application() {
    // Menggunakan 'lazy' agar database dan repository hanya diinisialisasi saat dibutuhkan
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { AppRepository(database.appDao()) }
}
