package nz.ac.uclive.dsi61.assignment1.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import nz.ac.uclive.dsi61.assignment1.screens.MyCollectionScreen
import nz.ac.uclive.dsi61.assignment1.screens.ViewMusicEntryScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import nz.ac.uclive.dsi61.assignment1.screens.EditMusicEntryScreen


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
            route = Screens.ViewMusicEntry.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            // https://developer.android.com/jetpack/compose/navigation#nav-with-args
            val musicEntryId = backStackEntry.arguments?.getInt("id") ?: -1
            ViewMusicEntryScreen(LocalContext.current, navController, musicEntryId)
        }

        composable(
            route = Screens.EditMusicEntry.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val musicEntryId = backStackEntry.arguments?.getInt("id") ?: -1
            EditMusicEntryScreen(LocalContext.current, navController, musicEntryId)
        }
    }
}










