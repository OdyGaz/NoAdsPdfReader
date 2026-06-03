package com.example.noadspdfreader

import android.app.Application
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PdfReaderViewModel(application: Application) : AndroidViewModel(application) {

    // Λίστα που θα περιέχει τις σελίδες του PDF ως εικόνες (Bitmaps)
    val pdfPages = mutableStateListOf<Bitmap>()

    // Κατάσταση φόρτωσης (δείχνει αν αυτή τη στιγμή φορτώνει το αρχείο)
    var isLoading by mutableStateOf(false)
        private set

    // Μήνυμα σφάλματος (αν κάτι πάει στραβά, π.χ. κατεστραμμένο αρχείο)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Η συνάρτηση που αναλαμβάνει να ανοίξει και να διαβάσει το PDF
    fun loadPdf(uri: Uri) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            pdfPages.clear() // Καθαρίζουμε τυχόν προηγούμενο PDF που ήταν ανοιχτό

            try {
                // Εκτελούμε τη βαριά εργασία σε Background Thread (Dispatchers.IO) για να μην κολλάει το κινητό
                withContext(Dispatchers.IO) {
                    val context = getApplication<Application>().applicationContext
                    val contentResolver = context.contentResolver

                    // Ανοίγουμε το αρχείο μέσω του Uri που επέλεξε ο χρήστης
                    val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")

                    if (parcelFileDescriptor != null) {
                        // Χρησιμοποιούμε τον ενσωματωμένο PdfRenderer του Android
                        val renderer = PdfRenderer(parcelFileDescriptor)
                        val pageCount = renderer.pageCount

                        // Διαβάζουμε κάθε σελίδα μία προς μία
                        for (i in 0 until pageCount) {
                            val page = renderer.openPage(i)

                            // Δημιουργούμε μια κενή εικόνα (Bitmap) με τις διαστάσεις της σελίδας
                            val bitmap = Bitmap.createBitmap(
                                page.width,
                                page.height,
                                Bitmap.Config.ARGB_8888
                            )

                            // Σχεδιάζουμε το περιεχόμενο της σελίδας του PDF πάνω στην εικόνα
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                            // Προσθέτουμε την έτοιμη εικόνα στη λίστα μας (επιστρέφοντας στο Main Thread)
                            withContext(Dispatchers.Main) {
                                pdfPages.add(bitmap)
                            }

                            page.close()
                        }
                        renderer.close()
                        parcelFileDescriptor.close()
                    } else {
                        throw Exception("Δεν ήταν δυνατή η ανάγνωση του αρχείου.")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Αποτυχία ανάγνωσης PDF: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}