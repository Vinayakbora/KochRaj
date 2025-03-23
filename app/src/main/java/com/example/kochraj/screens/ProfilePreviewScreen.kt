package com.example.kochraj.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kochraj.R
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.EMPTY_STRING
import com.example.kochraj.ui.theme.MintTulip
import com.example.kochraj.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePreviewScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    val user = if (userState is UserViewModel.UserState.Success) {
        (userState as UserViewModel.UserState.Success).user
    } else null

    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors =  TopAppBarDefaults.topAppBarColors(containerColor = Aztec),
                title = { Text("Profile Preview") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .background(color = Aztec)
                .fillMaxSize()
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Plumber Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!user?.phone.isNullOrBlank()) {
                        IconButton(
                            onClick = { uriHandler.openUri("tel:+91${user?.phone}") },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MintTulip)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call",
                                tint = Aztec
                            )
                        }
                    }

                    if (!user?.email.isNullOrBlank()) {
                        IconButton(
                            onClick = { uriHandler.openUri("mailto:${user?.email}") },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MintTulip)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = Aztec
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = user?.profession ?: EMPTY_STRING,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = user?.presentAddress ?: EMPTY_STRING,
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "About",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            InfoRow("Gender", user?.gender ?: EMPTY_STRING)
                            InfoRow("Date of Birth", user?.dateOfBirth ?: EMPTY_STRING)
                            InfoRow("Date of Birth", user?.gender ?: EMPTY_STRING)
                            InfoRow("Blood Group", user?.bloodGroup ?: EMPTY_STRING )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            InfoRow("Father's Name", user?.fatherName ?: EMPTY_STRING)
                            InfoRow("Mother's Name", user?.motherName ?: EMPTY_STRING)
                            InfoRow("Spouse Name", user?.spouseName ?: EMPTY_STRING)

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            InfoRow("Profession", user?.profession ?: EMPTY_STRING)
                            InfoRow("Qualification", user?.qualification ?: EMPTY_STRING)
                            InfoRow("Skills", user?.skills ?: EMPTY_STRING)
                            InfoRow("Languages", user?.languages?.joinToString(", ") ?: EMPTY_STRING)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            InfoRow("Place of Birth", user?.placeOfBirth ?: EMPTY_STRING)
                            InfoRow("Permanent Address", user?.permanentAddress ?: EMPTY_STRING)
                            InfoRow("Present Address", user?.presentAddress ?: EMPTY_STRING)
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
