package nz.ac.uclive.dsi61.assignment1.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.dsi61.assignment1.MusicEntry
import nz.ac.uclive.dsi61.assignment1.navigation.Screens
import nz.ac.uclive.dsi61.assignment1.Constants.TOP_APP_BAR_HEIGHT


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyCollectionScreen(navController: NavController) {
    val musicEntries: MutableList<MusicEntry> = MusicEntry.readArrayFromFile(LocalContext.current)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Collection")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            MusicList(musicEntries) { music ->
                navController.navigate(Screens.ViewMusicEntry.passId(music.id))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicList(musics: MutableList<MusicEntry>, onMusicEntryClick: (MusicEntry) -> Unit) {
    musics.sort() // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/sort.html
    LazyColumn (
        // in material 3, the top bar overlaps the content by default,
        // so we must add top padding to the content to push it down

        // make the music list start below the app bar so it doesn't get covered
        modifier = Modifier.padding(top = TOP_APP_BAR_HEIGHT)
    ) {

        items(musics) { music ->
            // Styling for an individual item in the list
            Text(
                modifier = Modifier
                    .padding(all = 40.dp)
                    .clickable {
                        onMusicEntryClick(music)
                    },
                style = MaterialTheme.typography.bodyLarge,
                text = music.musicName + "\n" + music.artistName,
            )
        }
    }
}