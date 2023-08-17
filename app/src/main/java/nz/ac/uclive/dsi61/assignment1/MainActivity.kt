package nz.ac.uclive.dsi61.assignment1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.dsi61.assignment1.navigation.NavGraph
import nz.ac.uclive.dsi61.assignment1.ui.theme.Assignment1Theme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Assignment1Theme {
                Scaffold(
                    topBar = {
                        TopAppBar (
                            title = {
                                Text("My Collection")
                            }
                        )
                    }

//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }

    }
}



