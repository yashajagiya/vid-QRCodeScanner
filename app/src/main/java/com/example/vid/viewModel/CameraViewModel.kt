package com.example.vid.viewModel

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.vid.core.State
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.Executors

class CameraViewModel : ViewModel() {

    private val logE = "CameraViewModel"

    private val _surfaceRequest = MutableStateFlow<State<SurfaceRequest>?>(null)
    val surfaceRequest = _surfaceRequest

    private fun cameraPreviewUseCase() = Preview.Builder().build().apply {
        setSurfaceProvider { request ->
            _surfaceRequest.update { State.Success(request) }
        }
    }

    suspend fun bindToCamera(
        appContext: Context,
        lifecycleOwner: LifecycleOwner,
        analyzer: ImageAnalysis.Analyzer? = null
    ) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)

        val preview = cameraPreviewUseCase()
        val useCases = mutableListOf<UseCase>(preview)

        analyzer?.let {
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), it)
            useCases.add(imageAnalysis)
        }

        processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            DEFAULT_BACK_CAMERA,
            *useCases.toTypedArray()
        )
        try {
            awaitCancellation()
        } catch (e: Exception) {
            Log.e(logE, e.message.toString())
        } finally {
            processCameraProvider.unbindAll()
        }
    }

}
