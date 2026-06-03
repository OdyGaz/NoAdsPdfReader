package com.example.noadspdfreader

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun PdfViewerScreen(viewModel: PdfReaderViewModel) {
    // Launcher που ανοίγει τον επιλογέα αρχείων του Android για να διαλέξουμε PDF
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            viewModel.loadPdf(it) // Στέλνουμε το αρχείο στο ViewModel για επεξεργασία
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)), // Απαλό γκρι φόντο
        contentAlignment = Alignment.Center
    ) {
        when {
            viewModel.isLoading -> {
                // 1. Κατάσταση Φόρτωσης: Δείχνει ένα κυκλικό animation όσο επεξεργάζεται το PDF
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Φόρτωση και επεξεργασία σελίδων...")
                }
            }
            viewModel.errorMessage != null -> {
                // 2. Κατάσταση Σφάλματος: Δείχνει τι πήγε στραβά και κουμπί για προσπάθεια ξανά
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = viewModel.errorMessage ?: "Άγνωστο σφάλμα",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { filePickerLauncher.launch(arrayOf("application/pdf")) }) {
                        Text(text = "Επιλογή άλλου αρχείου")
                    }
                }
            }
            viewModel.pdfPages.isEmpty() -> {
                // 3. Αρχική Κατάσταση: Κουμπί για να επιλέξει ο χρήστης το PDF του
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No-Ads PDF Reader",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    Button(
                        onClick = { filePickerLauncher.launch(arrayOf("application/pdf")) }
                    ) {
                        Text(text = "Άνοιγμα αρχείου PDF")
                    }
                }
            }
            else -> {
                // 4. Κατάσταση Προβολής: Εμφανίζει τις σελίδες σε μια λίστα που κυλάει (scroll)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.pdfPages) { bitmap ->
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Σελίδα PDF",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
            }
        }
    }
}