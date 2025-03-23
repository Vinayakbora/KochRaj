package com.example.kochraj.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kochraj.R
import com.example.kochraj.ui.theme.Juniper

@SuppressLint("DefaultLocale")
@Composable
fun RecommendedSearchCard(
    name: String,
    imageUrl: String,
    onAppointmentClick: () -> Unit,
    onCallClick: () -> Unit,
    onMessageClick: () -> Unit,
    onCardClick: () -> Unit,
    location: String,
    profession: String,
    isFavourite: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Juniper
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Doctor Image
//                Image(
//                    painter = rememberAsyncImagePainter(imageUrl),
//                    contentDescription = "Doctor $name",
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(RoundedCornerShape(12.dp)),
//                    contentScale = ContentScale.Crop
//                )
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Doctor $name",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Doctor Info
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = profession,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Action Buttons
                Row {
                    IconButton(
                        onClick = onCallClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            Icons.Default.Call,
                            contentDescription = "Call",
                            tint =  Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onMessageClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "Message",
                            tint =  Color.White
                        )
                    }
                }

                // Appointment Button
                Button(
                    onClick = onAppointmentClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isFavourite) "Remove Profile" else "Save Profile",
                        color = Color.White
                    )
                }
            }
        }
    }
}



