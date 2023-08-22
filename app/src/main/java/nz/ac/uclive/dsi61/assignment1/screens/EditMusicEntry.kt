package nz.ac.uclive.dsi61.assignment1.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.JsonReader
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.assignment1.MusicEntry
import nz.ac.uclive.dsi61.assignment1.R
import java.io.InputStreamReader
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import nz.ac.uclive.dsi61.assignment1.Constants
import nz.ac.uclive.dsi61.assignment1.navigation.Screens


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
//                        label = {
//                            Text(
//                                text = "Artist name",
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Normal,
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                        },
                        placeholder = {
                            Text(
                                text = value,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
                            )
                        },
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
                MyButton(
                    label = "💾",
                    musicEntry,
                    context
                )
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
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // music type
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
//                Text(
//                    text = stringResource(R.string.musicEntryType) + ": " +
//                            (musicEntry.musicType
//                                ?: stringResource(R.string.musicEntryValueNotGiven)),
//                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
//                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
//                    color = MaterialTheme.colorScheme.secondary
//                )

                myDropdown()
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


@OptIn(ExperimentalMaterial3Api::class) // exposedDropdown is experimental
@Composable
fun myDropdown() {
    val context = LocalContext.current
    val items = arrayOf("xxx", "yyy", "zzz")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(items[0]) }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
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
                            Toast.makeText(context, item, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }
    }
}
