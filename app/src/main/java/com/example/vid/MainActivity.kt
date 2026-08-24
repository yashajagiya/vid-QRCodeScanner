package com.example.vid

import android.content.ClipData
import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.ImageAnalysis
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vid.core.BarcodeAnalyzer
import com.example.vid.core.State
import com.example.vid.core.isValidUrl
import com.example.vid.ui.theme.VidTheme
import com.example.vid.viewModel.CameraViewModel
import com.example.vid.viewModel.QRScannerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CameraPer(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPer(modifier: Modifier = Modifier) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        CameraMain(modifier)
    } else {
        Column(
            modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textShow = if (cameraPermissionState.status.shouldShowRationale) {
                "We need camera permission to show the camera preview"
            } else {

                "Permission Required"
            }
            Text(text = textShow, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text(text = "Permission Request")
            }
        }
    }
}

@Composable
fun CameraMain(modifier: Modifier = Modifier) {
    QRCodeScannerScreen(modifier = modifier)
}


@Composable
fun CameraPreviewContent(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraViewModel: CameraViewModel = viewModel(),
    analyzer: ImageAnalysis.Analyzer? = null
) {
    val surfaceRequestState by cameraViewModel.surfaceRequest.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner, analyzer) {
        cameraViewModel.bindToCamera(context.applicationContext, lifecycleOwner, analyzer)
    }

    val state = surfaceRequestState
    if (state is State.Success) {
        CameraXViewfinder(
            surfaceRequest = state.data,
            modifier = modifier
        )
    }

}

@Composable
fun QRCodeScannerScreen(
    modifier: Modifier = Modifier,
    qrViewModel: QRScannerViewModel = viewModel(),
    cameraViewModel: CameraViewModel = viewModel()
) {
    val qrCodeState by qrViewModel.qrCodeData.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val analyzer = remember { BarcodeAnalyzer(qrViewModel) }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(3.dp, Color.White.copy(alpha = .5f)),
                ) {
                    CameraPreviewContent(
                        modifier = Modifier.size(300.dp),
                        cameraViewModel = cameraViewModel,
                        analyzer = analyzer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Scan a QR Code",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

        }



        Spacer(modifier = Modifier.height(16.dp))

        val state = qrCodeState
        if (state is State.Success) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val data = state.data

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = data,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            val clipData = ClipData.newPlainText("QR Code", data)
                            scope.launch {
                                clipboardManager.setClipEntry(ClipEntry(clipData))
                            }
                        }) {
                            Text(text = "Copy")
                        }
                        if (data.isValidUrl()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                uriHandler.openUri(data)
                            }) {
                                Text(text = "Open")
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}