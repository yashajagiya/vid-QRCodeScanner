package com.example.vid.viewModel

import com.example.vid.core.State
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QRScannerViewModel : ViewModel() {

    private val _qrCodeData = MutableStateFlow<State<String>?>(null)
    val qrCodeData: StateFlow<State<String>?> = _qrCodeData.asStateFlow()

    fun onQrCodeScanned(data: String) {
        _qrCodeData.value = State.Success(data)
    }
}