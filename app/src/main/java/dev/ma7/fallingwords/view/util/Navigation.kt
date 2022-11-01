package dev.ma7.fallingwords.view.util

import androidx.navigation.NavDirections

sealed class Navigation {

    object Pop : Navigation()

    class Direction(val directions: NavDirections) : Navigation()
}