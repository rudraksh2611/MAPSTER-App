plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.mapster"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mapster"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.concurrent.futures.ktx)
    implementation(libs.guava)

    //cameraX
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)

    //Firebase Auth
    implementation (libs.firebase.auth.v2300)
    implementation (platform(libs.firebase.bom))
    implementation (libs.google.services)


    //MlKit Barcode Scanner
    implementation (libs.barcode.scanning)
    implementation(libs.androidx.navigation.compose)

    //Splash screen API
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.animation.core.lint)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.functions.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Coil
    implementation (libs.coil.compose)

    //Coroutines
    implementation(libs.kotlinx.coroutines.play.services)

    //OkHTTPs
    implementation(libs.okhttp  )
}