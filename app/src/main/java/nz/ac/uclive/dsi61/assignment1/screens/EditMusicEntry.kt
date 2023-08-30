package nz.ac.uclive.dsi61.assignment1.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.media.ToneGenerator.MAX_VOLUME
import android.util.JsonReader
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.assignment1.MusicEntry
import nz.ac.uclive.dsi61.assignment1.R
import java.io.InputStreamReader
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import nz.ac.uclive.dsi61.assignment1.Constants
import nz.ac.uclive.dsi61.assignment1.navigation.Screens
import java.time.LocalDate
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun EditMusicEntryScreen(context: Context,
                         navController: NavController,
                         musicEntryId: Int
) {
    println("EditMusicEntryScreen")
    val fileName = context.resources.getString(R.string.file)
    val file = context.openFileInput(fileName)
    val reader = JsonReader(InputStreamReader(file))
    val musicEntry = MusicEntry.readAtIndex(reader, musicEntryId)

//    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
//    val toner = ToneGenerator(AudioManager.STREAM_ALARM, currentVolume)
    val toner = ToneGenerator(AudioManager.STREAM_ALARM, MAX_VOLUME)

    // fields to update when clicking the save button
    val dropdownPhysicalFormatItems = arrayOf(stringResource(R.string.formatPhysicalCD),
        stringResource(R.string.formatPhysicalCassette),
        stringResource(R.string.formatPhysicalDigital),
        stringResource(R.string.formatPhysicalVinyl),
        stringResource(R.string.other))
    val dropdownRecordingFormatItems = arrayOf(stringResource(R.string.formatRecordingAlbum),
        stringResource(R.string.formatRecordingLP),
        stringResource(R.string.formatRecordingEP),
        stringResource(R.string.formatRecordingMaxiSingle),
        stringResource(R.string.formatRecordingSingle),
        stringResource(R.string.formatRecordingCompilation),
        stringResource(R.string.other)) //33, 45, 78RPM, Promo, Limited/Deluxe edition...
    var selectedMusicName by remember { mutableStateOf(musicEntry.musicName) }
    var selectedArtistName by remember { mutableStateOf(musicEntry.artistName) }
    var selectedPhysicalFormat by remember { mutableStateOf(musicEntry.physicalFormat?: dropdownPhysicalFormatItems[dropdownPhysicalFormatItems.size -1]) } // last item is "Other"
    var selectedRecordingFormat by remember { mutableStateOf(musicEntry.recordingFormat?: dropdownRecordingFormatItems[dropdownRecordingFormatItems.size -1]) }
    var selectedDateObtained by remember { mutableStateOf("") }
    var selectedPricePaid by remember { mutableStateOf(musicEntry.pricePaid) }
    var selectedNotes by remember { mutableStateOf(musicEntry.notes) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(musicEntry.musicName) },
                navigationIcon = {
                    IconButton(onClick = {
                        toner.startTone(ToneGenerator.TONE_PROP_BEEP, 500)
                        navController.navigate(Screens.ViewMusicEntry.passId(musicEntryId))
                    }) {
                        Icon(Icons.Filled.List, null)
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

            // music name; save button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                TextField(
                    value = selectedMusicName,
                    onValueChange = {
                        selectedMusicName = it
                    },
                    label = { Text(text = stringResource(R.string.musicEntryMusicName)) },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
//                        modifier = Modifier.padding(20.dp)
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    )
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )

                // prepare date for saving
                val calendar = Calendar.getInstance()
                var year = calendar[Calendar.YEAR]
                var month = calendar[Calendar.MONTH]
                var day = calendar[Calendar.DAY_OF_MONTH]
                if (selectedDateObtained != "") {
                    val selectedDateObtainedParts: List<String> = selectedDateObtained.split("/")
                    day = selectedDateObtainedParts[0].toInt()
                    month = selectedDateObtainedParts[1].toInt()
                    year = selectedDateObtainedParts[2].toInt()
                }
                SaveMusicEntryButton(
                    navController, Icons.Filled.Done,
                    musicEntry, musicEntryId,
                    context, selectedMusicName,
                    selectedArtistName, selectedPhysicalFormat,
                    selectedRecordingFormat, LocalDate.of(year, month, day),
                    selectedPricePaid?: stringResource(R.string.musicEntryValueNotGiven),
                    selectedNotes ?: context.resources.getString(R.string.musicEntryValueNotGiven),
                    toner
                )
            }

            // artist name
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    TextField(
                        value = selectedArtistName,
                        onValueChange = {
                            selectedArtistName = it
                        },
                        label = { Text(text = stringResource(R.string.musicEntryArtistName)) },
                          textStyle = TextStyle(
                              color = MaterialTheme.colorScheme.secondary,
                              fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                              fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
//                        modifier = Modifier.padding(20.dp)
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }
            }

            // physical format
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(10.dp),
            ) {
                MyDropdown(
                    stringResource(R.string.musicEntryFormatPhysical),
                    dropdownPhysicalFormatItems,
                    mutableStateOf(selectedPhysicalFormat)) {
                        newSelection -> selectedPhysicalFormat = newSelection
                }
            }

            // recording format
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(10.dp),
            ) {
                MyDropdown(stringResource(R.string.musicEntryFormatRecording),
                    dropdownRecordingFormatItems,
                    mutableStateOf(selectedRecordingFormat)) {
                    newSelection -> selectedRecordingFormat = newSelection
                }
            }

            // date obtained
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                // https://medium.com/@daniel.atitienei/date-and-time-pickers-in-jetpack-compose-f641b1d72dd5
                val context = LocalContext.current
                val calendar = Calendar.getInstance()

                // prepare values for the date picker
                val year: Int
                val month: Int
                val day: Int
                if(musicEntry.dateObtained == null) {
                    // fetch current date
                    year = calendar[Calendar.YEAR]
                    month = calendar[Calendar.MONTH]
                    day = calendar[Calendar.DAY_OF_MONTH]
                } else {
                    // fetch chosen date
                    year = musicEntry.dateObtained!!.year
                    month = musicEntry.dateObtained!!.monthValue - 1 // months start at 0
                    day = musicEntry.dateObtained!!.dayOfMonth
                }

                // open the date picker
                val datePicker = DatePickerDialog(
                    context,
                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                        //TODO: fix date format switching between D/M/Y & Y-M-D
                        selectedDateObtained = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    }, year, month, day
                )

                Button(
                    onClick = {
                        datePicker.show()
                    },
//                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        // https://developer.android.com/reference/kotlin/androidx/compose/material3/ButtonColors
                        containerColor = MaterialTheme.colorScheme.onSurface,
                    )
                ) {
                    Column(
                    ) {
                        Text(
                            text = stringResource(R.string.musicEntryDateObtained),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            fontWeight = MaterialTheme.typography.labelSmall.fontWeight
                        )
                        Text(
                            // if a selection has been made in the date picker:
                            text = if (selectedDateObtained.isNotEmpty()) {
                                selectedDateObtained
                            } else {
                                // a selection has NOT been made in the date picker yet...
                                // the previously-saved date for the entry exists:
                                if (musicEntry.dateObtained != null) {
                                    musicEntry.dateObtained.toString()
                                // the previously-saved date does NOT exist: the date hasn't been set before:
                                } else {
                                    stringResource(R.string.musicEntryValueNotGiven)
                                }
                            },
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                        )
                    }
                }
                //TODO: add "Remove" button to revert date selection back to null
