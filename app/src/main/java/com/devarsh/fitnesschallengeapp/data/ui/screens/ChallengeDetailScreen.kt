package com.devarsh.fitnesschallengeapp.data.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.devarsh.fitnesschallengeapp.R
import com.devarsh.fitnesschallengeapp.data.entity.Challenge
import com.devarsh.fitnesschallengeapp.data.ui.viewmodel.ProgressViewModel
import com.devarsh.fitnesschallengeapp.data.ui.viewmodel.WalkingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeDetailScreen(
    challengeId: Int,
    viewModel: ProgressViewModel,
    walkingViewModel: WalkingViewModel,
    navController: NavController
) {

    var challenge by remember {
        mutableStateOf<Challenge?>(null)
    }

    var input by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    val stepsToday by walkingViewModel.stepsToday
        .collectAsStateWithLifecycle()

    val sensorAvailable by walkingViewModel.sensorAvailable
        .collectAsStateWithLifecycle()

    val isTracking by walkingViewModel.isTracking
        .collectAsStateWithLifecycle()

    LaunchedEffect(challengeId) {
        challenge = viewModel.getChallengeById(challengeId)
        isLoading = false
    }

    DisposableEffect(challenge?.category) {

        if (challenge?.category == "WALKING") {
            walkingViewModel.startTracking()
        }

        onDispose {
            if (challenge?.category == "WALKING") {
                walkingViewModel.stopTracking()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.fitness_background
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,

            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = challenge?.title ?: "Challenge"
                        )
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

            when {

                isLoading -> {

                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator()
                    }
                }

                challenge == null -> {

                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    Color.White.copy(alpha = 0.92f)
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            )
                        ) {

                            Text(
                                text = "Challenge not found",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }

                else -> {

                    val currentChallenge = challenge!!

                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            )
                            .padding(16.dp)
                    ) {

                        // -----------------------------------------
                        // TITLE / DESCRIPTION
                        // -----------------------------------------

                        Text(
                            text = currentChallenge.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFF252936)
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = currentChallenge.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF5F6678)
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        // -----------------------------------------
                        // CHALLENGE DETAILS
                        // -----------------------------------------

                        Card(
                            modifier = Modifier.fillMaxWidth(),

                            colors = CardDefaults.cardColors(
                                containerColor =
                                    Color.White.copy(alpha = 0.92f)
                            ),

                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            ),

                            shape = MaterialTheme.shapes.large
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = "Challenge Details",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF252936)
                                )

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )

                                Text(
                                    text =
                                        "Target: ${currentChallenge.target} " +
                                                currentChallenge.unit
                                )

                                Text(
                                    text =
                                        "Difficulty: ${currentChallenge.difficulty}"
                                )

                                Text(
                                    text =
                                        "Points: ${currentChallenge.points}"
                                )

                                Text(
                                    text =
                                        "Category: ${currentChallenge.category}"
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        // -----------------------------------------
                        // WALKING
                        // -----------------------------------------

                        if (currentChallenge.category == "WALKING") {

                            WalkingProgressSection(
                                challenge = currentChallenge,
                                stepsToday = stepsToday,
                                sensorAvailable = sensorAvailable,
                                isTracking = isTracking
                            )

                        } else {

                            // -------------------------------------
                            // MANUAL MODE
                            // -------------------------------------

                            Card(
                                modifier = Modifier.fillMaxWidth(),

                                colors = CardDefaults.cardColors(
                                    containerColor =
                                        Color.White.copy(alpha = 0.92f)
                                ),

                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 3.dp
                                ),

                                shape = MaterialTheme.shapes.large
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = "Progress",
                                        style =
                                            MaterialTheme.typography.titleLarge,
                                        color = Color(0xFF252936)
                                    )

                                    Spacer(
                                        modifier = Modifier.height(12.dp)
                                    )

                                    OutlinedTextField(
                                        value = input,

                                        onValueChange = {
                                            input =
                                                it.filter { character ->
                                                    character.isDigit()
                                                }
                                        },

                                        label = {
                                            Text("Enter your progress")
                                        },

                                        keyboardOptions =
                                            KeyboardOptions(
                                                keyboardType =
                                                    KeyboardType.Number
                                            ),

                                        singleLine = true,

                                        modifier =
                                            Modifier.fillMaxWidth()
                                    )

                                    Spacer(
                                        modifier = Modifier.height(16.dp)
                                    )

                                    Button(
                                        onClick = {

                                            val value =
                                                input.toIntOrNull() ?: 0

                                            viewModel.recordProgress(
                                                challengeId =
                                                    currentChallenge.id,

                                                value = value,

                                                target =
                                                    currentChallenge.target
                                            )

                                            navController.popBackStack()
                                        },

                                        enabled =
                                            input.toIntOrNull() != null,

                                        modifier =
                                            Modifier.fillMaxWidth()
                                    ) {

                                        Text("Log Progress")
                                    }
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(32.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun WalkingProgressSection(
    challenge: Challenge,
    stepsToday: Int,
    sensorAvailable: Boolean,
    isTracking: Boolean
) {

    val target =
        challenge.target.coerceAtLeast(1)

    val progress =
        (stepsToday.toFloat() / target.toFloat())
            .coerceIn(0f, 1f)

    val remaining =
        (target - stepsToday)
            .coerceAtLeast(0)

    val completed =
        stepsToday >= target

    Text(
        text = "Walking Progress",
        style = MaterialTheme.typography.titleLarge,
        color = Color(0xFF252936)
    )

    Spacer(
        modifier = Modifier.height(12.dp)
    )

    // ---------------------------------------------
    // SENSOR NOT AVAILABLE
    // ---------------------------------------------

    if (!sensorAvailable) {

        Card(
            modifier = Modifier.fillMaxWidth(),

            colors = CardDefaults.cardColors(
                containerColor =
                    Color.White.copy(alpha = 0.92f)
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),

            shape = MaterialTheme.shapes.large
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Step sensor unavailable",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text =
                        "This device does not provide a step counter sensor. " +
                                "Manual walking progress will be added as a fallback.",
                    color = Color(0xFF5F6678)
                )
            }
        }

        return
    }

    // ---------------------------------------------
    // SENSOR AVAILABLE
    // ---------------------------------------------

    Card(
        modifier = Modifier.fillMaxWidth(),

        colors = CardDefaults.cardColors(
            containerColor =
                Color.White.copy(alpha = 0.92f)
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),

        shape = MaterialTheme.shapes.large
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.DirectionsWalk,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Steps Today",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = stepsToday.toString(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text =
                    if (isTracking) {
                        "Tracking automatically"
                    } else {
                        "Waiting for step sensor"
                    },

                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF687086)
            )
        }
    }

    Spacer(
        modifier = Modifier.height(16.dp)
    )

    LinearProgressIndicator(
        progress = { progress },

        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
    )

    Spacer(
        modifier = Modifier.height(8.dp)
    )

    Text(
        text =
            "$stepsToday / ${challenge.target} ${challenge.unit}",

        style = MaterialTheme.typography.bodyLarge,
        color = Color(0xFF252936)
    )

    Spacer(
        modifier = Modifier.height(4.dp)
    )

    if (completed) {

        Text(
            text = "Challenge completed!",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

    } else {

        Text(
            text = "$remaining steps remaining",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF687086)
        )
    }
}