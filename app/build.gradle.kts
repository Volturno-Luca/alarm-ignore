plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.volturno.alarmignore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.volturno.alarmignore"
        minSdk = 23
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"
    }

    // Fixed signing key (committed) so every CI build is signed with the same
    // key — updates then install over each other without an uninstall.
    signingConfigs {
        create("shared") {
            storeFile = rootProject.file("keystore/skipalarms.jks")
            storePassword = "skipalarms"
            keyAlias = "skipalarms"
            keyPassword = "skipalarms"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("shared")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("shared")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}
