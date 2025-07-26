package com.example.mapster.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapster.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    onScanClick: () -> Unit,
    onUploadClick: () -> Unit,
    onLogoutIconClick: () -> Unit,
    onReportClick: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.5f)) {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                Text(
                    text = "Logout",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch { drawerState.close() }
                            onLogoutIconClick()
                        }
                        .padding(16.dp)
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
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
                    },
                    actions = {
                        IconButton(onClick = onReportClick) {
                            Icon(
                                imageVector = Icons.Default.QuestionAnswer,
                                contentDescription = null,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                modifier = Modifier.padding(end = 8.dp)
                            )
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.landing_screen_bg_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        onClick = { onScanClick() }) {
                        Text(
                            text = "Scan QR Code",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontFamily = FontFamily(
                                    listOf(Font(R.font.roboto_regular))
                                )
                            )
                        )
                    }

                    Button(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        onClick = { onUploadClick() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Upload QR Image",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontFamily = FontFamily(listOf(Font(R.font.roboto_regular)))
                            )
                        )
                    }
                }
            }
        }
    }
}