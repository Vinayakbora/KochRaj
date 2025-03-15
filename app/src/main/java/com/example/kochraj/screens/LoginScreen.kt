package com.example.kochraj.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kochraj.navigation.Routes
import com.example.kochraj.viewmodels.UserViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val loginFormState by viewModel.loginFormState.collectAsState()

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun onLoginSuccess() {
        sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
        navController.navigate(Routes.HomeScreen.route) {
            popUpTo(Routes.LoginScreen.route) { inclusive = true }
        }
    }

    LaunchedEffect(authState) {
        if (authState is UserViewModel.AuthState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = loginFormState.email,
            onValueChange = { viewModel.updateLoginEmail(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = loginFormState.emailError != null,
            supportingText = {
                loginFormState.emailError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = loginFormState.password,
            onValueChange = { viewModel.updateLoginPassword(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            isError = loginFormState.passwordError != null,
            supportingText = {
                loginFormState.passwordError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (authState is UserViewModel.AuthState.Error) {
            Text(
                text = (authState as UserViewModel.AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = { viewModel.login(::onLoginSuccess) },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is UserViewModel.AuthState.Loading
        ) {
            if (authState is UserViewModel.AuthState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate(Routes.RegistrationScreen.route) }
        ) {
            Text("Don't have an account? Register")
        }
    }
}

