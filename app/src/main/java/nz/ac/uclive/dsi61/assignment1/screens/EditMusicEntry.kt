package nz.ac.uclive.dsi61.assignment1.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.util.JsonReader
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(musicEntry.musicName) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screens.ViewMusicEntry.passId(musicEntryId))
                    }) {
                        Icon(Icons.Filled.List, null)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = Constants.TOP_APP_BAR_HEIGHT), // push below appbar
            verticalArrangement = Arrangement.Top,
        ) {
            // artist name; save button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    var value by remember { mutableStateOf(musicEntry.artistName) }
                    TextField(
                        value = value,
                        onValueChange = {
                            value = it
                        },
                        label = { Text(text = stringResource(R.string.musicEntryArtistName)) },
                          textStyle = TextStyle(
                              color = MaterialTheme.colorScheme.primary,
                              fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                              fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
//                        modifier = Modifier.padding(20.dp)
                        )
                    )
                }
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                MyFilledIconButton(
                    icon = Icons.Filled.Done,
                    musicEntry,
                    context
                )
            }


            // physical format
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(10.dp),
            ) {
//                Text(
//                    text = stringResource(R.string.musicEntryFormat) + ": " +
//                            (musicEntry.musicFormat
//                                ?: stringResource(R.string.musicEntryValueNotGiven)), // elvis expression
//                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
//                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
//                    color = MaterialTheme.colorScheme.secondary
//                )
                val items = arrayOf(stringResource(R.string.formatPhysicalCD),
                                    stringResource(R.string.formatPhysicalCassette),
                                    stringResource(R.string.formatPhysicalDigital),
                                    stringResource(R.string.formatPhysicalVinyl))
                MyDropdown(stringResource(R.string.musicEntryFormatPhysical), items)
            }

            // recording format
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(10.dp),
            ) {
                val items = arrayOf(stringResource(R.string.formatPhysicalCD),
                    stringResource(R.string.formatPhysicalCassette),
                    stringResource(R.string.formatPhysicalDigital),
                    stringResource(R.string.formatPhysicalVinyl)) //33, 45, 78RPM, Promo, Limited/Deluxe edition...
                MyDropdown(stringResource(R.string.musicEntryFormatPhysical), items)
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

                var selectedDateText by remember { mutableStateOf("") }

                // Fetch current date
                //TODO: fetch chosen date
                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH]
                val day = calendar[Calendar.DAY_OF_MONTH]

                val datePicker = DatePickerDialog(
                    context,
                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                        selectedDateText = "$selectedDay/{$selectedMonth + 1}/$selectedYear"
                    }, year, month, day
                )


                Button(
                    onClick = {
                        datePicker.show()
                    },
                    shape = RectangleShape,
                ) {
                    Text(
                        text = musicEntry.dateObtained.toString(),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                    )
                }


            }

            // price paid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    var value by remember { mutableStateOf(musicEntry.pricePaid.toString()) }
                    TextField(
                        value = value,
                        onValueChange = {
                            value = it
                        },
                        label = { Text(text = stringResource(R.string.musicEntryPricePaid)) },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
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
                var value by remember { mutableStateOf(musicEntry.notes) }
                TextField(
                    value?: ":)", //TODO: ":)" doesn't appear
                    onValueChange = {
                        value = it
                    },
                    label = { Text(text = stringResource(R.string.musicEntryExtraNotes)) },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                    ),
                    singleLine = false
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class) // exposedDropdown is experimental
@Composable
fun MyDropdown(label: String, items: Array<String>) { // https://alexzh.com/jetpack-compose-dropdownmenu/
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(items[0]) }

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
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
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
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
