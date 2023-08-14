package nz.ac.uclive.dsi61.assignment1

import java.time.LocalDate

class MusicEntry(val musicName: String,
                 val artistName: String,
                 val musicFormat: String?,
                 val musicType: String?,
                 val dateObtained: LocalDate?,
                 val pricePaid: Float?,
                 val notes: String?) : Comparable<MusicEntry> {
    override fun toString() = musicName

    // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/sort.html
    override fun compareTo(other: MusicEntry): Int
        = this.musicName.compareTo(other.musicName)

}