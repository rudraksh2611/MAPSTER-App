package com.example.mapster.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapster.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val EMAILJS_SERVICE_ID = "service_y32teki"
private const val EMAILJS_TEMPLATE_ID = "template_exos08q"
private const val EMAILJS_PUBLIC_KEY = "7xs9lQrnvPM13_8wE"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
    onReportSent: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null
) {
    val user = auth.currentUser
    val userName = user?.displayName ?: "Anonymous"
    val userEmail = user?.email ?: "unknown@example.com"

    var issueText by rememberSaveable { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    var sendError by remember { mutableStateOf<String?>(null) }
    var sendSuccess by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Report an Issue",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(
                            listOf(Font(R.font.poppins_semi_bold))
                        )
                    ),
                    textAlign = TextAlign.Center
                ) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = "Back")
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Please describe your issue below. Your name and email will be recorded when you send this report.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = issueText,
                onValueChange = { issueText = it },
                label = { Text("Describe your issue") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp)
                    .padding(horizontal = 16.dp),
                maxLines = 10
            )

            Spacer(Modifier.height(32.dp))

            if (sendError != null) {
                Text(
                    sendError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            if (sendSuccess) {
                Text(
                    "Report sent successfully!",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (issueText.isBlank()) {
                        Toast.makeText(context, "Please enter an issue.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    scope.launch {
                        sending = true
                        sendError = null
                        sendSuccess = false
                        val emailBody = "A new report has been submitted by $userEmail \n\n$issueText"
                        val result = sendEmailJS(userName, userEmail, emailBody)
                        sending = false

                        if (result.isSuccess) {
                            sendSuccess = true
                            Toast.makeText(context, "Report sent successfully!", Toast.LENGTH_SHORT)
                                .show()
                            issueText = ""
                            onReportSent?.invoke()
                        } else {
                            val errorMsg = result.errorMessage ?: "Failed to send report. Please try again."
                            sendError = errorMsg
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                enabled = !sending,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                if (sending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Sending…")
                } else {
                    Text("Send Report")
                }
            }
        }
    }
}

data class EmailResult(
    val isSuccess: Boolean,
    val errorMessage: String? = null,
    val responseCode: Int? = null
)

private suspend fun sendEmailJS(userName: String, userEmail: String, issue: String): EmailResult {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            val json = JSONObject().apply {
                put("service_id", EMAILJS_SERVICE_ID)
                put("template_id", EMAILJS_TEMPLATE_ID)
                put("user_id", EMAILJS_PUBLIC_KEY)
                put("template_params", JSONObject().apply {
                    put("from_name", userName)
                    put("from_email", userEmail)
                    put("to_email", "mapster.officials@gmail.com")
                    put("message", issue)
                    put("reply_to", userEmail)
                })
            }

            Log.d("EmailJS", "Sending request with payload: $json")

            val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("https://api.emailjs.com/api/v1.0/email/send")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 13; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Accept-Language", "en-US,en;q=0.9")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Origin", "https://www.emailjs.com")
                .addHeader("Referer", "https://www.emailjs.com/")
                .addHeader("Sec-Ch-Ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
                .addHeader("Sec-Ch-Ua-Mobile", "?1")
                .addHeader("Sec-Ch-Ua-Platform", "\"Android\"")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body.string()

            Log.d("EmailJS", "Response code: ${response.code}")
            Log.d("EmailJS", "Response body: $responseBody")

            if (response.isSuccessful) {
                EmailResult(isSuccess = true)
            } else {
                val errorMsg = when (response.code) {
                    400 -> "Bad request - Please check your EmailJS configuration"
                    401 -> "Unauthorized - Invalid EmailJS credentials"
                    403 -> "Forbidden - EmailJS service may be disabled"
                    404 -> "Not found - Invalid EmailJS service or template ID"
                    422 -> "Validation error - Check template parameters"
                    429 -> "Rate limit exceeded - Too many requests"
                    else -> "Server error (${response.code}): $responseBody"
                }
                EmailResult(isSuccess = false, errorMessage = errorMsg, responseCode = response.code)
            }
        } catch (e: Exception) {
            Log.e("EmailJS", "Exception occurred", e)
            val errorMsg = when (e) {
                is java.net.SocketTimeoutException -> "Request timed out - Check your internet connection"
                is java.net.UnknownHostException -> "No internet connection"
                is javax.net.ssl.SSLException -> "SSL/TLS error - Check your network security settings"
                else -> "Network error: ${e.message}"
            }
            EmailResult(isSuccess = false, errorMessage = errorMsg)
        }
    }
}