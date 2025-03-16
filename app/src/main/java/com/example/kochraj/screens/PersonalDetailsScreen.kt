package com.example.kochraj.screens

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.MintTulip
import com.example.kochraj.viewmodels.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetailsScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val personalDetailsState by viewModel.personalDetailsState.collectAsState()
    val userState by viewModel.userState.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Aztec)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Personal Details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Show loading indicator if data is being loaded
        if (userState is UserViewModel.UserState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally)
            )
        }

        // Show error message if there's an error
        if (userState is UserViewModel.UserState.Error) {
            Text(
                text = (userState as UserViewModel.UserState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        OutlinedTextField(
            value = personalDetailsState.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            isError = personalDetailsState.nameError != null,
            supportingText = {
                personalDetailsState.nameError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = personalDetailsState.fatherName,
            onValueChange = { viewModel.updateFatherName(it) },
            label = { Text("Father's Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            isError = personalDetailsState.fatherNameError != null,
            supportingText = {
                personalDetailsState.fatherNameError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = personalDetailsState.motherName,
            onValueChange = { viewModel.updateMotherName(it) },
            label = { Text("Mother's Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            isError = personalDetailsState.motherNameError != null,
            supportingText = {
                personalDetailsState.motherNameError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = personalDetailsState.spouseName,
            onValueChange = { viewModel.updateSpouseName(it) },
            label = { Text("Spouse Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            isError = personalDetailsState.spouseNameError != null,
            supportingText = {
                personalDetailsState.spouseNameError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        // Date of Birth
        OutlinedTextField(
            value = personalDetailsState.dateOfBirth,
            onValueChange = { viewModel.updateDateOfBirth(it) },
            label = { Text("Date of Birth") },
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
            },
            isError = personalDetailsState.dateOfBirthError != null,
            supportingText = {
                personalDetailsState.dateOfBirthError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
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
                            viewModel.updateDateOfBirth(localDate.format(dateFormatter))
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
            value = personalDetailsState.placeOfBirth,
            onValueChange = { viewModel.updatePlaceOfBirth(it) },
            label = "Place of Birth",
            icon = Icons.Outlined.LocationOn,
            options = listOf("City A", "City B", "City C")
        )

        OutlinedTextField(
            value = personalDetailsState.permanentAddress,
            onValueChange = { viewModel.updatePermanentAddress(it) },
            label = { Text("Permanent Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            minLines = 2,
            isError = personalDetailsState.permanentAddressError != null,
            supportingText = {
                personalDetailsState.permanentAddressError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = personalDetailsState.presentAddress,
            onValueChange = { viewModel.updatePresentAddress(it) },
            label = { Text("Present Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            minLines = 2,
            isError = personalDetailsState.presentAddressError != null,
            supportingText = {
                personalDetailsState.presentAddressError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = personalDetailsState.profession,
            onValueChange = { viewModel.updateProfession(it) },
            label = { Text("Profession") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(Icons.Outlined.Star, contentDescription = null) }
        )

        // Qualifications (Dropdown)
        PersonalDetailDropdown(
            value = personalDetailsState.qualification,
            onValueChange = { viewModel.updateQualification(it) },
            label = "Qualifications",
            icon = Icons.Outlined.Home,
            options = listOf("High School", "Bachelor's", "Master's", "PhD")
        )

        // Gender (Dropdown)
        PersonalDetailDropdown(
            value = personalDetailsState.gender,
            onValueChange = { viewModel.updateGender(it) },
            label = "Gender",
            icon = Icons.Outlined.Person,
            options = listOf("Male", "Female", "Other")
        )

        OutlinedTextField(
            value = personalDetailsState.bloodGroup,
            onValueChange = { viewModel.updateBloodGroup(it) },
            label = { Text("Blood Group") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(Icons.Outlined.Star, contentDescription = null) }
        )

        // Languages Known (Dropdown)
        PersonalDetailMultiDropdown(
            selectedOptions = personalDetailsState.languagesKnown,
            onOptionsChange = { viewModel.updateLanguagesKnown(it) },
            label = "Languages Known",
            icon = Icons.Outlined.Star,
            options = listOf("English", "Hindi", "Bengali", "Tamil", "Telugu")
        )

        OutlinedTextField(
            value = personalDetailsState.skills,
            onValueChange = { viewModel.updateSkills(it) },
            label = { Text("Skills") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = { Icon(Icons.Outlined.Star, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.savePersonalDetails()
                // Only navigate back if there are no validation errors
                if (personalDetailsState.nameError == null &&
                    personalDetailsState.fatherNameError == null &&
                    personalDetailsState.motherNameError == null &&
                    personalDetailsState.spouseNameError == null &&
                    personalDetailsState.dateOfBirthError == null &&
                    personalDetailsState.permanentAddressError == null &&
                    personalDetailsState.presentAddressError == null) {
                    navController.popBackStack()
                }
            },
            colors =  ButtonDefaults.buttonColors(
                containerColor = MintTulip, contentColor = Aztec
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Details")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetailDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = value,
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
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetailMultiDropdown(
    selectedOptions: List<String>,
    onOptionsChange: (List<String>) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    remember(selectedOptions) { selectedOptions.toMutableStateList() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOptions.joinToString(", "),
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
                val isSelected = option in selectedOptions
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        val newList = if (isSelected) {
                            selectedOptions - option
                        } else {
                            selectedOptions + option
                        }
                        onOptionsChange(newList)
                    },
                    trailingIcon = if (isSelected) {
                        { Icon(Icons.Outlined.Check, contentDescription = null) }
                    } else null
                )
            }

            Button(
                onClick = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Done")
            }
        }
    }
}

