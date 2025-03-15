package com.example.kochraj.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kochraj.navigation.Routes
import com.example.kochraj.ui.theme.KochRajTheme
import com.example.kochraj.viewmodels.UserViewModel

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val registrationFormState by viewModel.registrationFormState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is UserViewModel.AuthState.Success) {
            navController.navigate(Routes.LoginScreen.route)
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
            text = "Register",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = registrationFormState.name,
            onValueChange = { viewModel.updateRegistrationName(it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = registrationFormState.nameError != null,
            supportingText = {
                registrationFormState.nameError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = registrationFormState.email,
            onValueChange = { viewModel.updateRegistrationEmail(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = registrationFormState.emailError != null,
            supportingText = {
                registrationFormState.emailError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = registrationFormState.phone,
            onValueChange = {
                // Only allow digits and limit to 10 characters
                if (it.all { char -> char.isDigit() } && it.length <= 10) {
                    viewModel.updateRegistrationPhone(it)
                }
            },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
            isError = registrationFormState.phoneError != null,
            supportingText = {
                registrationFormState.phoneError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = registrationFormState.password,
            onValueChange = { viewModel.updateRegistrationPassword(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            isError = registrationFormState.passwordError != null,
            supportingText = {
                registrationFormState.passwordError?.let {
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
            onClick = { viewModel.register() },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is UserViewModel.AuthState.Loading
        ) {
            if (authState is UserViewModel.AuthState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Register")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigateUp() }
        ) {
            Text("Already have an account? Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    val navController = rememberNavController()
    KochRajTheme {
        RegistrationScreen(navController)
    }
}

