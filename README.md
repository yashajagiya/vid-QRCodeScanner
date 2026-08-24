# Vid - QR Code Scanner

Vid is a modern, fast, and lightweight Android QR Code Scanner built with **Jetpack Compose** and **CameraX**. It leverages Google's **ML Kit** for high-performance barcode scanning and provides a seamless user experience for scanning, copying, and opening URLs.

## Features

- 📸 **Real-time Scanning**: Fast and accurate QR code detection using Google ML Kit.
- 🖼️ **CameraX Viewfinder**: Smooth camera preview using the latest CameraX Compose components.
- 🔗 **Smart URL Detection**: Automatically detects if the scanned content is a URL and provides an "Open" button.
- 📋 **Clipboard Integration**: Easily copy scanned text or URLs to your clipboard with a single tap.
- 🛡️ **Permission Handling**: Clean and intuitive camera permission flow using Accompanist Permissions.
- 🎨 **Material 3 UI**: Modern, clean design following Material Design 3 guidelines.
- 🏗️ **MVVM Architecture**: Built using a robust MVVM pattern with Kotlin Coroutines and StateFlow.

## Screenshots

*(Add screenshots of your app here)*

## Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Camera**: [CameraX](https://developer.android.com/training/camerax)
- **Barcode Detection**: [Google ML Kit Barcode Scanning](https://developers.google.com/ml-kit/vision/barcode-scanning)
- **Architecture**: MVVM
- **Language**: Kotlin
- **Dependency Injection**: ViewModel & StateFlow
- **Permission Management**: [Accompanist Permissions](https://google.github.io/accompanist/permissions/)

## Getting Started

### Prerequisites

- Android Studio Ladybug | 2024.2.1 or newer
- Android SDK 24 or higher
- A physical Android device for camera testing

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yashajagiya/vid-QRCodeScanner.git
   ```
2. Open the project in Android Studio.
3. Build and run the app on your device.

## Project Structure

```text
app/src/main/java/com/example/vid/
├── core/             # Barcode analyzer and utility functions
├── ui/theme/         # Compose Material 3 theme configurations
├── viewModel/        # ViewModels for Camera and Scanner logic
└── MainActivity.kt   # Main UI and Permission handling
```

## Contributing

Contributions are welcome! If you have any ideas, suggestions, or bug reports, feel free to open an issue or submit a pull request.

## License

```text
Copyright 2026 Yash Jaghiya

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---
Developed with ❤️ by [Yash Jaghiya](https://github.com/yashajagiya)
