package nz.ac.uclive.dsi61.assignment1

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nz.ac.uclive.dsi61.assignment1.ui.theme.Assignment1Theme
import java.time.LocalDate
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Returns whether the app has the given permission.
         */
        fun hasPermissions(permissions: Array<String>): Boolean {
            return permissions.all {
                checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
            }
        }


        setContent {
            Assignment1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MusicList(musicEntries, onMusicEntryClick = { music ->
                        Toast.makeText(this, music.musicName, Toast.LENGTH_LONG).show()
                    })
                }
            }
        }

    }
}


@Composable
fun MusicList(musics: Array<MusicEntry>, onMusicEntryClick: (MusicEntry) -> Unit) {
    musics.sort() // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/sort.html
    LazyColumn {
        items(musics) { music ->
            // Styling for an individual item in the list
            Text(
                modifier = Modifier
                    .padding(all=40.dp)
                    .clickable {
                        onMusicEntryClick(music)
                    },
                style = MaterialTheme.typography.body1,
                text = music.musicName + "\n" + music.artistName,
            )
        }
    }
}



