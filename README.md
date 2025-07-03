# Fluid Water Drop Navigation Bar

A highly customizable Jetpack Compose bottom navigation component with a unique fluid, water-drop-like animation.

## Features

* Smooth, fluid animation.
* Customizable colors, icons, and animation speeds.
* Built with Jetpack Compose, Kotlin DSL, and Version Catalogs.

---

## Demo

![Fluid Nav Animation Demo](assets/bottom_nav_demo.gif)

---

## Installation

1.  **Add to your root `settings.gradle.kts`:**
    ```kotlin
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
            // If publishing to JitPack, add:
            // maven("[https://jitpack.io](https://jitpack.io)")
        }
    }
    ```

2.  **Add the dependency to your app module's `build.gradle.kts`:**
    ```kotlin
    dependencies {
        implementation("com.bharat1320.fluidwaterdropnav:fluidwaterdropnav:1.0.0")
    }
    ```