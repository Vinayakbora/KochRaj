package com.example.kochraj.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kochraj.R
import com.example.kochraj.domaim.model.User
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.EMPTY_STRING
import com.example.kochraj.ui.theme.MintTulip
import com.example.kochraj.viewmodels.UserSearchViewModel
import com.example.kochraj.viewmodels.UserViewModel

@Composable
fun UserDetailsScreen(
    navController: NavController,
    userId: String,
    userViewModel: UserViewModel = hiltViewModel(),
    searchViewModel: UserSearchViewModel = hiltViewModel()
) {
    val userState by userViewModel.userState.collectAsState()
    val selectedUser by searchViewModel.selectedUser.collectAsState()

    // If we have a selected user from search, use that
    // Otherwise, fetch the user details using the userId
    LaunchedEffect(userId) {
        if (selectedUser?.id != userId) {
            userViewModel.getUserDetails(userId)
        }
    }

    // Determine which user to display
    val user = if (selectedUser?.id == userId) {
        selectedUser
    } else if (userState is UserViewModel.UserState.Success) {
        (userState as UserViewModel.UserState.Success).user
    } else {
        null
    }

    if (user != null) {
        UserDetailsContent(user = user, navController = navController)
    } else if (userState is UserViewModel.UserState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (userState is UserViewModel.UserState.Error) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(
                text = (userState as UserViewModel.UserState.Error).message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun UserDetailsContent(
    user: User, navController: NavController,
) {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier
            .background(color = Aztec)
            .fillMaxSize()
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = user.name.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
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
                if (!user.phone.isNullOrBlank()) {
                    IconButton(
                        onClick = { uriHandler.openUri("tel:+91${user.phone}") },
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

                if (!user.email.isNullOrBlank()) {
                    IconButton(
                        onClick = { uriHandler.openUri("mailto:${user.email}") },
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
                    text = user.profession ?: EMPTY_STRING,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    text = user.presentAddress ?: EMPTY_STRING,
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
                        InfoRow("Gender", user.gender ?: EMPTY_STRING)
                        InfoRow("Date of Birth", user.dateOfBirth ?: EMPTY_STRING)
                        InfoRow("Date of Birth", user.gender ?: EMPTY_STRING)
                        InfoRow("Blood Group", user.bloodGroup ?: EMPTY_STRING)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        InfoRow("Father's Name", user.fatherName ?: EMPTY_STRING)
                        InfoRow("Mother's Name", user.motherName ?: EMPTY_STRING)
                        InfoRow("Spouse Name", user.spouseName ?: EMPTY_STRING)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        InfoRow("Profession", user.profession ?: EMPTY_STRING)
                        InfoRow("Qualification", user.qualification ?: EMPTY_STRING)
                        InfoRow("Skills", user.skills ?: EMPTY_STRING)
                        InfoRow("Languages", user.languages?.joinToString(", ") ?: EMPTY_STRING)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        InfoRow("Place of Birth", user.placeOfBirth ?: EMPTY_STRING)
                        InfoRow("Permanent Address", user.permanentAddress ?: EMPTY_STRING)
                        InfoRow("Present Address", user.presentAddress ?: EMPTY_STRING)
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun InfoRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 2,
            color = Color.Gray,
            modifier = Modifier
                .width(120.dp)
                .padding(end = 4.dp)
        )
        Text(
            text = value,
            textAlign = TextAlign.End,
            fontSize = 14.sp,
            maxLines = 2,
            color = Color.Black
        )
    }
}