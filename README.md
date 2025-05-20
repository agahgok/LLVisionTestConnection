# LLVision Test Connection - Android SDK Entegrasyon Projesi

Bu proje, harici SDK dosyalarının manuel olarak eklendiği ve Android Studio kullanılarak geliştirilen bir test uygulamasıdır. Uygulamanın amacı, SDK ile bağlantı kurmak ve temel işlevleri test etmektir.

---

## ✨ Temel Bilgiler

* **Paket Adı:** `com.vartech.llvisiontestconnection`
* **Minimum SDK Seviyesi:** 24
* **Hedef SDK Seviyesi:** 35
* **Kotlin JVM Hedefi:** 11
* **Jetpack Compose:** Kullanılıyor
* Android Studio (Arctic Fox ve üzeri)


```bash
git clone https://github.com/kullaniciadi/llvision-test-connection.git
cd llvision-test-connection
```

### Gerekli SDK Dosyalarını Ekleyin

Projenin `libs` klasörü içerisine ilgili `.aar` ve `.jar` SDK dosyalarını kopyalayın.

(Proje dosyaları içerisinde hali hazırda eklenmiştir.)

### Bağlantıyı Tanımlayan Kod Parçası

Aşağıdaki kod, bu SDK dosyalarını projenize dahil eder:

```kotlin
implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar", "*.jar"))))
```

Ayrıca JNI dosyaları ve asset klasörleri de şu şekilde tanımlanmıştır:

```kotlin
sourceSets {
    named("main") {
        assets.srcDirs("src/main/assets")
        jniLibs.srcDirs("libs")
    }
}

## build.gradle 

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.vartech.llvisiontestconnection"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vartech.llvisiontestconnection"
        minSdk = 24
        targetSdk = 35
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


## AndroidManifest.xml İzinleri

Uygulama, aşağıdaki izinleri istemektedir:

```xml
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
```

Android 11 ve üzeri için `MANAGE_EXTERNAL_STORAGE` iznini almak için kullanıcıdan ayrıca manuel izin alınması gerekir. Örnek kodlar README sonunda verilebilir.

