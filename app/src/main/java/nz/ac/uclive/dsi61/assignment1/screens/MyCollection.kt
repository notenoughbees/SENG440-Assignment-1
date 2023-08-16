package nz.ac.uclive.dsi61.assignment1.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.dsi61.assignment1.MusicEntry
import nz.ac.uclive.dsi61.assignment1.navigation.Screens


@Composable
fun MyCollectionScreen(navController: NavController) {
//    var selectedMusicEntry by remember { mutableStateOf(musicEntries[0]) }

    val musicEntries: MutableList<MusicEntry> = MusicEntry.readArrayFromFile(LocalContext.current)

    MusicList(musicEntries) { music ->
//        Toast.makeText(this, music.musicName, Toast.LENGTH_LONG).show()
//        selectedMusicEntry = music
        navController.navigate(Screens.ViewMusicEntry.passId(musicEntries.indexOf(music)))
    }

}


@Composable
fun MusicList(musics: MutableList<MusicEntry>, onMusicEntryClick: (MusicEntry) -> Unit) {
    musics.sort() // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/sort.html
    LazyColumn {
        items(musics) { music ->
            // Styling for an individual item in the list
            Text(
                modifier = Modifier
                    .padding(all = 40.dp)
                    .clickable {
                        onMusicEntryClick(music)
                    },
                style = MaterialTheme.typography.body1,
                text = music.musicName + "\n" + music.artistName,
            )
        }
    }
}