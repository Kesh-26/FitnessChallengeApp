package com.devarsh.fitnesschallengeapp.data.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.devarsh.fitnesschallengeapp.R
import com.devarsh.fitnesschallengeapp.data.ui.components.WeeklyActivityChart
import com.devarsh.fitnesschallengeapp.data.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navController: NavController
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    val points by viewModel.totalPoints.collectAsStateWithLifecycle()
    val completed by viewModel.totalCompleted.collectAsStateWithLifecycle()
    val streak by viewModel.currentStreak.collectAsStateWithLifecycle()
    val badges by viewModel.badgeCount.collectAsStateWithLifecycle()
    val weeklyData by viewModel.weeklyActivity.collectAsStateWithLifecycle()

    /*
     * Detect whether the current Material theme is dark.
     * This lets us keep the same background image without
     * destroying readability in dark mode.
     */
    val isDarkTheme =
        MaterialTheme.colorScheme.background.luminance() < 0.5f

    val screenTextColor =
        if (isDarkTheme) {
            MaterialTheme.colorScheme.onBackground
        } else {
            Color(0xFF252936)
        }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // ---------------------------------------------------------
        // BACKGROUND IMAGE
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
        // BACKGROUND OVERLAY
        //
        // Light mode:
        // Only a tiny white layer because the image is already light.
        //
        // Dark mode:
        // Darken the picture significantly.
        // ---------------------------------------------------------

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isDarkTheme) {
                        Color.Black.copy(alpha = 0.78f)
                    } else {
                        Color.White.copy(alpha = 0.08f)
                    }
                )
        )

        // ---------------------------------------------------------
        // MAIN SCREEN
        // ---------------------------------------------------------

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,

            // -----------------------------------------------------
            // TOP BAR
            // -----------------------------------------------------

            topBar = {

                TopAppBar(
                    title = {

                        Text(
                            text = "Dashboard",
                            color = screenTextColor
                        )
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = screenTextColor
                    )
                )
            },

            // -----------------------------------------------------
            // BOTTOM NAVIGATION
            // -----------------------------------------------------

            bottomBar = {

                NavigationBar(
                    containerColor =
                        MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.94f
                        )
                ) {

                    NavigationBarItem(
                        selected = true,

                        onClick = {
                            // Already on Home
                        },

                        icon = {

                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        },

                        label = {
                            Text("Home")
                        }
                    )

                    NavigationBarItem(
                        selected = false,

                        onClick = {
                            navController.navigate("challenges")
                        },

                        icon = {

                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "Challenges"
                            )
                        },

                        label = {
                            Text("Challenges")
                        }
                    )

                    NavigationBarItem(
                        selected = false,

                        onClick = {
                            navController.navigate("badges")
                        },

                        icon = {

                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Badges"
                            )
                        },

                        label = {
                            Text("Badges")
                        }
                    )

                    NavigationBarItem(
                        selected = false,

                        onClick = {
                            navController.navigate("settings")
                        },

                        icon = {

                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        },

                        label = {
                            Text("Settings")
                        }
                    )
                }
            }

        ) { innerPadding ->

            // -----------------------------------------------------
            // SCROLLABLE DASHBOARD CONTENT
            // -----------------------------------------------------

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 24.dp
                    )
            ) {

                // -------------------------------------------------
                // GREETING
                // -------------------------------------------------

                user?.let { currentUser ->

                    Text(
                        text = "Hello, ${currentUser.name}!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = screenTextColor
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )
                }

                // -------------------------------------------------
                // STATISTIC CARDS
                // -------------------------------------------------

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    DashboardStatCard(
                        label = "Points",
                        value = points.toString(),
                        icon = Icons.Default.Star
                    )

                    DashboardStatCard(
                        label = "Done",
                        value = completed.toString(),
                        icon = Icons.Default.CheckCircle
                    )

                    DashboardStatCard(
                        label = "Streak",
                        value = streak.toString(),
                        icon = Icons.Default.LocalFireDepartment
                    )

                    DashboardStatCard(
                        label = "Badges",
                        value = badges.toString(),
                        icon = Icons.Default.EmojiEvents
                    )
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // -------------------------------------------------
                // WEEKLY ACTIVITY
                //
                // Important:
                // No extra Card around WeeklyActivityChart.
                // The component already provides its own layout.
                // -------------------------------------------------

                WeeklyActivityChart(
                    data = weeklyData
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                // -------------------------------------------------
                // TODAY'S CHALLENGES
                // -------------------------------------------------

                Text(
                    text = "Today's Challenges",
                    style = MaterialTheme.typography.titleLarge,
                    color = screenTextColor
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    onClick = {
                        navController.navigate("challenges")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {

                    Text(
                        text = "Browse Challenges"
                    )
                }

                /*
                 * Extra breathing room at the bottom.
                 */
                Spacer(
                    modifier = Modifier.height(32.dp)
                )
            }
        }
    }
}


// -----------------------------------------------------------------
// DASHBOARD STAT CARD
// -----------------------------------------------------------------

@Composable
private fun DashboardStatCard(
    label: String,
    value: String,
    icon: ImageVector
) {

    Card(
        modifier = Modifier
            .width(76.dp)
            .height(82.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface.copy(
                    alpha = 0.93f
                )
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),

        shape = MaterialTheme.shapes.medium
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}