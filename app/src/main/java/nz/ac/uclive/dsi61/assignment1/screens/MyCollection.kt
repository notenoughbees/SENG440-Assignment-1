package nz.ac.uclive.dsi61.assignment1.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.dsi61.assignment1.MusicEntry
import nz.ac.uclive.dsi61.assignment1.navigation.Screens
import java.time.LocalDate


private val musicEntries = arrayOf<MusicEntry>(
    MusicEntry("Folie à Deux", "Fall Out Boy", "CD", "Album",  LocalDate.of(2022, 9, 30), 5f, "Jewel case"),
    MusicEntry("Hesitant Alien", "Gerard Way", "CD", "Album",  LocalDate.of(2022, 11, 16), 15f, "Comes with poster"),
    MusicEntry("Midnights", "Taylor Swift", "CD", "Album",  LocalDate.of(2022, 11, 16), 19.99f, "Bought new :)"),
    MusicEntry("Cotton Eye Joe", "Rednex", "CD", "Single",  LocalDate.of(2022, 11, 16), 0.25f, ""),
    MusicEntry("Fantasies & Delusions", "Billy Joel", "CD", "Album",  LocalDate.of(2023, 8, 10), 1.95f, ""),
    MusicEntry("California Flash", "Billy Joel", "Vinyl", "Compilation",  null, null, ""),
    MusicEntry("Flaunt It", "Sigue Sigue Sputnik", "Vinyl", "Album",  null, null, ""),
    MusicEntry("Greatest Hits Vol. 2", "ABBA", "Vinyl", "Album",  null, null,  ""),
    MusicEntry("Creep", "Radiohead", "Vinyl", "Single",  null, null,  "Jukebox. No sleeve"),
    MusicEntry("The Exponents", "The Exponents", "Tape", "Album",  null, null,  ""),
    MusicEntry("Human Racing", "Nik Kershaw", "Tape", "Album",  null, null,  ""),
    MusicEntry("Careless Whisper", "George Michael", "Vinyl", "Single",  null, null,  "Australian Promo version"),
)


@Composable
fun MyCollectionScreen(navController: NavController) {
//    var selectedMusicEntry by remember { mutableStateOf(musicEntries[0]) }

    MusicList(musicEntries, onMusicEntryClick = { music ->
//        Toast.makeText(this, music.musicName, Toast.LENGTH_LONG).show()
//        selectedMusicEntry = music
        navController.navigate(Screens.ViewMusicEntry.passId(musicEntries.indexOf(music)))
    })

}


@Composable
fun MusicList(musics: Array<MusicEntry>, onMusicEntryClick: (MusicEntry) -> Unit) {
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