//                Button(
//                    onClick = {
//                        selectedDateObtained = ""
//                    }
//                ) {
//                    Text (
//                        text = "Reset date",
//                        color = MaterialTheme.colorScheme.secondary,
//                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
//                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
//                    )
//                }
            }

            // price paid
            // TODO: if price paid value is 0, show 0 instead of "-"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    TextField(
                        value = selectedPricePaid?: stringResource(R.string.musicEntryValueNotGiven),
                        onValueChange = {
                            selectedPricePaid = it
                        },
                        label = { Text(text = stringResource(R.string.musicEntryPricePaid)) },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }
            }

            // extra notes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                TextField(
                    value = selectedNotes?: stringResource(R.string.musicEntryValueNotGiven),
                    onValueChange = {
                        selectedNotes = it
                    },
                    label = { Text(text = stringResource(R.string.musicEntryExtraNotes)) },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    ),
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class) // exposedDropdown is experimental
@Composable
// https://alexzh.com/jetpack-compose-dropdownmenu/
fun MyDropdown(label: String, items: Array<String>,
               selectedDropdownText: MutableState<String>,
               onItemSelected: (String) -> Unit) { // https://stackoverflow.com/questions/74248340/changing-the-jetpack-compose-remember-variable-from-within-another-function
    var expanded by remember { mutableStateOf(false) }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                label = { Text(label) },
                value = selectedDropdownText.value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.onSurface, // dropdown background
                    focusedLabelColor = MaterialTheme.colorScheme.primary, // label above text
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary, // dropdown arrow
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedDropdownText.value = item
                            onItemSelected(item) // call the callback function with the selected value
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SaveMusicEntryButton(navController: NavController, icon: ImageVector,
                         musicEntry: MusicEntry, musicEntryId: Int,
                         context: Context, selectedMusicName: String,
                         selectedArtistName: String, selectedPhysicalFormat: String,
                         selectedRecordingFormat: String, selectedDateObtained: LocalDate,
                         selectedPricePaid: String, selectedNotes: String,
                         toner: ToneGenerator) {
    FilledIconButton( // https://semicolonspace.com/jetpack-compose-material3-icon-buttons/#filled
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .aspectRatio(1f), // 1:1 aspect ratio: square button
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            toner.startTone(ToneGenerator.TONE_PROP_BEEP, 500)

            saveMusicEntry(musicEntry, context, selectedMusicName,
                selectedArtistName, selectedPhysicalFormat, selectedRecordingFormat,
                selectedDateObtained, selectedPricePaid, selectedNotes)
            navController.navigate(Screens.ViewMusicEntry.passId(musicEntryId))
            Toast.makeText(context, "Saved music details!", Toast.LENGTH_SHORT).show()
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

fun saveMusicEntry(musicEntry: MusicEntry, context: Context, selectedMusicName: String,
                   selectedArtistName: String, selectedPhysicalFormat: String,
                   selectedRecordingFormat: String, selectedDateObtained: LocalDate,
                   selectedPricePaid: String, selectedNotes: String) {
    musicEntry.musicName = selectedMusicName
    musicEntry.artistName = selectedArtistName
    musicEntry.physicalFormat = selectedPhysicalFormat
    musicEntry.recordingFormat = selectedRecordingFormat
    musicEntry.dateObtained = selectedDateObtained
    musicEntry.pricePaid = selectedPricePaid
    musicEntry.notes = selectedNotes

    musicEntry.update(context, musicEntry)
}
