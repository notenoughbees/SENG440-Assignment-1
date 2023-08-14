package nz.ac.uclive.dsi61.assignment1.navigation

sealed class Screens(val route: String) {
    object MyCollection : Screens("my_collection")
    object Settings: Screens("settings")
}