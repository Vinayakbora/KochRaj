package com.example.kochraj.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kochraj.R
import com.example.kochraj.ui.theme.Aztec

@Composable
fun ProductDetailsScreen(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .background(color = Aztec)
            .fillMaxSize()
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Deepak Ray",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /*TODO: Handle share click*/ }) {
                    Icon(
                        Icons.Filled.Share,
                        "Share",
                        tint = Color.White
                    )
                }

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
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Senior Surgeon",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        "Share",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "4.8 (2k+ Rating)",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = "123 Main Street, Guwahati, Assam",
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
                        // Personal information
                        InfoRow("Gender", "Male")
                        InfoRow("Date of Birth", "13/08/1988")
                        InfoRow("Blood Group", "A+")

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Family information
                        InfoRow("Father's Name", "Satyam Ray")
                        InfoRow("Mother's Name", "Sunita Ray")
                        InfoRow("Spouse Name", "Rita Ray")

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Professional information
                        InfoRow("Profession", "Senior Surgeon")
                        InfoRow("Qualification", "MBBS, MS")
                        InfoRow("Skills", "Surgery, Medicine")
                        InfoRow("Languages", "Bengali, Assamese, Hindi, English")

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Address information
                        InfoRow("Place of Birth", "Guwahati, Assam")
                        InfoRow("Permanent Address", "123 Main Street, Guwahati, Assam")
                        InfoRow("Present Address", "123 Main Street, Guwahati, Assam ")
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
            modifier = Modifier.width(120.dp).padding(end = 4.dp)
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