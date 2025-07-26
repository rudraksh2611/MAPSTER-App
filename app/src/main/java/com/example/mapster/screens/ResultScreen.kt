package com.example.mapster.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapster.FloorNavigation
import com.example.mapster.QRScannerViewModel
import com.example.mapster.R

val redHatDisplayFontFamily =
    FontFamily(listOf(Font(R.font.redhatdisplay_regular), Font(R.font.redhatdisplay_semibold)))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(result: String, destination: String, onBackClick: () -> Unit) {
    val viewModel = remember { QRScannerViewModel() }
    calculatePath(result, destination, viewModel)
    val calculatedPath = viewModel.calculatedPath.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "MAPSTER",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(
                                    listOf(Font(R.font.poppins_semi_bold))
                                )
                            ),
                            textAlign = TextAlign.Center
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                                    contentDescription = "Back"
                                )
                            }
                            Text(
                                text = "Back",
                                style = TextStyle(
                                    fontSize = 14.sp, fontFamily = FontFamily(
                                        listOf(Font(R.font.poppins_regular))
                                    )
                                )
                            )
                        }
                    }

                },
                colors = TopAppBarColors(
                    containerColor = Color.DarkGray,
                    scrolledContainerColor = Color(30, 31, 38),
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            Text(
                modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                text = "Steps To Follow",
                fontSize = 24.sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            if (calculatedPath != null) {
                NavigationPathDisplay(calculatedPath, PaddingValues(8.dp))
            }

        }
    }
}

private fun calculatePath(
    currentLocation: String,
    endDestination: String,
    viewModel: QRScannerViewModel
) {
    val calculatedPath = FloorNavigation().navigate(
        currentLocation,
        endDestination
    )
    viewModel.onPathCalculation(calculatedPath)
}

@Composable
fun NavigationPathDisplay(path: List<List<String>>, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        items(path) { step ->
            NavigationItem(step)
        }
    }
}

@Composable
fun NavigationItem(step: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Step number
        Text(
            text = step[0],
            style = TextStyle(
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = redHatDisplayFontFamily
            ),
            modifier = Modifier.padding(end = 16.dp)
        )

        // Direction text
        Text(
            text = step[1],
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = redHatDisplayFontFamily
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )

        // Distance
        Box(
            modifier = Modifier
                .background(Color(0xFFE0E0E0))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "${step[2]} m",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(listOf(Font(R.font.poppins_regular)))
                )
            )
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.Gray
    )
}