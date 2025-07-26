package com.example.mapster.screens

import android.content.Context
import android.util.Log
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.example.mapster.R
import com.example.mapster.ui.theme.Purple40
import com.example.mapster.ui.theme.Purple80
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

fun createUser(
    username: String,
    email: String,
    password: String,
    context: Context,
    onSignupClick: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(
                    context,
                    "Registration failed: ${task.exception?.localizedMessage ?: "Unknown error"}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("SignUp", "got true")
                return@addOnCompleteListener
            }

            val user = auth.currentUser
            val uid = user?.uid
            if (uid == null) {
                Toast.makeText(context, "Registration failed: missing user ID.", Toast.LENGTH_SHORT)
                    .show()
                return@addOnCompleteListener
            }

            val updates = userProfileChangeRequest {
                displayName = username
            }
            user.updateProfile(updates).addOnFailureListener {
            }

            val userMap = mapOf(
                "name" to username,
                "email" to email,
            )

            db.collection("users").document(uid)
                .set(userMap, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("createUser", "createUser: Successful")
                    Toast.makeText(
                        context,
                        "You're registered successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Failed to save profile: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            onSignupClick()
        }
}


@Composable
fun SignUp(onSignupClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    val isUsernameEmpty = username.isEmpty()
    val isPasswordEmpty = password.isEmpty()

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.mapster_logo_jpg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.15f)
                .clip(shape = RoundedCornerShape(16.dp))
                .align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(color = Color(red = 115, green = 33, blue = 166))
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxHeight(0.7f)
        ) {
            Text(
                text = "hello!",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .background(color = Color.Black)
            )
            TextField(
                value = username,
                onValueChange = { input ->
                    username = input
                },
                label = {
                    Row {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        Text("Full Name")
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
                    cursorColor = Purple40,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            if (isUsernameEmpty) {
                Text(
                    text = "This field cannot be empty",
                    color = Color(red = 255, green = 87, blue = 51),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = email,
                onValueChange = { input ->
                    email = input
                    isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                },
                label = {
                    Row {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null)
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
                    cursorColor = Purple40,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            if (!isEmailValid) {
                Text(
                    text = "Please enter a valid email",
                    color = Color(red = 255, green = 87, blue = 51),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
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
                    cursorColor = Purple40,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            if (isPasswordEmpty) {
                Text(
                    text = "This field cannot be empty",
                    color = Color(red = 255, green = 87, blue = 51),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
                Button(
                    onClick = {
                        if (isEmailValid && !isUsernameEmpty && !isPasswordEmpty) {
                            createUser(username, email, password, context, onSignupClick)
                        }
                    }, modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonColors(
                        containerColor = Color.White,
                        contentColor = Purple80,
                        disabledContentColor = Color.DarkGray,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text(text = "Register", color = Purple40)
                }
            }
        }
    }
}