package com.example.mapster.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapster.QRScannerViewModel
import com.example.mapster.R
import kotlinx.coroutines.launch

val indoorLocations = listOf(
    "306 IPDC Lab",
    "307A Classroom",
    "307B Control Lab",
    "308A Classroom",
    "309A Classroom",
    "309B Analog Lab",
    "Washroom",
    "310 Classroom",
    "311 Faculty Room",
    "312A Classroom",
    "313A Classroom",
    "313B Classroom",
    "314A Food Science Lab",
    "314B Biology Lab",
    "Elevator"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationSelectorScreen(
    result: String,
    onBackClick: () -> Unit,
    onResultClick: (result: String, destination: String) -> Unit
) {
    val viewModel = remember { QRScannerViewModel() }
    val snackBarScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    viewModel.onQRCodeScanned(result)
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                snackBarHostState,
                modifier = Modifier.padding(bottom = 96.dp)
            )
        },
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
                .background(color = Color(228, 228, 228))
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp),
                text = "Current Location: $result",
                style = TextStyle(fontFamily = FontFamily(listOf(Font(R.font.roboto_regular))))
            )
            IndoorLocationSearch(viewModel)
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                Button(
                    onClick = {
                        if (viewModel.destination.value == null) {
                            snackBarScope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Please select a destination",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {
                            onResultClick(
                                viewModel.scannedResult.value.toString(),
                                viewModel.destination.value.toString()
                            )
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Let's Get you to your Location",
                        fontFamily = FontFamily(listOf(Font(R.font.poppins_regular)))
                    )
                }
            }

            Text(
                text = viewModel.calculatedPath.collectAsState().value.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun IndoorLocationSearch(
    viewModel: QRScannerViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    var suggestions by remember { mutableStateOf(emptyList<String>()) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
                suggestions = if (newQuery.isBlank()) {
                    emptyList()
                } else {
                    indoorLocations.filter { location ->
                        location.contains(newQuery, ignoreCase = true)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isSearchFocused = it.isFocused },
            placeholder = {
                Text(
                    "Search Destination",
                    fontFamily = FontFamily(listOf(Font(R.font.poppins_regular)))
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchQuery = ""
                            suggestions = emptyList()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors()
        )

        AnimatedVisibility(visible = isSearchFocused && suggestions.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shadowElevation = 4.dp
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(suggestions) { suggestion ->
                        SuggestionItem(
                            suggestion = suggestion,
                            searchQuery = searchQuery,
                            onClick = {
                                searchQuery = suggestion
                                viewModel.onLocationSearchResult(suggestion)
                                focusManager.clearFocus()
                                suggestions = emptyList()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    suggestion: String,
    searchQuery: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        HighlightedText(
            text = suggestion,
            highlightText = searchQuery
        )
    }
}

@Composable
private fun HighlightedText(
    text: String,
    highlightText: String
) {
    val startIndex = text.indexOf(highlightText, ignoreCase = true)
    if (startIndex == -1) {
        Text(text = text)
        return
    }

    Row {
        Text(text = text.substring(0, startIndex))
        Text(
            text = text.substring(startIndex, startIndex + highlightText.length),
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = text.substring(startIndex + highlightText.length))
    }
}