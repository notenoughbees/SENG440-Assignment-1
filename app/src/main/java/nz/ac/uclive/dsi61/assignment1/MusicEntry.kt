package nz.ac.uclive.dsi61.assignment1

import android.content.Context
import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.LocalDate

private val sampleData = mutableListOf<MusicEntry>(
    MusicEntry(1, "Folie à Deux", "Fall Out Boy", "CD", "Album",  LocalDate.of(2022, 9, 30), 5f, "Jewel case"),
    MusicEntry(2, "Hesitant Alien", "Gerard Way", "CD", "Album",  LocalDate.of(2022, 11, 16), 15f, "Comes with poster"),
    MusicEntry(3, "Midnights", "Taylor Swift", "CD", "Album",  LocalDate.of(2022, 11, 16), 19.99f, "Bought new :)"),
    MusicEntry(4, "Cotton Eye Joe", "Rednex", "CD", "Single",  LocalDate.of(2022, 11, 16), 0.25f, ""),
    MusicEntry(5, "Fantasies & Delusions", "Billy Joel", "CD", "Album",  LocalDate.of(2023, 8, 10), 1.95f, ""),
    MusicEntry(6, "California Flash", "Billy Joel", "Vinyl", "Compilation",  LocalDate.of(1988, 1, 1), 10.00f, "this is in my mum's collection, she says this record really disappointed her because this is billy's early stuff where he did thrash metal and she doesn't like that. it turns out though that this record is rare because it was only ever released in new zealand, unlike his other records. i hope this description is long enough now"),
    MusicEntry(7, "Flaunt It", "Sigue Sigue Sputnik", "Vinyl", "Album",  LocalDate.of(1986, 1, 1), 12.00f, ""),
    MusicEntry(8, "Greatest Hits Vol. 2", "ABBA", "Vinyl", "Album",  null, null,  ""),
    MusicEntry(9, "Creep", "Radiohead", "Vinyl", "Single",  LocalDate.of(2001, 1, 1), null,  "Jukebox. No sleeve"),
    MusicEntry(10, "Why Does Love Do This To Me?", "The Exponents", "Cassette", "Single",  LocalDate.of(1991, 1, 1), null,  ""),
    MusicEntry(11, "Human Racing", "Nik Kershaw", "Cassette", "Album",  LocalDate.of(1985, 1, 1), null,  ""),
    MusicEntry(12, "Careless Whisper", "George Michael", "Vinyl", "Single",  null, null,  "Australian Promo version"),
    MusicEntry(13, "unknown music", "unknown artist", null, null,  null, null, null),
)


