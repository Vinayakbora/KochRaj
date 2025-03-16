package com.example.kochraj.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    val user = if (userState is UserViewModel.UserState.Success) {
        (userState as UserViewModel.UserState.Success).user
    } else null

    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors =  TopAppBarDefaults.topAppBarColors(containerColor = Aztec),
                title = { Text("My Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = Aztec)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Name
                    AccountInfoItem(
                        icon = Icons.Outlined.Person,
                        label = "Name",
                        value = user?.name ?: "Not provided"
                    )

                    // Email
                    AccountInfoItem(
                        icon = Icons.Outlined.Email,
                        label = "Email",
                        value = user?.email ?: "Not provided"
                    )

                    // Phone
                    AccountInfoItem(
                        icon = Icons.Outlined.Phone,
                        label = "Phone",
                        value = user?.phone ?: "Not provided"
                    )

                    // Address
                    AccountInfoItem(
                        icon = Icons.Outlined.Home,
                        label = "Present Address",
                        value = user?.presentAddress ?: "Not provided"
                    )
                }
            }

            // Security Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Security",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Button(
                        onClick = { showChangePasswordDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Outlined.Lock,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Change Password")
                    }
                }
            }
        }

        // Change Password Dialog
        if (showChangePasswordDialog) {
            AlertDialog(
                onDismissRequest = {
                    showChangePasswordDialog = false
                    newPassword = ""
                    confirmPassword = ""
                    passwordError = null
                },
                title = { Text("Change Password") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            isError = passwordError != null
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            isError = passwordError != null
                        )

                        passwordError?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newPassword.length < 6) {
                                passwordError = "Password must be at least 6 characters"
                            } else if (newPassword != confirmPassword) {
                                passwordError = "Passwords do not match"
                            } else {
                                // TODO: Implement password change functionality
                                showChangePasswordDialog = false
                                newPassword = ""
                                confirmPassword = ""
                                passwordError = null
                            }
                        }
                    ) {
                        Text("Change")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showChangePasswordDialog = false
                            newPassword = ""
                            confirmPassword = ""
                            passwordError = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun AccountInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 16.dp)
        )

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

