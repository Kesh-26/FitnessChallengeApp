package com.devarsh.fitnesschallengeapp.data.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.devarsh.fitnesschallengeapp.R
import com.devarsh.fitnesschallengeapp.data.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }

    var nameTouched by remember { mutableStateOf(false) }
    var ageTouched by remember { mutableStateOf(false) }
    var heightTouched by remember { mutableStateOf(false) }
    var weightTouched by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        user?.let {
            name = it.name

            age =
                if (it.age > 0) {
                    it.age.toString()
                } else {
                    ""
                }

            height =
                if (it.heightCm > 0) {
                    it.heightCm.toString()
                } else {
                    ""
                }

            weight =
                if (it.weightKg > 0f) {
                    it.weightKg.toString()
                } else {
                    ""
                }

            goal = it.goal
        }
    }

    val ageValue = age.toIntOrNull()
    val heightValue = height.toIntOrNull()
    val weightValue = weight.toFloatOrNull()

    val nameError =
        nameTouched && name.isBlank()

    val ageError =
        ageTouched &&
                (ageValue == null || ageValue !in 10..120)

    val heightError =
        heightTouched &&
                (heightValue == null || heightValue !in 50..300)

    val weightError =
        weightTouched &&
                (weightValue == null || weightValue !in 20f..500f)

    val isFormValid =
        name.isNotBlank() &&
                ageValue != null &&
                ageValue in 10..120 &&
                heightValue != null &&
                heightValue in 50..300 &&
                weightValue != null &&
                weightValue in 20f..500f

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->

            bitmap?.let {
                viewModel.saveProfilePhoto(
                    it,
                    context
                )
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

        // Very light overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White.copy(alpha = 0.10f)
                )
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,

            topBar = {
                TopAppBar(
                    title = {
                        Text("Your Profile")
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                )
            }

        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // -------------------------------------------------
                // PROFILE PHOTO
                // -------------------------------------------------

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color.White.copy(alpha = 0.92f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.BottomEnd
                        ) {

                            val photoBitmap =
                                remember(user?.photoPath) {
                                    user?.photoPath?.let {
                                        loadBitmap(it)
                                    }
                                }

                            if (photoBitmap != null) {

                                Image(
                                    bitmap =
                                        photoBitmap.asImageBitmap(),
                                    contentDescription =
                                        "Profile photo",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    contentScale =
                                        ContentScale.Crop
                                )

                            } else {

                                Surface(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    color =
                                        Color(0xFFE8EDF7)
                                ) {

                                    Box(
                                        contentAlignment =
                                            Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector =
                                                Icons.Default.CameraAlt,
                                            contentDescription = null,
                                            modifier =
                                                Modifier.size(46.dp),
                                            tint =
                                                MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            FilledIconButton(
                                onClick = {
                                    cameraLauncher.launch(null)
                                },
                                modifier =
                                    Modifier.size(40.dp),
                                shape = CircleShape
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.CameraAlt,
                                    contentDescription =
                                        "Take photo",
                                    modifier =
                                        Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = "Profile Photo",
                            style =
                                MaterialTheme.typography.titleSmall
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // -------------------------------------------------
                // PROFILE FORM
                // -------------------------------------------------

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color.White.copy(alpha = 0.92f)
                    ),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        OutlinedTextField(
                            value = name,

                            onValueChange = {
                                name = it
                                nameTouched = true
                            },

                            label = {
                                Text("Name")
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            isError = nameError,

                            supportingText = {
                                if (nameError) {
                                    Text(
                                        "Name is required"
                                    )
                                }
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        OutlinedTextField(
                            value = age,

                            onValueChange = {
                                age =
                                    it.filter { c ->
                                        c.isDigit()
                                    }

                                ageTouched = true
                            },

                            label = {
                                Text("Age (years)")
                            },

                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Number
                                ),

                            modifier =
                                Modifier.fillMaxWidth(),

                            isError = ageError,

                            supportingText = {
                                if (ageError) {
                                    Text(
                                        "Age must be between 10 and 120"
                                    )
                                }
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        OutlinedTextField(
                            value = height,

                            onValueChange = {
                                height =
                                    it.filter { c ->
                                        c.isDigit()
                                    }

                                heightTouched = true
                            },

                            label = {
                                Text("Height (cm)")
                            },

                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Number
                                ),

                            modifier =
                                Modifier.fillMaxWidth(),

                            isError = heightError,

                            supportingText = {
                                if (heightError) {
                                    Text(
                                        "Height must be between 50 and 300 cm"
                                    )
                                }
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        OutlinedTextField(
                            value = weight,

                            onValueChange = {
                                weight =
                                    it.filter { c ->
                                        c.isDigit() ||
                                                c == '.'
                                    }

                                weightTouched = true
                            },

                            label = {
                                Text("Weight (kg)")
                            },

                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Decimal
                                ),

                            modifier =
                                Modifier.fillMaxWidth(),

                            isError = weightError,

                            supportingText = {
                                if (weightError) {
                                    Text(
                                        "Weight must be between 20 and 500 kg"
                                    )
                                }
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        OutlinedTextField(
                            value = goal,

                            onValueChange = {
                                goal = it
                            },

                            label = {
                                Text(
                                    "Fitness Goal (optional)"
                                )
                            },

                            modifier =
                                Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // -------------------------------------------------
                // BMI
                // -------------------------------------------------

                val bmi =
                    if (
                        heightValue != null &&
                        heightValue > 0 &&
                        weightValue != null &&
                        weightValue > 0f
                    ) {

                        viewModel.calculateBMI(
                            heightValue,
                            weightValue
                        )

                    } else {
                        null
                    }

                bmi?.let {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),

                        colors =
                            CardDefaults.cardColors(
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
                                Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = "Body Mass Index",
                                style =
                                    MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text =
                                    "BMI: %.1f".format(it),
                                style =
                                    MaterialTheme.typography.headlineSmall,
                                color =
                                    MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )
                }

                // -------------------------------------------------
                // SAVE BUTTON
                // -------------------------------------------------

                Button(
                    onClick = {

                        if (isFormValid) {

                            viewModel.saveProfile(
                                name =
                                    name.trim(),

                                age =
                                    ageValue!!,

                                heightCm =
                                    heightValue!!,

                                weightKg =
                                    weightValue!!,

                                goal =
                                    goal.trim()
                            )

                            navController.navigate(
                                "dashboard"
                            ) {

                                popUpTo("profile") {
                                    inclusive = true
                                }
                            }
                        }
                    },

                    enabled =
                        isFormValid,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {

                    Text(
                        "Save & Continue"
                    )
                }

                Spacer(
                    modifier = Modifier.height(32.dp)
                )
            }
        }
    }
}

private fun loadBitmap(
    path: String
): Bitmap? {

    return try {
        BitmapFactory.decodeFile(path)
    } catch (e: Exception) {
        null
    }
}