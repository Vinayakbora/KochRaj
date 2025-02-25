package com.example.kochraj.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetailsScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var emailId by remember { mutableStateOf("") }
    var instagramId by remember { mutableStateOf("") }
    var facebookId by remember { mutableStateOf("") }
    val selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedGender by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var detailedAddress by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    val documents by remember { mutableStateOf<List<String>>(emptyList()) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Personal Details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text("Contact Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = emailId,
            onValueChange = { emailId = it },
            label = { Text("Email ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = instagramId,
            onValueChange = { instagramId = it },
            label = { Text("Instagram ID") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Star, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = facebookId,
            onValueChange = { facebookId = it },
            label = { Text("Facebook ID") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Star, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Age (Calendar Menu)
        OutlinedTextField(
            value = selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
            onValueChange = { },
            label = { Text("Age") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    // Show date picker dialog
                }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select date")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Star, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Gender (Dropdown Menu)
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { }
        ) {
            OutlinedTextField(
                value = selectedGender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) }
            )
            ExposedDropdownMenu(
                expanded = false,
                onDismissRequest = { }
            ) {
                listOf("Male", "Female", "Other").forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            selectedGender = gender
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // State (Dropdown Menu)
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { }
        ) {
            OutlinedTextField(
                value = selectedState,
                onValueChange = {},
                readOnly = true,
                label = { Text("State") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = null) }
            )
            ExposedDropdownMenu(
                expanded = false,
                onDismissRequest = { }
            ) {
                listOf("State 1", "State 2", "State 3").forEach { state ->
                    DropdownMenuItem(
                        text = { Text(state) },
                        onClick = {
                            selectedState = state
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // City (Dropdown Menu)
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { }
        ) {
            OutlinedTextField(
                value = selectedCity,
                onValueChange = {},
                readOnly = true,
                label = { Text("City") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = null) }
            )
            ExposedDropdownMenu(
                expanded = false,
                onDismissRequest = { }
            ) {
                listOf("City 1", "City 2", "City 3").forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city) },
                        onClick = {
                            selectedCity = city
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = pincode,
            onValueChange = { pincode = it },
            label = { Text("Pincode") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = detailedAddress,
            onValueChange = { detailedAddress = it },
            label = { Text("Detailed Address") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            leadingIcon = { Icon(Icons.Outlined.Home, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = occupation,
            onValueChange = { occupation = it },
            label = { Text("Occupation") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.AccountBox, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Documents (File Upload)
        Text(
            text = "Documents",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                // Implement file upload logic
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Outlined.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Upload Documents")
        }

        if (documents.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            documents.forEach { document ->
                Text(document, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Save personal details
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Details")
        }
    }
}

