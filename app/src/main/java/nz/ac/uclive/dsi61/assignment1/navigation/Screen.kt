package nz.ac.uclive.dsi61.assignment1.navigation

sealed class Screens(val route: String) {
    object MyCollection : Screens("my_collection")
    object ViewMusicEntry: Screens("view_entry/{id}") {
        fun passId(id: Int): String {
            return "view_entry/$id"
        }
    }
}