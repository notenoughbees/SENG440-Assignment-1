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
    MusicEntry(1, "Folie à Deux", "Fall Out Boy", "CD", "Album",  LocalDate.of(2022, 9, 30), "New Zealand", "5", "Digipak.\n\nthe australasian release with the extra songs.\ncomes with poster!"),
    MusicEntry(2, "Hesitant Alien", "Gerard Way", "CD", "Album",  LocalDate.of(2022, 11, 16), "Penny Lane","15", "comes with poster"),
    MusicEntry(3, "Midnights", "Taylor Swift", "CD", "Album",  LocalDate.of(2022, 11, 16), "JB HiFi Christchurch", "19.99", "bought new @ 20% off"),
    MusicEntry(4, "Cotton Eye Joe", "Rednex", "CD", "Single",  LocalDate.of(2022, 11, 16), null, "0.25", "comes with 4 remixes of cotton eye joe plus original song, which is 5 cotton eye joe's too many"),
    MusicEntry(5, "Fantasies & Delusions", "Billy Joel", "CD", "Album",  LocalDate.of(2023, 8, 10), "UK", "1.95", null),
    MusicEntry(6, "California Flash", "Billy Joel", "Vinyl", "Compilation",  LocalDate.of(1988, 1, 1), "Wellington", "10.00", "this is in my mum's collection, she says this record really disappointed her because this is billy's early stuff where he did thrash metal and she doesn't like that. it turns out though that this record is rare because it was only ever released in new zealand, unlike his other records. i hope this description is long enough now."),
    MusicEntry(7, "Flaunt It", "Sigue Sigue Sputnik", "Vinyl", "Album",  LocalDate.of(1986, 1, 1), null, "12.00", ""),
    MusicEntry(8, "Greatest Hits Vol. 2", "ABBA", "Vinyl", "Album",  null, null, null,  null),
    MusicEntry(9, "Creep", "Radiohead", "Vinyl", "Single",  LocalDate.of(2001, 1, 1), null, null,  "Jukebox. No sleeve"),
    MusicEntry(10, "Why Does Love Do This To Me?", "The Exponents", "Cassette", "Single",  LocalDate.of(1991, 1, 1), null, null,  null),
    MusicEntry(11, "Human Racing", "Nik Kershaw", "Cassette", "Album",  LocalDate.of(1985, 1, 1), null, null,  null),
    MusicEntry(12, "Careless Whisper", "George Michael", "Vinyl", "Single",  null, null, null,  "Australian Promo version"),
    MusicEntry(13, "unknown music test", "unknown artist", null, null,  null, null, null, null),
    MusicEntry(14, "long music test", "long artist", "CD", "Album",  LocalDate.of(2023, 8, 29), null, "0.00", "What is Lorem Ipsum?\n" +
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it?\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\n" +
            "Where does it come from?\n" +
            "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
            "\n" +
            "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.\n" +
            "\n" +
            "Where can I get some?\n" +
            "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc."),
)


class MusicEntry(val id: Int,
                 var musicName: String,
                 var artistName: String,
                 var physicalFormat: String?,
                 var recordingFormat: String?,
                 var dateObtained: LocalDate?,
                 var whereObtained: String?,
                 var pricePaid: String?,
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
            var whereObtained: String? = null
            var pricePaid: String? = null
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
                    "whereObtained" -> whereObtained = parseNullableValue(reader)
                    "pricePaid" -> pricePaid = parseNullableValue(reader)
                    "notes" -> notes = parseNullableValue(reader)
                }
            }
            reader.endObject()
//            reader.close()
            return MusicEntry(id, musicName, artistName, physicalFormat, recordingFormat, dateObtained, whereObtained, pricePaid, notes)
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
        writer.name("whereObtained").value(whereObtained)
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
