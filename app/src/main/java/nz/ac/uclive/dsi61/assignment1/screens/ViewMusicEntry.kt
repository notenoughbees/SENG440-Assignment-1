package nz.ac.uclive.dsi61.assignment1.screens

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.JsonReader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.assignment1.MusicEntry
import nz.ac.uclive.dsi61.assignment1.R
import java.io.InputStreamReader
import androidx.compose.ui.res.stringResource


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ViewMusicEntryScreen(context: Context,
                         navController: NavController,
                         musicEntryId: Int
                         ) {
    println("ViewMusicEntryScreen")
    val fileName = context.resources.getString(R.string.file)
    val file = context.openFileInput(fileName)
    val reader = JsonReader(InputStreamReader(file))
    val musicEntry = MusicEntry.readAtIndex(reader, musicEntryId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(musicEntry.musicName)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.Start
        ) {
            // artist name; search button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
//                horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = musicEntry.artistName,
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = MaterialTheme.typography.h5.fontWeight,
                        color = MaterialTheme.colors.primary
                    )
                }
                Column(
                    // must wrap the button in a column so that we can apply horizontalAlignment
                    horizontalAlignment = Alignment.End, // push button to corner of screen
                ) {
                    SoundButton(
                        label = "🔎",
                        musicEntry,
                        context
                    ) {
                        val toner =
                            ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME)
                        val dotTime = 200
                        toner.startTone(ToneGenerator.TONE_SUP_DIAL, dotTime)
                    }
                }

            }

            // music format
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.musicEntryFormat) + ": " +
                            (musicEntry.musicFormat
                                ?: stringResource(R.string.musicEntryValueNotGiven)), // elvis expression
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    fontWeight = MaterialTheme.typography.body2.fontWeight,
                    color = MaterialTheme.colors.secondary
                )
            }
            // music type
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.musicEntryType) + ": " +
                            (musicEntry.musicType
                                ?: stringResource(R.string.musicEntryValueNotGiven)),
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    fontWeight = MaterialTheme.typography.body2.fontWeight,
                    color = MaterialTheme.colors.secondary
                )
            }

            // date got
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.musicEntryDateObtained) + ": " +
                            (musicEntry.dateObtained
                                ?: stringResource(R.string.musicEntryValueNotGiven)),
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    fontWeight = MaterialTheme.typography.body2.fontWeight,
                    color = MaterialTheme.colors.secondary
                )
            }
            // price paid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.musicEntryPricePaid) + ": " +
                            (musicEntry.pricePaid
                                ?: stringResource(R.string.musicEntryValueNotGiven)),
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    fontWeight = MaterialTheme.typography.body2.fontWeight,
                    color = MaterialTheme.colors.secondary
                )
            }

            // extra notes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.musicEntryExtraNotes) + ": " +
                            (musicEntry.notes ?: stringResource(R.string.musicEntryValueNotGiven)),
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    fontWeight = MaterialTheme.typography.body2.fontWeight,
                    color = MaterialTheme.colors.secondary
                )
            }

        }
    }

}

    @Composable
    fun SoundButton(label: String, musicEntry: MusicEntry, context: Context, onClick: () -> Unit) {
        Button(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .aspectRatio(1f), // 1:1 aspect ratio: square button
            onClick = {
            dispatchAction("Browser", musicEntry, context)
        }
        ) {
            Text(
                text = label,
            )
        }
    }


private fun dispatchAction(option: String, musicEntry: MusicEntry, context: Context): Unit {
    when (option) {
        // https://developer.android.com/reference/android/content/Intent#ACTION_WEB_SEARCH
        "Browser" -> {
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, musicEntry.artistName + " " + musicEntry.musicName)
            context.startActivity(intent)
        }
//        "Map" -> {
//            val uri = Uri.parse("geo:0,0?q=${URLEncoder.encode(friend.home, "UTF-8")}")
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
//        }
//        "Email" -> {
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type = "text/plain"
//            intent.putExtra(Intent.EXTRA_EMAIL, friend.email)
//            startActivity(intent)
//        }
//        "Text" -> {
//            val uri = Uri.parse("smsto:${friend.phone}")
//            val intent = Intent(Intent.ACTION_SEND, uri)
//            startActivity(intent)
//        }
//        "Call" -> {
//            val uri = Uri.parse("tel:${friend.phone}")
//            val intent = Intent(Intent.ACTION_DIAL, uri)
//            startActivity(intent)
//        }
//        "Slack" -> {
//            val uri = Uri.parse("slack://user?team=TR8N4694&id=${friend.slackId}")
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
//        }
    }
}
