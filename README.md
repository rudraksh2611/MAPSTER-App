# 🗺️ MAPSTER - Indoor Navigation System

Mapster is designed to assist users navigating **large indoor spaces**—such as university campuses, hospitals, museums, and office buildings—**where GPS and internet access may be limited or unavailable**.

Using **Dijkstra’s algorithm**, Mapster efficiently calculates the shortest path from a user’s scanned location (via QR code) to their destination, ensuring **intuitive, offline navigation**.

---

## 🚀 Features

- 📸 Scan QR code to get real-time position  
- 🔍 Enter destination to get shortest route  
- 🌐 Works **offline** without internet or GPS  
- 🧭 Optimized for indoor navigation  
- 🎯 Minimalistic and user-friendly interface  
- 🔐 Custom-built **Mapster login system** (no Google/third-party login)

---

## 📸 App Preview

### 🏁 Face Page of Mapster  
![Face Page of Mapster](https://raw.githubusercontent.com/rudraksh2611/MAPSTER-App/main/images/First%20Page%20for%20new%20User.jpg)  
<br>

### 📝 New Registration Page  
![New Registration](https://raw.githubusercontent.com/rudraksh2611/MAPSTER-App/main/images/New%20Registration%20Page.jpg)  
<br>

### 🔐 Already Registered Page  
![Already Registration](https://raw.githubusercontent.com/rudraksh2611/MAPSTER-App/main/images/Already%20Register.jpg)  
<br>

### 📷 Scan or Upload QR Code  
![Scan or Upload QR Code](https://raw.githubusercontent.com/rudraksh2611/MAPSTER-App/main/images/Scan%20QR%20Code.jpg)  
<br>

### 🎯 Enter Destination Page  
![Enter Destination](https://raw.githubusercontent.com/rudraksh2611/MAPSTER-App/main/images/Enter%20Destination.jpg)  
<br>

### 📍 Final Path Result Page  
![Resulting Page](https://raw.githubusercontent.com/rudraksh2611/MAPSTER-App/main/images/Resulting%20page.jpg)  
<br>

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
2. App maps the code to a specific position on the indoor graph.  
3. User selects or enters a **destination**.  
4. App computes shortest path using **Dijkstra’s algorithm**.  
5. Navigation steps are displayed **instantly and offline**.

---

## ⚙️ Setup Instructions

### 🧰 Prerequisites

- Android Studio (Arctic Fox or above)  
- Kotlin SDK (bundled with Android Studio)  
- Python 3.10+ (optional, for backend simulation or debugging)  
- SQLite plugin (optional, for database inspection)  
- QR code generator (optional, for testing locations)

### 📦 Clone the Repository

```bash
git clone https://github.com/rudraksh2611/MAPSTER-App.git
cd MAPSTER-App
