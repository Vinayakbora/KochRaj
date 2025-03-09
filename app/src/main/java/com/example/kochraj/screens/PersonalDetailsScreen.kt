package com.example.kochraj.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.EMPTY_STRING
import com.example.kochraj.viewmodels.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetailsScreen(navController: NavController) {
    var name by remember { mutableStateOf(EMPTY_STRING) }
    var fatherName by remember { mutableStateOf(EMPTY_STRING) }
    var motherName by remember { mutableStateOf(EMPTY_STRING) }
    var spouseName by remember { mutableStateOf(EMPTY_STRING) }
    var dateOfBirth by remember { mutableStateOf(EMPTY_STRING) }
    var placeOfBirth by remember { mutableStateOf(EMPTY_STRING) }
    var permanentAddress by remember { mutableStateOf(EMPTY_STRING) }
    var presentAddress by remember { mutableStateOf(EMPTY_STRING) }
    var profession by remember { mutableStateOf(EMPTY_STRING) }
    var qualification by remember { mutableStateOf(EMPTY_STRING) }
    var gender by remember { mutableStateOf(EMPTY_STRING) }
    var bloodGroup by remember { mutableStateOf(EMPTY_STRING) }

    var languagesKnown by remember { mutableStateOf(EMPTY_STRING) }
    var skills by remember { mutableStateOf(EMPTY_STRING) }

    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Aztec)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Personal Details",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PersonalDetailTextField(
            value = name,
            onValueChange = { name = it },
            label = "Name",
            icon = Icons.Outlined.Person
        )

        PersonalDetailTextField(
            value = fatherName,
            onValueChange = { fatherName = it },
            label = "Father's Name",
            icon = Icons.Outlined.Person
        )

        PersonalDetailTextField(
            value = motherName,
            onValueChange = { motherName = it },
            label = "Mother's Name",
            icon = Icons.Outlined.Person
        )

        PersonalDetailTextField(
            value = spouseName,
            onValueChange = { spouseName = it },
            label = "Spouse Name",
            icon = Icons.Outlined.Person
        )

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { },
            label = { Text("Date of Birth") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    showDatePicker = true
                }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select date")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker = true
                }
                .padding(vertical = 8.dp),
            leadingIcon = {
                IconButton(onClick = {
                    showDatePicker = true
                }) { Icon(Icons.Outlined.DateRange, contentDescription = null)
                }
            }
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                            dateOfBirth = localDate.format(dateFormatter)
                        }
                        showDatePicker = false
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Place of Birth (Dropdown)
        PersonalDetailDropdown(
            value = placeOfBirth,
            onValueChange = { placeOfBirth = it },
            label = "Place of Birth",
            icon = Icons.Outlined.LocationOn,
            options = listOf("City A", "City B", "City C")
        )

        PersonalDetailTextField(
            value = permanentAddress,
            onValueChange = { permanentAddress = it },
            label = "Permanent Address",
            icon = Icons.Outlined.Home,
            maxLines = 3
        )

        PersonalDetailTextField(
            value = presentAddress,
            onValueChange = { presentAddress = it },
            label = "Present Address",
            icon = Icons.Outlined.Home,
            maxLines = 3
        )

        PersonalDetailTextField(
            value = profession,
            onValueChange = { profession = it },
            label = "Profession",
            icon = Icons.Outlined.Star
        )

        // Qualifications (Dropdown)
        PersonalDetailDropdown(
            value = qualification,
            onValueChange = { qualification = it },
            label = "Qualifications",
            icon = Icons.Outlined.Home,
            options = listOf("High School", "Bachelor's", "Master's", "PhD")
        )

        // Gender (Dropdown)
        PersonalDetailDropdown(
            value = gender,
            onValueChange = { gender = it },
            label = "Gender",
            icon = Icons.Outlined.Person,
            options = listOf("Male", "Female", "Other")
        )

        PersonalDetailTextField(
            value = bloodGroup,
            onValueChange = { bloodGroup = it },
            label = "Blood Group",
            icon = Icons.Outlined.Favorite
        )

        // Languages Known (Dropdown)
        PersonalDetailDropdown(
            value = languagesKnown,
            onValueChange = { languagesKnown = it },
            label = "Languages Known",
            icon = Icons.Outlined.Star,
            options = listOf("English", "Hindi", "Bengali", "Assamese"),
            isMultiSelect = true
        )

        PersonalDetailTextField(
            value = skills,
            onValueChange = { skills = it },
            label = "Skills",
            icon = Icons.Outlined.Build
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Details Saved", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Details")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Rest of your code remains the same
@Composable
fun PersonalDetailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        leadingIcon = { Icon(icon, contentDescription = null) },
        maxLines = maxLines
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetailDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    options: List<String>,
    isMultiSelect: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptions by remember { mutableStateOf(setOf<String>()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (isMultiSelect) selectedOptions.joinToString(", ") else value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(icon, contentDescription = null) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        if (isMultiSelect) {
                            selectedOptions = if (option in selectedOptions) {
                                selectedOptions - option
                            } else {
                                selectedOptions + option
                            }
                        } else {
                            onValueChange(option)
                            expanded = false
                        }
                    },
                    trailingIcon = if (isMultiSelect && option in selectedOptions) {
                        { Icon(Icons.Outlined.Check, contentDescription = null) }
                    } else null
                )
            }
        }
    }
}
