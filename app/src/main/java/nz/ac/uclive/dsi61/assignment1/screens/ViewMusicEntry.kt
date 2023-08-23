package nz.ac.uclive.dsi61.assignment1.screens

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.util.JsonReader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.assignment1.Constants
import nz.ac.uclive.dsi61.assignment1.MusicEntry
import nz.ac.uclive.dsi61.assignment1.R
import nz.ac.uclive.dsi61.assignment1.navigation.Screens
import java.io.InputStreamReader


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                title = { Text(musicEntry.musicName) },
                // https://foso.github.io/Jetpack-Compose-Playground/material/topappbar/
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screens.EditMusicEntry.passId(musicEntryId))
                    }) {
                        Icon(Icons.Filled.Edit, null)
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = Constants.TOP_APP_BAR_HEIGHT), // push below appbar
            verticalArrangement = Arrangement.Top,
        ) {
            // artist name; search button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = musicEntry.artistName,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer( // push the button to the right side of the screen
                    modifier = Modifier.weight(1f)
                )
                SearchBrowserButton(
                    icon = Icons.Filled.Search,
                    musicEntry,
                    context
                )
            }

            // physical format
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.musicEntryFormatPhysical) + ": " +
                            (musicEntry.musicFormat
                                ?: stringResource(R.string.musicEntryValueNotGiven)), // elvis expression
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // recording format
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.musicEntryFormatRecording) + ": " +
                            (musicEntry.musicType
                                ?: stringResource(R.string.musicEntryValueNotGiven)),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // date obtained
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.musicEntryDateObtained) + ": " +
                            (musicEntry.dateObtained
                                ?: stringResource(R.string.musicEntryValueNotGiven)),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    color = MaterialTheme.colorScheme.secondary
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
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    color = MaterialTheme.colorScheme.secondary
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
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

        }
    }
}

@Composable
fun MyButton(label: String, musicEntry: MusicEntry, context: Context) {
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

@Composable
fun SearchBrowserButton(icon: ImageVector, musicEntry: MusicEntry, context: Context) {
    FilledIconButton( // https://semicolonspace.com/jetpack-compose-material3-icon-buttons/#filled
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .aspectRatio(1f), // 1:1 aspect ratio: square button
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            dispatchAction("Browser", musicEntry, context)
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface,
//            modifier = Modifier.fillMaxSize()
//            modifier = Modifier.size(30.dp)
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
    }
}
