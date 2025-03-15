package com.example.kochraj

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.kochraj.navigation.AppNavHost
import com.example.kochraj.navigation.Routes
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.KochRajTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val isLoggedIn = remember { sharedPreferences.getBoolean("is_logged_in", false) }
            KochRajTheme {
                val systemUiController = rememberSystemUiController()

                systemUiController.setSystemBarsColor(
                    color = Aztec,
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(startDestination = if (isLoggedIn) Routes.HomeScreen.route else Routes.LoginScreen.route)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KochRajTheme {
        AppNavHost(Routes.LoginScreen.route)
    }
}