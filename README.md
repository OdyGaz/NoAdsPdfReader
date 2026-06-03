# NoAdsPdfReader

A lightweight, 100% ad-free PDF reader for Android, built with modern development practices and native APIs.

## Why this project?
Many PDF readers on the Google Play Store are bloated with intrusive ads, trackers, and unnecessary permissions.
This project was built to solve that problem by creating a secure, minimal, and fully native PDF reader.
It also serves as a practical project to learn Android development architecture, Jetpack Compose, and performance optimization.

## Features
*   🚫 **100% Ad-Free & Tracker-Free**: No internet required, no analytics, no privacy concerns.
*   ⚡ **Ultra-Lightweight**: Leverages Android's built-in native `PdfRenderer` API instead of heavy third-party libraries, keeping the APK size extremely small.
*   🔒 **Highly Secure**: Uses the modern **Storage Access Framework (SAF)**, meaning the app never requests broad read/write storage permissions. It only accesses the specific PDF file you choose.
*   🎨 **Modern UI**: Designed with **Jetpack Compose** and Material 3 for a sleek, responsive, and scrollable reading experience.
*   📈 **Memory Optimized**: Handles rendering off the main thread (using Kotlin Coroutines) to prevent screen stuttering and UI lag.

## Tech Stack & Architecture
*   **Language**: Kotlin
*   **UI Framework**: Jetpack Compose (Declarative UI)
*   **Architecture**: MVVM (Model-View-ViewModel) using `AndroidViewModel` for clean separation of concerns and lifecycle-aware state management.
*   **PDF Engine**: Android Native `PdfRenderer` (available from API 21+)
*   **Threading**: Kotlin Coroutines (for background image rendering and smooth main thread performance)
*   **Minimum SDK**: API 24 (Android 7.0)
*   **Compile SDK**: API 35 (Android 15)

## How to Build & Run
1.  Clone this repository:
    ```bash
    git clone https://github.com/YOUR_USERNAME/NoAdsPdfReader.git
    ```
2.  Open the project in **Android Studio**.
3.  Let the Gradle sync finish.
4.  Run the application on a physical device or emulator running Android 7.0 (API 24) or higher.

## Project Structure
*   `MainActivity.kt`: The entry point that hooks the UI with the ViewModel.
*   `PdfReaderViewModel.kt`: The business logic container. Opens the PDF file descriptor, parses pages, and renders them to Bitmaps in a background thread.
*   `PdfViewerScreen.kt`: The declarative UI built with Jetpack Compose, presenting the file picker and the scrollable card list of rendered pages.

## License
This project is open-source and available under the [MIT License](LICENSE).
