package com.wiwiwi.pondokmadre.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.wiwiwi.pondokmadre.data.navigation.AppNavigation
import com.wiwiwi.pondokmadre.ui.theme.PondokMadreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PondokMadreTheme {
                MainScreen()
            }
        }
    }
}
