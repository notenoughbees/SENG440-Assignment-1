package nz.ac.uclive.dsi61.assignment1.screens

import android.content.Context
import android.util.JsonReader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.assignment1.MusicEntry
import java.io.InputStreamReader


@Composable
fun ViewMusicEntryScreen(context: Context,
                         navController: NavController,
                         musicEntryId: Int
                         ) {

    println("ViewMusicEntryScreen")
    val file = context.openFileInput("music.json")
    val reader = JsonReader(InputStreamReader(file))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "${MusicEntry.readAtIndex(reader, musicEntryId)}",
                fontSize = MaterialTheme.typography.h3.fontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}