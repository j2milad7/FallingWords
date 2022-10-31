package dev.ma7.fallingwords.buildsrc

object Libs {

    object Plugins {

        const val navigationSafeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
        const val detektFormatting =
            "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
    }

    object Jetpack {

        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val viewModelKtx =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleKtx}"
        const val liveDataKtx =
            "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleKtx}"
        const val navigationFragmentKtx =
            "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val navigationUiKtx =
            "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val material = "com.google.android.material:material:${Versions.material}"
        const val annotations = "androidx.annotation:annotation:${Versions.material}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    }

    object Kotlin {

        const val kotlinLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        const val coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object Common {

        const val gson = "com.google.code.gson:gson:${Versions.gson}"
    }

    object Testing {

        const val junit = "junit:junit:${Versions.junit}"
        const val coroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}"
        const val testRunner = "androidx.test:runner:${Versions.testRunner}"
        const val testExtJunit = "androidx.test.ext:junit:${Versions.testExt}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
        const val coreTesting = "androidx.arch.core:core-testing:${Versions.coreTesting}"
    }
}