class MusicEntry(val id: Int,
                 var musicName: String,
                 var artistName: String,
                 var physicalFormat: String?,
                 var recordingFormat: String?,
                 var dateObtained: LocalDate?,
                 var pricePaid: Float?,
                 var notes: String?) : Comparable<MusicEntry> {
    override fun toString() = musicName

    // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/sort.html
    override fun compareTo(other: MusicEntry): Int
        = this.musicName.compareTo(other.musicName)

    companion object {
        /**
         * Reads a JSON file consisting of a single JSON array.
         */
        fun readArrayFromFile(context: Context): MutableList<MusicEntry> {
            var musicEntries = mutableListOf<MusicEntry>()

            val fileName = context.resources.getString(R.string.file)
            try {
                val file = context.openFileInput(fileName)
                val reader = JsonReader(InputStreamReader(file))
                println("readArrayFromFile: file exists")

                musicEntries = readArray(reader)
                reader.close()
            } catch (e: FileNotFoundException) {
                println("readArrayFromFile: file does NOT exist")
                // create empty json file (empty json array, no json objects)
                val file = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                val writer = JsonWriter(OutputStreamWriter(file))
                writer.setIndent("  ")
                writer.beginArray()

                // populate with some sample data!
                for(musicEntry in sampleData) {
                    println(musicEntry.toString())
                    musicEntry.write(writer)
                }

                writer.endArray()
                writer.close()
            }
            return musicEntries
        }

        /**
         * Reads a JSON array.
         */
        private fun readArray(reader: JsonReader) : MutableList<MusicEntry> {
            val musicEntries = mutableListOf<MusicEntry>()

            reader.beginArray()
            while (reader.hasNext()) {
                val musicEntry = read(reader)
                musicEntries.add(musicEntry)
            }
            reader.endArray()
//            reader.close()
            return musicEntries
        }

        /**
         * Reads a single JSON value which could be null.
         * Returns the value if it exists, else null.
         */
        private fun parseNullableValue(reader: JsonReader): String? {
            val parsedValue: String?
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                parsedValue = null
            } else {
                parsedValue = reader.nextString()
            }
            return parsedValue
        }

        /**
         * Reads a single JSON object.
         */
        private fun read(reader: JsonReader) : MusicEntry {
            var id: Int = -1
            lateinit var musicName: String // lateinit here, then initialise in the when statement
            lateinit var artistName: String
            var physicalFormat: String? = null // lateinit not needed on nullable types since we just init to null
            var recordingFormat: String? = null
            var dateObtained: LocalDate? = null
            var pricePaid: Float? = null
            var notes: String? = null

            reader.beginObject()
            while (reader.hasNext()) {

                val jsonKey = reader.nextName()
                when (jsonKey) {
                    "id" -> id = reader.nextInt()
                    "musicName" -> musicName = reader.nextString()
                    "artistName" -> artistName = reader.nextString()
                    "physicalFormat" -> physicalFormat = parseNullableValue(reader)
                    "recordingFormat" -> recordingFormat = parseNullableValue(reader)
                    "dateObtained" -> {
                        if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull()
                            dateObtained = null
                        } else {
                            dateObtained = LocalDate.parse(reader.nextString())
                        }
                    }
                    "pricePaid" -> pricePaid = parseNullableValue(reader)?.toFloat()
                    "notes" -> notes = parseNullableValue(reader)
                }
            }
            reader.endObject()
//            reader.close()
            return MusicEntry(id, musicName, artistName, physicalFormat, recordingFormat, dateObtained, pricePaid, notes)
        }


        /**
         * Reads a single JSON object with the given ID value.
         */
        fun readAtIndex(reader: JsonReader, index: Int) : MusicEntry {
            reader.beginArray()

            // skip elements until we get to the element we want to read
            for(i in 1 until index) {
                // start reading a new object
                reader.beginObject()
                while (reader.hasNext()) {
                    // skip all values in the object
                    reader.skipValue()
                }
                reader.endObject()
            }

            val musicEntry = read(reader)

            // now skip reading all other objects in the rest of the file (and then the end of the array)
            while (reader.hasNext()) {
                reader.skipValue()
            }

            reader.endArray()
//            reader.close()
            return musicEntry
        }
    }


    /**
     * Writes a single JSON object.
     */
    fun write(writer: JsonWriter) {
        writer.beginObject()
        writer.name("id").value(id)
        writer.name("musicName").value(musicName)
        writer.name("artistName").value(artistName)
        writer.name("physicalFormat").value(physicalFormat)
        writer.name("recordingFormat").value(recordingFormat)
        writer.name("dateObtained").value(if (dateObtained.toString() != "null") dateObtained.toString() else null)
        writer.name("pricePaid").value(pricePaid)
        writer.name("notes").value(notes)
        writer.endObject()
    }


    /**
     * Updates a single JSON object.
     */
    fun update(context: Context, updatedEntry: MusicEntry) {
        // find the index of the entry to update
        val musicEntries = readArrayFromFile(context)
        val indexToUpdate = musicEntries.indexOfFirst {
            it.id == updatedEntry.id
        }

        // if entry was found, update it
        if (indexToUpdate != -1) {
          musicEntries[indexToUpdate] = updatedEntry
        }

        // write updated entry to the file
        val fileName = context.resources.getString(R.string.file)
        val file = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        val writer = JsonWriter(OutputStreamWriter(file))
        writer.setIndent("  ")
        writer.beginArray()
        for (musicEntry in musicEntries) {
            musicEntry.write(writer)
        }

        writer.endArray()
        writer.close()
    }

}
