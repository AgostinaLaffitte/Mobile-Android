import java.util.Properties
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.parcelizeKotlinAndroid)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}
val versionMajor= 1
val versionMinior= 2
val versionPatch = 1
android {
    namespace = "ar.edu.unicen.seminario"
    compileSdk = 36
    // --- CÓDIGO PARA CARGAR EL ARCHIVO local.properties ---
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }
    defaultConfig {
        applicationId = "ar.edu.unicen.seminario"
        minSdk = 24
        targetSdk = 36
        versionCode = versionMajor * 100 + versionMinior * 10 + versionPatch
        versionName = "$versionMajor.$versionMinior.$versionPatch"
        val rawgApiKey = localProperties.getProperty("RAWG_API_KEY") ?: ""
        buildConfigField("String", "RAWG_API_KEY", "\"$rawgApiKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true;
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig=signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding=true
        buildConfig=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activityKtx)
    implementation(libs.hilt)
    implementation(libs.play.services.maps)
    kapt(libs.hilt.compiler)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.glide)


    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)



}