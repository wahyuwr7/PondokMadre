package com.wiwiwi.pondokmadre.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wiwiwi.pondokmadre.data.dummy.SampleDataProvider
import com.wiwiwi.pondokmadre.data.route.AppNavigation
import com.wiwiwi.pondokmadre.ui.dashboard.DashboardScreen
import com.wiwiwi.pondokmadre.ui.theme.PondokMadreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PondokMadreTheme {
                // MainActivity hanya perlu memanggil AppNavigation.
                // Semua logika screen dan navigasi sudah diatur di dalamnya.
                AppNavigation()
            }
        }
    }
}
