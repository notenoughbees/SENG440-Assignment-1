package nz.ac.uclive.dsi61.assignment1.screens

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.util.JsonReader
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nz.ac.uclive.dsi61.assignment1.Constants
import nz.ac.uclive.dsi61.assignment1.MusicEntry
import nz.ac.uclive.dsi61.assignment1.R
import nz.ac.uclive.dsi61.assignment1.navigation.Screens
import java.io.InputStreamReader
import java.net.URLEncoder


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ViewMusicEntryScreen(context: Context,
                         navController: NavController,
                         musicEntryId: Int
                         ) {
    println("ViewMusicEntryScreen")
    val configuration = LocalConfiguration.current
    val IS_LANDSCAPE = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val fileName = context.resources.getString(R.string.file)
    val file = context.openFileInput(fileName)
    val reader = JsonReader(InputStreamReader(file))
    val musicEntry = MusicEntry.readAtIndex(reader, musicEntryId)

    var buttonVisible by remember { mutableStateOf(true) }
    val ANIMATION_DURATION = 2000L
    // make the button reappear after a delay (otherwise, it will stay invisible after clicking it the first time!)
    LaunchedEffect(buttonVisible) {
        if (!buttonVisible) {
            // via exit animation, button becomes invisible
            delay(ANIMATION_DURATION) // wait after the exit animation
            buttonVisible = true // make button appear again: trigger the enter animation
        }
    }

//    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
//    val toner = ToneGenerator(AudioManager.STREAM_ALARM, currentVolume)
    val toner = ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME)


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(musicEntry.musicName) },
                // https://foso.github.io/Jetpack-Compose-Playground/material/topappbar/
                navigationIcon = {
                    IconButton(onClick = {
                        toner.startTone(ToneGenerator.TONE_PROP_BEEP, 500)
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
                .padding(top = Constants.TOP_APP_BAR_HEIGHT) // push below appbar
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
        ) {

            // ROW 1: artist name, search button
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

                // https://developer.android.com/jetpack/compose/animation/composables-modifiers#built-in_animated_composables
                val density = LocalDensity.current
                AnimatedVisibility(
                    visible = buttonVisible,
                    enter = slideInVertically {
                        // Slide in from 40 dp from the top.
                        with(density) { -40.dp.roundToPx() }
                    } + expandVertically(
                        // Expand from the top.
                        expandFrom = Alignment.Top
                    ) + fadeIn(
                        // Fade in with the initial alpha of 0.3f.
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()

                    // only shrink the button (requires ExperimentalAnimationApi)
//                AnimatedVisibility(
//                    visible = buttonVisible,
//                    enter = scaleIn() + fadeIn(
//                        // Fade in with the initial alpha of 0.3f.
//                        initialAlpha = 0.3f
//                    ),
//                    exit = scaleOut() + fadeOut()

                ) {
                    PrimaryButton(
                        icon = Icons.Filled.Search,
                        musicEntry,
                        context,
                        buttonVisible,
                        true,
                        toner,
                        "Search"
                    ) {
                        newVisibility -> buttonVisible = newVisibility
                    }
                }
            }

            // ROW 2: all other elements [incl extra notes if portrait]
            Row(
            ) {
                // COLUMN 1: formats, date obtained, where obtained, price paid
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                ) {
                    // physical format
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.musicEntryFormatPhysical) + ": " +
                                    (musicEntry.physicalFormat
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
                                    (musicEntry.recordingFormat
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

                    // where obtained
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.musicEntryWhereObtained) + ": " +
                                        (musicEntry.whereObtained
                                            ?: stringResource(R.string.musicEntryValueNotGiven)),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Spacer( // push the button to the right side of the screen
                            modifier = Modifier.weight(1f)
                        )

                        PrimaryButton(
                            icon = Icons.Filled.LocationOn,
                            musicEntry,
                            context,
                            buttonVisible, //TODO: buttonVisible: when click this button, the other button animates
                            musicEntry.whereObtained != null, // disable button if no location given
                            toner,
                            "Map"
                        ) {
                            newVisibility -> buttonVisible = newVisibility
                        }
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
                    if(!IS_LANDSCAPE) { // if portrait, all fields display in one column
                        ExtraNotesText(musicEntry)
                    }
                }

                // COLUMN 2: extra notes [if landscape]
                if(IS_LANDSCAPE) { // if landscape, the notes field displays to the right, in a separate column
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                    ) {
                        ExtraNotesText(musicEntry)
                    }
                }
            }
        }
    }
}

@Composable
fun PrimaryButton(icon: ImageVector, musicEntry: MusicEntry, context: Context,
                        visible: Boolean, enabled: Boolean, toner: ToneGenerator, action: String,
                        onButtonClicked: (Boolean) -> Unit) {
    FilledIconButton( // https://semicolonspace.com/jetpack-compose-material3-icon-buttons/#filled
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .aspectRatio(1f), // 1:1 aspect ratio: square button
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            toner.startTone(ToneGenerator.TONE_PROP_BEEP, 500)
            onButtonClicked(!visible)
            dispatchAction(musicEntry, context, action)
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

private fun dispatchAction(musicEntry: MusicEntry, context: Context, action: String) {
    when (action) {
        "Search" -> {
            // https://developer.android.com/reference/android/content/Intent#ACTION_WEB_SEARCH
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, musicEntry.artistName + " " + musicEntry.musicName)
            context.startActivity(intent)
        }
        "Map" -> {
            val uri = Uri.parse("geo:0,0?q=${URLEncoder.encode(musicEntry.whereObtained, "UTF-8")}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }

}

@Composable
private fun ExtraNotesText(musicEntry: MusicEntry) {
    Row(
        modifier = Modifier
            .padding(10.dp),
    ) {
        Text(
            text = stringResource(R.string.musicEntryExtraNotes) + ": \n" +
                    (musicEntry.notes ?: stringResource(R.string.musicEntryValueNotGiven)),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
