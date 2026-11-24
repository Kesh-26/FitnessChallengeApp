package com.devarsh.fitnesschallengeapp.data.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class WeekDayData(
    val dateKey: String,
    val dayLabel: String
)

@Composable
fun WeeklyActivityChart(
    data: Map<String, Int>,
    modifier: Modifier = Modifier
) {

    val weekDays = getLastSevenDays()

    val hasActivity = weekDays.any { day ->
        (data[day.dateKey] ?: 0) > 0
    }

    val maxValue = weekDays
        .map { day ->
            data[day.dateKey] ?: 0
        }
        .maxOrNull()
        ?.coerceAtLeast(1)
        ?: 1

    // Light pastel colours to match the application
    val primaryColor = Color(0xFF586FA8)
    val emptyBarColor = Color(0xFFDDE3F0)
    val titleColor = Color(0xFF252936)
    val labelColor = Color(0xFF687086)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(
                alpha = 0.92f
            )
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),

        shape = MaterialTheme.shapes.large
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Weekly Activity",
                style = MaterialTheme.typography.titleMedium,
                color = titleColor
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (!hasActivity) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = null,
                            modifier = Modifier.size(38.dp),
                            tint = primaryColor
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "No activity recorded yet",
                            style = MaterialTheme.typography.titleSmall,
                            color = titleColor
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Complete a challenge to see your weekly progress.",
                            style = MaterialTheme.typography.bodySmall,
                            color = labelColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                ) {

                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        val barWidth =
                            size.width / 7f * 0.55f

                        val spacing =
                            size.width / 7f

                        weekDays.forEachIndexed { index, day ->

                            val count =
                                data[day.dateKey] ?: 0

                            val barHeight =
                                if (count > 0) {
                                    (count.toFloat() / maxValue) *
                                            size.height *
                                            0.82f
                                } else {
                                    6.dp.toPx()
                                }

                            val x =
                                index * spacing +
                                        (spacing - barWidth) / 2

                            val y =
                                size.height - barHeight

                            drawRoundRect(
                                color =
                                    if (count > 0) {
                                        primaryColor
                                    } else {
                                        emptyBarColor
                                    },

                                topLeft = Offset(
                                    x = x,
                                    y = y
                                ),

                                size = Size(
                                    width = barWidth,
                                    height = barHeight
                                ),

                                cornerRadius = CornerRadius(
                                    x = 6.dp.toPx(),
                                    y = 6.dp.toPx()
                                )
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    weekDays.forEach { day ->

                        Text(
                            text = day.dayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = labelColor
                        )
                    }
                }
            }
        }
    }
}

private fun getLastSevenDays(): List<WeekDayData> {

    val dateFormatter =
        SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        )

    val dayFormatter =
        SimpleDateFormat(
            "EEE",
            Locale.getDefault()
        )

    val today =
        Calendar.getInstance()

    return (6 downTo 0).map { daysAgo ->

        val calendar =
            today.clone() as Calendar

        calendar.add(
            Calendar.DAY_OF_YEAR,
            -daysAgo
        )

        WeekDayData(
            dateKey =
                dateFormatter.format(
                    calendar.time
                ),

            dayLabel =
                dayFormatter
                    .format(
                        calendar.time
                    )
                    .take(1)
        )
    }
}