package nz.ac.uclive.dsi61.assignment1.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import nz.ac.uclive.dsi61.assignment1.screens.MyCollectionScreen
import nz.ac.uclive.dsi61.assignment1.screens.SettingsScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


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
        composable(
            route = Screens.Settings.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) {
            Log.d("Args", it.arguments?.getInt("id").toString())
            SettingsScreen(navController)
        }
    }
}










