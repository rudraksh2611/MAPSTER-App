package com.example.mapster.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.mapster.QRScannerViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(onResultScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = remember { QRScannerViewModel() }

    LaunchedEffect(viewModel.scannedResult.collectAsState().value) {
        viewModel.scannedResult.value?.let { result ->
            onResultScanned(result)
            viewModel.clearResult()
        }
    }

    val hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasCameraPermission) {
            ScannerAndroidView(lifecycleOwner, viewModel)
        } else {
            ActivityCompat.requestPermissions(
                context as ComponentActivity,
                arrayOf(Manifest.permission.CAMERA),
                0
            )
            ScannerAndroidView(lifecycleOwner, viewModel)
        }
    }
}

@OptIn(ExperimentalGetImage::class)
private fun setupCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onQRCodeScanned: (String) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val cameraExecutor = Executors.newSingleThreadExecutor()

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(cameraExecutor) { imageProxy ->
                    val image = imageProxy.image
                    if (image != null) {
                        val inputImage = InputImage.fromMediaImage(
                            image,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        val scanner = BarcodeScanning.getClient()
                        scanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                barcodes.firstOrNull()?.rawValue?.let { result ->
                                    Log.d("QRScan", "QR Code: $result")
                                    onQRCodeScanned(result)
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                    }
                }
            }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalyzer
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }, ContextCompat.getMainExecutor(context))
}

@Composable
fun ScannerAndroidView(lifecycleOwner: LifecycleOwner, viewModel: QRScannerViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }.also { previewView ->
                    setupCamera(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        previewView = previewView,
                        onQRCodeScanned = viewModel::onQRCodeScanned
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
