//plugins {
//    alias(libs.plugins.android.library)
//    alias(libs.plugins.kotlin.android)
//}
//
//android {
//    namespace = "com.bharat1320.fluidwaterdropnav"
//    compileSdk = 35
//
//    defaultConfig {
//        minSdk = 21
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
//}
//
//dependencies {
//
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//}

// fluidwaterdropnav/build.gradle.kts

plugins {
    // Use aliases from libs.versions.toml
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    // Namespace should match your library's package name
    namespace = "com.bharat1320.fluidwaterdropnav"
    compileSdk = 35 // Or latest stable SDK

    defaultConfig {
        // Set your minimum SDK for the library here.
        // If you keep RenderEffect, use 31. If you remove it, use 21 or 24.
        minSdk = 21 // Example: API 21 for broad compatibility

        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Uncomment if you add instrumented tests
        // consumerProGuardFiles("consumer-rules.pro") // Uncomment if you add ProGuard rules for library
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // Ensure this version matches your Jetpack Compose BOM version or is compatible
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Standard for Compose libraries
        }
    }
}

dependencies {
    // Import the Compose BOM to manage Compose dependency versions
    implementation(platform(libs.androidx.compose.bom))

    // Core Compose UI dependencies
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)

    // Preview tooling (for library preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.test.manifest) // Required for previews to work in some cases

    // AndroidX Activity and Lifecycle for Composable context (e.g., rememberCoroutineScope)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Testing dependencies (uncomment if you add tests to your library module)
    // testImplementation(libs.junit)
    // androidTestImplementation(libs.androidx.junit)
    // androidTestImplementation(libs.androidx.espresso.core)
    // androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}