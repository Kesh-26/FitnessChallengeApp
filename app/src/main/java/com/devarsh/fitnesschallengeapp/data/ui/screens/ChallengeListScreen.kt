package com.devarsh.fitnesschallengeapp.data.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.devarsh.fitnesschallengeapp.R
import com.devarsh.fitnesschallengeapp.data.entity.Challenge
import com.devarsh.fitnesschallengeapp.data.ui.viewmodel.ChallengeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeListScreen(
    viewModel: ChallengeViewModel,
    navController: NavController
) {

    val challenges by viewModel.challenges.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val categories = listOf(
        "ALL",
        "WALKING",
        "STRENGTH",
        "FLEXIBILITY",
        "HABIT"
    )

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
                .padding(0.dp)
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,

            topBar = {

                TopAppBar(
                    title = {
                        Text("Challenges")
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
                    .fillMaxSize()
            ) {

                // -------------------------------------------------
                // CATEGORY TABS
                // -------------------------------------------------

                ScrollableTabRow(
                    selectedTabIndex =
                        categories.indexOf(selectedCategory),

                    modifier = Modifier.fillMaxWidth(),

                    edgePadding = 8.dp,

                    containerColor =
                        Color.White.copy(alpha = 0.88f),

                    divider = {}
                ) {

                    categories.forEach { category ->

                        Tab(
                            selected =
                                selectedCategory == category,

                            onClick = {
                                viewModel.setCategory(category)
                            },

                            modifier =
                                Modifier.widthIn(min = 90.dp),

                            text = {

                                Text(
                                    text = category,
                                    maxLines = 1
                                )
                            }
                        )
                    }
                }

                // -------------------------------------------------
                // FILTER CHALLENGES
                // -------------------------------------------------

                val filteredChallenges =
                    if (selectedCategory == "ALL") {
                        challenges
                    } else {
                        challenges.filter {
                            it.category == selectedCategory
                        }
                    }

                // -------------------------------------------------
                // CHALLENGE LIST
                // -------------------------------------------------

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),

                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 24.dp
                    ),

                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    items(
                        items = filteredChallenges,
                        key = { it.id }
                    ) { challenge ->

                        ChallengeCard(
                            challenge = challenge,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ChallengeCard(
    challenge: Challenge,
    navController: NavController
) {

    Card(
        onClick = {

            navController.navigate(
                "challenge_detail/${challenge.id}"
            )
        },

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
                text = challenge.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF252936)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    "${challenge.target} ${challenge.unit} | " +
                            "${challenge.difficulty} | " +
                            "${challenge.points} pts",

                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F6678)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6D7484)
            )
        }
    }
}