package nz.ac.uclive.dsi61.assignment1

import java.time.LocalDate

class MusicEntry(val musicName: String,
                 val artistName: String,
                 val musicFormat: String?,
                 val musicType: String?,
                 val dateObtained: LocalDate?,
                 val pricePaid: Float?,
                 val notes: String?) {
    override fun toString() = musicName

}