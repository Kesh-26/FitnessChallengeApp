package com.devarsh.fitnesschallengeapp.data.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.devarsh.fitnesschallengeapp.R
import com.devarsh.fitnesschallengeapp.data.entity.Badge
import com.devarsh.fitnesschallengeapp.data.repository.BadgeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeScreen(
    repository: BadgeRepository,
    navController: NavController
) {

    val badges by repository.allBadges.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    val unlocked by repository.unlockedBadges.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    val unlockedIds =
        unlocked.map { it.id }.toSet()

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
                        Text("Badges")
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

            if (badges.isEmpty()) {

                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),

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
                                .padding(24.dp),

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.EmojiEvents,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint =
                                    MaterialTheme.colorScheme.primary
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text(
                                text = "No badges available",
                                style =
                                    MaterialTheme.typography.titleMedium,
                                color = Color(0xFF252936)
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    "Complete challenges to start earning badges.",
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF687086)
                            )
                        }
                    }
                }

            } else {

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {

                    Text(
                        text =
                            "${unlockedIds.size} of ${badges.size} badges unlocked",

                        style =
                            MaterialTheme.typography.titleMedium,

                        color = Color(0xFF252936),

                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 12.dp
                        )
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp),

                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp),

                        contentPadding =
                            PaddingValues(bottom = 20.dp)
                    ) {

                        items(
                            items = badges,
                            key = { it.id }
                        ) { badge ->

                            BadgeItem(
                                badge = badge,
                                isUnlocked =
                                    unlockedIds.contains(
                                        badge.id
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun BadgeItem(
    badge: Badge,
    isUnlocked: Boolean
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                if (isUnlocked) {
                    Color(0xFFF2E9C8).copy(
                        alpha = 0.95f
                    )
                } else {
                    Color.White.copy(
                        alpha = 0.92f
                    )
                }
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),

        shape = MaterialTheme.shapes.large
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            if (isUnlocked) {

                Icon(
                    imageVector =
                        Icons.Default.EmojiEvents,

                    contentDescription =
                        "Unlocked badge",

                    modifier =
                        Modifier.size(38.dp),

                    tint = Color(0xFFD09A2D)
                )

            } else {

                Icon(
                    imageVector =
                        Icons.Default.Lock,

                    contentDescription =
                        "Locked badge",

                    modifier =
                        Modifier.size(32.dp),

                    tint = Color(0xFF7A8090)
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = badge.name,
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF252936)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = badge.description,
                style =
                    MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color(0xFF687086)
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text =
                    if (isUnlocked) {
                        "Unlocked"
                    } else {
                        "Locked"
                    },

                style =
                    MaterialTheme.typography.labelMedium,

                color =
                    if (isUnlocked) {
                        Color(0xFFD09A2D)
                    } else {
                        Color(0xFF7A8090)
                    }
            )
        }
    }
}