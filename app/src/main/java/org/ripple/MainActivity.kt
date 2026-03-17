package org.ripple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import org.ripple.navigation.AppNavigation
import org.ripple.ui.theme.RippleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RippleTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}