package com.devarsh.fitnesschallengeapp.data.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.devarsh.fitnesschallengeapp.R
import com.devarsh.fitnesschallengeapp.data.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController,
    onLogout: () -> Unit
) {

    val settings by viewModel.settings.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.saveInitial()
    }

    var reminderEnabled by remember {
        mutableStateOf(
            settings?.reminderEnabled ?: true
        )
    }

    var reminderTime by remember {
        mutableStateOf(
            settings?.reminderTime ?: "08:00"
        )
    }

    var units by remember {
        mutableStateOf(
            settings?.units ?: "metric"
        )
    }

    LaunchedEffect(settings) {

        settings?.let {

            reminderEnabled = it.reminderEnabled
            reminderTime = it.reminderTime
            units = it.units
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Background image
        Image(
            painter = painterResource(
                id = R.drawable.fitness_background
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Very light overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White.copy(alpha = 0.12f)
                )
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,

            topBar = {

                TopAppBar(
                    title = {
                        Text("Settings")
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }

        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                // -------------------------------------------------
                // DAILY REMINDER
                // -------------------------------------------------

                SettingsSectionCard {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Column {

                            Text(
                                text = "Daily Reminder",
                                style =
                                    MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text =
                                    "Receive a daily reminder for your fitness goals.",
                                style =
                                    MaterialTheme.typography.bodySmall,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            checked = reminderEnabled,

                            onCheckedChange = {

                                reminderEnabled = it

                                viewModel.updateReminder(
                                    it,
                                    reminderTime
                                )
                            }
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    OutlinedTextField(
                        value = reminderTime,

                        onValueChange = {

                            reminderTime = it

                            if (reminderEnabled) {

                                viewModel.updateReminder(
                                    true,
                                    it
                                )
                            }
                        },

                        label = {
                            Text(
                                "Reminder Time (HH:mm)"
                            )
                        },

                        singleLine = true,

                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // -------------------------------------------------
                // UNITS
                // -------------------------------------------------

                SettingsSectionCard {

                    Text(
                        text = "Measurement Units",
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "Choose how measurements are displayed.",
                        style =
                            MaterialTheme.typography.bodySmall,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            RadioButton(
                                selected =
                                    units == "metric",

                                onClick = {

                                    units = "metric"

                                    viewModel.updateUnits(
                                        "metric"
                                    )
                                }
                            )

                            Text(
                                text = "Metric",
                                modifier =
                                    Modifier.padding(
                                        start = 4.dp,
                                        top = 12.dp
                                    )
                            )
                        }

                        Row(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            RadioButton(
                                selected =
                                    units == "imperial",

                                onClick = {

                                    units = "imperial"

                                    viewModel.updateUnits(
                                        "imperial"
                                    )
                                }
                            )

                            Text(
                                text = "Imperial",
                                modifier =
                                    Modifier.padding(
                                        start = 4.dp,
                                        top = 12.dp
                                    )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    content: @Composable ColumnScope.() -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        colors = CardDefaults.cardColors(
            containerColor =
                Color.White.copy(
                    alpha = 0.92f
                )
        ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp),

            content = content
        )
    }
}