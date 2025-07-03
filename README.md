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
        implementation("com.bharat1320.fluidwaterdropnav:fluidwaterdropnav:1.0.0") // Replace 1.0.0 with the latest version
    }
    ```
    *(You'll update this with the real coordinates once published)*