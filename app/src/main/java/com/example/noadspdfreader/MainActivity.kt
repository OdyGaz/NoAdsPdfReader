package com.example.noadspdfreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    // Δημιουργούμε τη σύνδεση με το ViewModel (τη Λογική της εφαρμογής)
    private val viewModel: PdfReaderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Χρησιμοποιούμε το default Material Theme για να αποφύγουμε μπερδέματα με ονόματα
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Καλούμε την οθόνη μας και της δίνουμε πρόσβαση στη Λογική (ViewModel)
                    PdfViewerScreen(viewModel = viewModel)
                }
            }
        }
    }
}