package nz.ac.uclive.dsi61.assignment1.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.JsonReader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.TextStyle
import nz.ac.uclive.dsi61.assignment1.navigation.Screens


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                .background(Color.White),
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
//                                color = MaterialTheme.colors.primary
//                            )
//                        },
                        placeholder = {
                            Text(
                                text = value,
                                color = MaterialTheme.colors.primary,
                                fontSize = MaterialTheme.typography.h5.fontSize,
                                fontWeight = MaterialTheme.typography.h5.fontWeight,
                            )
                        },
                          textStyle = TextStyle(
                              color = MaterialTheme.colors.primary,
                              fontSize = MaterialTheme.typography.h5.fontSize,
                              fontWeight = MaterialTheme.typography.h5.fontWeight,
//                        modifier = Modifier.padding(20.dp)
                        )
                    )
                }
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Button(
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

