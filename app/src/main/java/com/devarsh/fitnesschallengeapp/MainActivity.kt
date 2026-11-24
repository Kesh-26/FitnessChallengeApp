package com.devarsh.fitnesschallengeapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devarsh.fitnesschallengeapp.data.database.DatabaseProvider
import com.devarsh.fitnesschallengeapp.data.entity.Settings
import com.devarsh.fitnesschallengeapp.data.repository.*
import com.devarsh.fitnesschallengeapp.data.ui.screens.*
import com.devarsh.fitnesschallengeapp.data.ui.theme.FitnessChallengeTheme
import com.devarsh.fitnesschallengeapp.data.ui.viewmodel.*
import com.devarsh.fitnesschallengeapp.data.util.AuthManager
import com.devarsh.fitnesschallengeapp.data.util.StreakCalculator
import com.devarsh.fitnesschallengeapp.notification.ReminderScheduler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private lateinit var authManager: AuthManager

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    private val activityRecognitionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authManager = AuthManager(this)

        // Request CAMERA permission
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            cameraPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }

        // Request POST_NOTIFICATIONS permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }

        // Request ACTIVITY_RECOGNITION permission (Android 10+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activityRecognitionLauncher.launch(
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
            }
        }

        // ---------------------------------------------------------
        // DATABASE
        // ---------------------------------------------------------

        val db = DatabaseProvider.getDatabase(this)

        // ---------------------------------------------------------
        // REPOSITORIES
        // ---------------------------------------------------------

        val userRepo =
            UserRepository(
                db.userDao()
            )

        val challengeRepo =
            ChallengeRepository(
                db.challengeDao()
            )

        val progressRepo =
            ProgressRepository(
                db.progressDao()
            )

        val badgeRepo =
            BadgeRepository(
                db.badgeDao()
            )

        val settingsRepo =
            SettingsRepository(
                db.settingsDao()
            )

        val dailyStepsRepo =
            DailyStepsRepository(
                db.dailyStepsDao()
            )

        // ---------------------------------------------------------
        // UTILITIES
        // ---------------------------------------------------------

        val streakCalculator =
            StreakCalculator(
                db.progressDao()
            )

        // ---------------------------------------------------------
        // VIEWMODELS
        // ---------------------------------------------------------

        val authVm =
            AuthViewModel(
                authManager,
                userRepo
            )

        val profileVm =
            ProfileViewModel(
                userRepo
            )

        val challengeVm =
            ChallengeViewModel(
                challengeRepo,
                badgeRepo
            )

        val progressVm =
            ProgressViewModel(
                application,
                progressRepo,
                badgeRepo,
                challengeRepo
            )

        val dashboardVm =
            DashboardViewModel(
                userRepo,
                progressRepo,
                badgeRepo,
                streakCalculator
            )

        val settingsVm =
            SettingsViewModel(
                application,
                settingsRepo
            )

        val walkingVm =
            WalkingViewModel(
                application,
                dailyStepsRepo
            )

        // ---------------------------------------------------------
        // SEED DEFAULT APPLICATION DATA
        // ---------------------------------------------------------

        challengeVm.seedData()
        challengeVm.seedBadges()

        // ---------------------------------------------------------
        // SCHEDULE DAILY REMINDER
        // ---------------------------------------------------------

        lifecycleScope.launch {

            val settings =
                settingsRepo.settings.first()

            settings?.let {

                ReminderScheduler.schedule(
                    this@MainActivity,
                    it.reminderTime,
                    it.reminderEnabled
                )

            } ?: run {

                settingsRepo.save(
                    Settings()
                )

                ReminderScheduler.schedule(
                    this@MainActivity,
                    "08:00",
                    true
                )
            }
        }

        // ---------------------------------------------------------
        // START DESTINATION
        // ---------------------------------------------------------

        val startDestination =
            when {

                !authManager.isLoggedIn() -> {
                    "login"
                }

                else -> {

                    val hasProfile =
                        runBlocking {
                            userRepo.user.first()
                        }

                    if (
                        hasProfile != null &&
                        hasProfile.name.isNotBlank()
                    ) {
                        "dashboard"
                    } else {
                        "profile"
                    }
                }
            }

        // ---------------------------------------------------------
        // COMPOSE UI
        // ---------------------------------------------------------

        setContent {

            FitnessChallengeTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController =
                        rememberNavController()

                    NavHost(
                        navController =
                            navController,

                        startDestination =
                            startDestination
                    ) {

                        // LOGIN
                        composable("login") {

                            LoginScreen(
                                authVm,
                                navController
                            )
                        }

                        // REGISTER
                        composable("register") {

                            RegisterScreen(
                                authVm,
                                navController
                            )
                        }

                        // WELCOME
                        composable("welcome") {

                            WelcomeScreen(
                                navController
                            )
                        }

                        // PROFILE
                        composable("profile") {

                            ProfileScreen(
                                profileVm,
                                navController
                            )
                        }

                        // DASHBOARD
                        composable("dashboard") {

                            DashboardScreen(
                                dashboardVm,
                                navController
                            )
                        }

                        // CHALLENGE LIST
                        composable("challenges") {

                            ChallengeListScreen(
                                challengeVm,
                                navController
                            )
                        }

                        // CHALLENGE DETAILS
                        composable(
                            "challenge_detail/{id}"
                        ) { backStackEntry ->

                            val id =
                                backStackEntry
                                    .arguments
                                    ?.getString("id")
                                    ?.toIntOrNull()
                                    ?: 0

                            ChallengeDetailScreen(
                                challengeId = id,
                                viewModel = progressVm,
                                walkingViewModel = walkingVm,
                                navController = navController
                            )
                        }

                        // BADGES
                        composable("badges") {

                            BadgeScreen(
                                badgeRepo,
                                navController
                            )
                        }

                        // SETTINGS
                        composable("settings") {

                            SettingsScreen(
                                viewModel = settingsVm,
                                navController = navController,
                                onLogout = {
                                    authVm.logout()
                                    navController.navigate("login") {
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}