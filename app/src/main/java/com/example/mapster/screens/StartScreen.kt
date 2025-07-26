package com.example.mapster.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mapster.R

@Composable
fun StartScreen(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
) {

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.mapster_logo_jpg),
            contentDescription = "Mapster Logo",
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(16.dp)
                ),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { onRegisterClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Register", modifier = Modifier.padding(horizontal = 64.dp))
            }
            TextButton(onClick = { onLoginClick() }) {
                Text("I already have an account")
            }
        }
    }

}