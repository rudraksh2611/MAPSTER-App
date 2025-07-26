package com.example.mapster.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.mapster.R
import com.example.mapster.ui.theme.Purple40
import com.example.mapster.ui.theme.Purple80
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(onLoginClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(red = 115, green = 33, blue = 166))
            .padding(top = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.mapster_logo_jpg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight(0.15f)
                .fillMaxWidth(0.8f)
                .clip(shape = RoundedCornerShape(16.dp))
                .align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(color = Color.White)
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxHeight(0.7f)
        ) {
            Text(
                text = "hello!",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
            )
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .background(color = Color.Black)
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Row {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        Text("Email")
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(32.dp)),
                colors = TextFieldDefaults.colors(
                    focusedPlaceholderColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Purple80
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Row {
                        Icon(imageVector = Icons.Default.Key, contentDescription = null)
                        Text("Password")
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(32.dp)),
                colors = TextFieldDefaults.colors(
                    focusedPlaceholderColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Purple80
                )
            )
            TextButton(
                onClick = {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(context, "Please enter a correct email", Toast.LENGTH_SHORT).show()
                    } else {
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Password reset email sent to $email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error: ${task.exception?.localizedMessage}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                },
            ) {
                Text(
                    text = "Forgot Password?",
                    color = Purple40,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                )
            }
            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
                Button(onClick = {

                    auth.signInWithEmailAndPassword(email, password.trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Logged In", Toast.LENGTH_SHORT).show()
                                onLoginClick()
                            } else {
                                Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Login")
                }
            }
        }
    }
}