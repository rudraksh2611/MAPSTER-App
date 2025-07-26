
# 🗺️ MAPSTER - Indoor Navigation System

Mapster is designed to assist users navigating **large indoor spaces**—such as university campuses, hospitals, museums, and office buildings—**where GPS and internet access may be limited or unavailable**.

Using **Dijkstra’s algorithm**, Mapster efficiently calculates the shortest path from a user’s scanned location (via QR code) to their destination, ensuring **intuitive, offline navigation**.

---

## 🚀 Features

- Scan QR code to get real-time position  
- Enter destination to get shortest route  
- Works **offline** without internet/GPS  
- Optimized for indoor navigation  
- Minimalistic and user-friendly interface

---

## 🧠 Tech Stack & Concepts Used

| Layer              | Technology                         |
|-------------------|------------------------------------|
| **Frontend**       | Kotlin, Android Studio (XML UI)   |
| **Backend**        | Python (Dijkstra’s algorithm)     |
| **Interfacing**    | QR Code Scanning, SQLite Storage  |
| **Algorithm Used** | DSA (Graph + Dijkstra Algorithm)  |
| **Offline Logic**  | Custom Android Logic, File I/O    |

---

## 📲 How It Works

1. **User scans QR code** at their current location.  
2. App identifies position via QR mapping.  
3. User enters a **destination**.  
4. Backend applies **Dijkstra’s algorithm** to compute the shortest path.  
5. Directions are shown step-by-step.

---

## ⚙️ Setup Instructions

### Prerequisites

- Android Studio (Arctic Fox or above)  
- Python 3.10+ (for algorithm simulation/testing)  
- Kotlin SDK (included in Android Studio)  
- QR code generator (optional for testing)

### Clone the Repo

```bash
git clone https://github.com/rudraksh2611/MAPSTER-App.git
cd MAPSTER-App
