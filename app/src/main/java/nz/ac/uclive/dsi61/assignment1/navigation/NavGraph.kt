package nz.ac.uclive.dsi61.assignment1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import nz.ac.uclive.dsi61.assignment1.screens.MyCollectionScreen
import nz.ac.uclive.dsi61.assignment1.screens.SettingsScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


// https://dev.to/jbc7ag/jetpack-compose-navigation-tutorial-9en
@Composable
fun NavGraph (navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.MyCollection.route
    ) {
        composable(route = Screens.MyCollection.route) {
            MyCollectionScreen(navController)
        }
        composable(route = Screens.Settings.route) {
            SettingsScreen(navController)
        }
    }
}










