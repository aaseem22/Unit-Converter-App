# Unit Converter App

A modern and intuitive **Android Unit Converter** application built with **Jetpack Compose**. This app allows users to easily convert between various units of measurement, featuring a **clean UI**, **smooth animations**, and the ability to **save the last conversion**.

---

##  Features

-  **Stylish UI** – Built with Jetpack Compose for a beautiful and responsive interface.
-  **Multiple Unit Conversions** – Supports Meters, Feet, Inches, and Centimeters.
-  **Real-time Conversion** – Get instant results as you type.
-  **Swap Units** – Easily swap 'From' and 'To' units with a single tap.
-  **Last Conversion Memory** – Remembers and displays your last conversion.
-  **Copy to Clipboard** – Quickly copy the conversion result.
-  **Smooth Animations** – Delightful animations enhance the experience.

---

## 📸 Screenshots


<!-- ![Main Screen](screenshots/main_screen.png) -->

---

## 🛠️ Technologies Used

- **Kotlin** – Primary language for development
- **Jetpack Compose** – Modern UI toolkit
- **ViewModel** – Manages UI-related data lifecycle-consciously
- **DataStore** – For storing the last conversion persistently
- **Kotlinx Serialization** – For serializing and deserializing objects to/from JSON

---

## Architecture

The app follows **Clean Architecture**, separating concerns into distinct layers:

- **UI Layer (`MainActivity.kt`)**
  - Uses Compose to display UI
  - Observes `ViewModel` state for UI updates

- **ViewModel Layer (`MainViewModel.kt`)**
  - Exposes state for UI
  - Handles conversion logic and interactions with the repository
  - Uses `viewModelScope` for coroutine management

- **Data Layer (`LastConversionRepository.kt`)**
  - Handles saving and loading the last conversion using DataStore

---

