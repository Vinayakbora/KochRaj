package com.example.kochraj.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.kochraj.R
import com.example.kochraj.navigation.Routes
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.EMPTY_STRING
import com.example.kochraj.ui.theme.MintTulip
import com.example.kochraj.ui.theme.NewContainer
import com.example.kochraj.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    navHostController: NavHostController,
    viewModel: UserViewModel = hiltViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val isUploadingImage by viewModel.isUploadingImage.collectAsState()

    val userState by viewModel.userState.collectAsState()
    val user = if (userState is UserViewModel.UserState.Success) {
        (userState as UserViewModel.UserState.Success).user
    } else null

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun onLogout() {
        sharedPreferences.edit().putBoolean("is_logged_in", false).apply()
        navController.navigate(Routes.LoginScreen.route) {
            popUpTo(Routes.HomeScreen.route) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Aztec)
    ) {
        // Profile Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MintTulip
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .border(width = 1.dp, color = Color.Black, shape = CircleShape )
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user?.name ?: "User",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Aztec,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user?.profession ?: EMPTY_STRING,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Aztec.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navHostController.navigate(Routes.PersonalDetailsScreen.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Aztec
                    ),
                    shape = RoundedCornerShape(100.dp),
                    border = BorderStroke(width = 1.dp, color = Aztec),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                ) {
                    Text(
                        "Edit Profile",
                        color = Aztec,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Menu Items
        ProfileMenuItem(
            icon = Icons.Outlined.Person,
            title = "My Account",
            subtitle = "Make changes to your account",
            showWarning = true,
            onClick = { }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.Close,
            title = "Log out",
            subtitle = "Logout of the application",
            onClick = {
                showLogoutDialog = true
            }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.Person,
            title = "Profile Preview",
            subtitle = "Preview Profile",
            onClick = { }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.Info,
            title = "Help & Support",
            onClick = {  }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.Info,
            title = "About App",
            onClick = {  }
        )
    }

    // Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout(::onLogout)
                        navController.navigate(Routes.LoginScreen.route) {
                            popUpTo(0) // Clear back stack
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    showWarning: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MintTulip.copy(alpha = 0.8f),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = NewContainer
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MintTulip
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showWarning) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "Warning",
                    tint = Color(0xFFFF3B30),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

