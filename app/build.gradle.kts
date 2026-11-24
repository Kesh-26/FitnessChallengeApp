plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.devarsh.fitnesschallengeapp"

    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.devarsh.fitnesschallengeapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // ---------------------------------------------------------
    // CORE ANDROID
    // ---------------------------------------------------------

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")


    // ---------------------------------------------------------
    // COMPOSE
    // ---------------------------------------------------------

    val composeBom =
        platform("androidx.compose:compose-bom:2024.02.00")

    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.compose.material3:material3")
    implementation(
        "androidx.compose.material:material-icons-extended"
    )


    // ---------------------------------------------------------
    // NAVIGATION
    // ---------------------------------------------------------

    implementation(
        "androidx.navigation:navigation-compose:2.7.7"
    )


    // ---------------------------------------------------------
    // VIEWMODEL / LIFECYCLE
    // ---------------------------------------------------------

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0"
    )

    implementation(
        "androidx.lifecycle:lifecycle-runtime-compose:2.7.0"
    )


    // ---------------------------------------------------------
    // ROOM DATABASE
    // ---------------------------------------------------------

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    ksp(libs.room.compiler)


    // ---------------------------------------------------------
    // WORKMANAGER
    // ---------------------------------------------------------

    implementation(
        "androidx.work:work-runtime-ktx:2.9.0"
    )


    // ---------------------------------------------------------
    // SECURITY
    // ---------------------------------------------------------

    implementation(
        "androidx.security:security-crypto:1.1.0-alpha06"
    )


    // ---------------------------------------------------------
    // LOCAL JVM UNIT TESTING
    //
    // Used by tests inside:
    // app/src/test/java
    // ---------------------------------------------------------

    testImplementation(
        "junit:junit:4.13.2"
    )


    // ---------------------------------------------------------
    // ANDROID INSTRUMENTATION TESTING
    //
    // Used by tests inside:
    // app/src/androidTest/java
    // ---------------------------------------------------------

    androidTestImplementation(
        "androidx.test.ext:junit:1.2.1"
    )

    androidTestImplementation(
        "androidx.test:core-ktx:1.6.1"
    )

    androidTestImplementation(
        "androidx.test:runner:1.6.2"
    )

    androidTestImplementation(
        "androidx.test.espresso:espresso-core:3.6.1"
    )


    // ---------------------------------------------------------
    // COMPOSE UI TESTING
    // ---------------------------------------------------------

    androidTestImplementation(
        "androidx.compose.ui:ui-test-junit4"
    )


    // ---------------------------------------------------------
    // DEBUG TOOLS
    // ---------------------------------------------------------

    debugImplementation(
        "androidx.compose.ui:ui-tooling"
    )

    debugImplementation(
        "androidx.compose.ui:ui-test-manifest"
    )
}