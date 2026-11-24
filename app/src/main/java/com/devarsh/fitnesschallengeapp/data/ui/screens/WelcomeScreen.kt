package com.devarsh.fitnesschallengeapp.data.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devarsh.fitnesschallengeapp.R

@Composable
fun WelcomeScreen(
    navController: NavController
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // ---------------------------------------------------------
        // BACKGROUND
        // ---------------------------------------------------------

        Image(
            painter = painterResource(
                id = R.drawable.fitness_background
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ---------------------------------------------------------
        // MAIN CONTENT
        // ---------------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 28.dp,
                    vertical = 32.dp
                ),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // -----------------------------------------------------
            // APP ICON
            // -----------------------------------------------------

            Surface(
                modifier = Modifier.size(90.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = Color.White.copy(alpha = 0.90f),
                shadowElevation = 6.dp
            ) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // -----------------------------------------------------
            // TITLE
            // -----------------------------------------------------

            Text(
                text = "Fitness Challenge",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF252936),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Build healthy habits, one challenge at a time.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF5F6678),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Track your progress, complete challenges and stay motivated every day.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF687086),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(48.dp)
            )

            // -----------------------------------------------------
            // GET STARTED
            // -----------------------------------------------------

            Button(
                onClick = {
                    navController.navigate("profile")
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {

                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}