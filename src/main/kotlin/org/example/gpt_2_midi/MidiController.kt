package org.example.gpt_2_midi

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader

import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.MenuButton
import javafx.scene.control.TextArea
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import javax.sound.midi.*
import javafx.scene.control.MenuItem
import javafx.scene.control.Tooltip
import javafx.application.Platform
import javafx.fxml.LoadException
import javafx.scene.image.Image


class MidiController {

    @FXML
    private lateinit var btn_gen: Button



    @FXML
    private lateinit var channel1: TextArea

    @FXML
    private lateinit var channel2: TextArea

    @FXML
    private lateinit var channel3: TextArea

    @FXML
    private lateinit var channel4: TextArea

    @FXML
    private lateinit var channel5: TextArea

    @FXML
    private lateinit var channel6: TextArea

    @FXML
    private lateinit var channel7: TextArea

    @FXML
    private lateinit var channel8: TextArea

    @FXML
    private lateinit var music_genre: TextArea // Género musical

    @FXML
    private lateinit var key_txt: TextArea

    @FXML
    private lateinit var time: TextArea // Song duration in minutes

    @FXML
    private lateinit var bpm_: TextArea // Tempo /bpm

    @FXML
    private lateinit var signature: TextArea // Compás

    @FXML
    private lateinit var btn_save: Button // Botón para guardar el fichero .midi

    @FXML
    private lateinit var menu_genre: MenuButton

    @FXML
    private lateinit var menu_btn_bpm: MenuButton

    @FXML
    private lateinit var menu_btn_sign: MenuButton

    @FXML
    private lateinit var menu_btn_key: MenuButton


    // Variables que se actualizan automáticamente al llenar los cuadros de textos
    private var genreText: String = ""
    private var keyText: String = ""
    private var bpmText: String = ""
    private var signatureText: String = ""
    private var timeText: String = ""

    @FXML
    private fun initialize() {

        // Lista de géneros musicales
        val genres = listOf(
            "Rock", "Pop", "Jazz", "Classical", "Hip Hop", "Electronic", "Country", "Blues",
            "Reggae", "Folk", "Metal", "Punk", "R&B", "Soul", "Funk", "Disco", "Indie",
            "Alternative", "Latin", "World", "Opera", "New Age", "Ambient", "House", "Techno",
            "Trance", "Dubstep", "EDM", "Trap", "K-Pop", "J-Pop", "Gospel", "Ska", "Reggaeton",
            "Cumbia", "Salsa", "Bachata", "Tango", "Flamenco", "Bluegrass", "Celtic", "Grunge",
            "Progressive", "Symphonic", "Industrial", "Gothic", "Experimental", "Noise", "Chiptune",
            "Drum and Bass", "Garage", "IDM", "Trip Hop", "Acoustic", "Vocal", "Choral", "Baroque",
            "Romantic", "Modern", "Minimalism", "Avant-Garde", "Film Score", "Video Game Music",
            "Spoken Word", "Comedy", "Children's Music", "Holiday", "Religious", "Medieval", "Renaissance"
        )

        val keys = listOf(
            "C Major: No sharps or flats (Default)",
            "G Major: One sharp (F#)",
            "D Major: Two sharps (F#, C#)",
            "A Major: Three sharps (F#, C#, G#)",
            "E Major: Four sharps (F#, C#, G#, D#)",
            "B Major: Five sharps (F#, C#, G#, D#, A#)",
            "F# Major: Six sharps (F#, C#, G#, D#, A#, E#)",
            "C# Major: Seven sharps (F#, C#, G#, D#, A#, E#, B#)",
            "F Major: One flat (Bb)",
            "Bb Major: Two flats (Bb, Eb)",
            "Eb Major: Three flats (Bb, Eb, Ab)",
            "Ab Major: Four flats (Bb, Eb, Ab, Db)",
            "Db Major: Five flats (Bb, Eb, Ab, Db, Gb)",
            "Gb Major: Six flats (Bb, Eb, Ab, Db, Gb, Cb)",
            "Cb Major: Seven flats (Bb, Eb, Ab, Db, Gb, Cb, Fb)",
            "A Minor: No sharps or flats",
            "E Minor: One sharp (F#)",
            "B Minor: Two sharps (F#, C#)",
            "F# Minor: Three sharps (F#, C#, G#)",
            "C# Minor: Four sharps (F#, C#, G#, D#)",
            "G# Minor: Five sharps (F#, C#, G#, D#, A#)",
            "D# Minor: Six sharps (F#, C#, G#, D#, A#, E#)",
            "A# Minor: Seven sharps (F#, C#, G#, D#, A#, E#, B#)",
            "D Minor: One flat (Bb)",
            "G Minor: Two flats (Bb, Eb)",
            "C Minor: Three flats (Bb, Eb, Ab)",
            "F Minor: Four flats (Bb, Eb, Ab, Db)",
            "Bb Minor: Five flats (Bb, Eb, Ab, Db, Gb)",
            "Eb Minor: Six flats (Bb, Eb, Ab, Db, Gb, Cb)",
            "Ab Minor: Seven flats (Bb, Eb, Ab, Db, Gb, Cb, Fb)"
        )

        // Lista de compas
        val _signatures = listOf("2/4", "3/4", "4/4 (def.)", "6/8")

        // Lista de BPM
        val bpms = (90..180).toList()

        // Añadir opciones al MenuButton
        for (genre in genres) {
            val item = MenuItem(genre)
            item.setOnAction { music_genre.text = genre ; println("$genre selected") }
            menu_genre.items.add(item)
        }

        // Añadir opciones al MenuButton de BPM
        for (bpm in bpms) {
            val item = MenuItem(bpm.toString())
            item.setOnAction { bpm_.text = bpm.toString() }
            menu_btn_bpm.items.add(item)
        }

        // Añadir compas del boton al textArea
        for (_signature in _signatures){
            val item = MenuItem(_signature)
            item.setOnAction {  signature.text = _signature }
            menu_btn_sign.items.add(item)
        }

        // Añadir lista de keys al textArea
        for (key in keys) {
            val item = MenuItem(key)
            item.setOnAction {  key_txt.text = key }
            menu_btn_key.items.add(item)
        }


        // Listeners para actualizar las variables cada vez que se escribe en los cuadros de texto
        music_genre.textProperty().addListener { _, _, newValue ->
            genreText = newValue
        }

        bpm_.textProperty().addListener { _, _, newValue ->
            bpmText = newValue
        }

        signature.textProperty().addListener { _, _, newValue ->
            signatureText = newValue
        }

        time.textProperty().addListener { _, _, newValue ->
            timeText = newValue
        }

        key_txt.textProperty().addListener { _, _, newValue ->
            keyText = newValue
        }


    }

    // Accion del boton about
    @FXML
    private fun handleAbout() {
        try {
            // Cargar el archivo FXML de la ventana "about"
            val loader = FXMLLoader(javaClass.getResource("about.fxml"))

            val scene_ = Scene(loader.load(), 320.0, 355.0)

            // Crear una nueva instancia de Stage
            val aboutStage = Stage()

            // Establecer el icono de la aplicación
            val icon = Image(javaClass.getResourceAsStream("logo.png"))
            aboutStage.icons.add(icon)

            aboutStage.title = "About"
            aboutStage.isResizable = false
            aboutStage.scene = scene_

            // Mostrar la nueva ventana
            aboutStage.show()
        } catch (e: LoadException) {
            e.printStackTrace()
            println("Error loading FXML file: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            println("General error: ${e.message}")
        }
    }




    // Accion del boton de generar el prompt
    @FXML
    private fun handleGeneratePrompt() {
        // Crear el texto predeterminado basado en las entradas
        val promptText = "Create a song, based on this format (for example):\n" +
                "\n" +
                "Let's say you want the following notes to be played on Channel 1 (the first text box):\n" +
                "\n" +
                "Note C4 (60), duration of 24 ticks, with a speed of 93.\n" +
                "Note D4 (62), duration of 48 ticks, with a speed of 100.\n" +
                "Note E4 (64), duration of 24 ticks, with a speed of 80.\n" +
                "\n" +
                "The text you would paste in the first text box would be:\n" +
                "60 24 93, 62 48 100, 64 24 80\n" +
                "\n" +
                "Prompt for the generation is:\n" +
                "Now I have 8 textboxes/channels and I want you to create a song with characteristics of $keyText key signature, $bpmText bpm and time signature $signatureText of $genreText music genre, with a duration of the song approximately $timeText minutes."

        // Copiar el texto al portapapeles
        val clipboard = Clipboard.getSystemClipboard()
        val content = ClipboardContent()
        content.putString(promptText)
        clipboard.setContent(content)

        // Mostrar el aviso
        val tooltip = Tooltip("Content copied to clipboard successfully!")
        Tooltip.install(btn_gen, tooltip)

        // Ocultar el tooltip después de un tiempo
        tooltip.show(btn_gen, btn_gen.scene.window.x + 200, btn_gen.scene.window.y + 250)
        java.util.Timer().schedule(object : java.util.TimerTask() {
            override fun run() {
                Platform.runLater {
                    tooltip.hide()
                }
            }
        }, 2000) // Ocultar después de 2 segundos


    }



    // Accion del boton de guardar el fichero
    @FXML
    private fun handleGenerateButton() {
        try {
            // Verificar si el canal 1 está vacío o no contiene datos válidos
            if (channel1.text.isBlank() || !containsValidData(channel1.text)) {
                showAlert("Channel 1 empty", "The channel do not have a valid data.")
                return
            }

            println("Generando secuencia")
            // Generar la secuencia MIDI con BPM y Time Signature
            val sequence = generateMidiSequence(
                listOf(channel1.text, channel2.text, channel3.text, channel4.text, channel5.text, channel6.text, channel7.text, channel8.text),
                bpmText,
                signatureText
            )

            // Guardar la secuencia MIDI en un archivo
            saveMidiFile(sequence)
            println("Guardando secuencia en un archivo")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Función para generar la secuencia MIDI
    fun generateMidiSequence(channelTexts: List<String>, bpmText: String, signatureText: String): Sequence {
        val sequence = Sequence(Sequence.PPQ, 24)
        val track = sequence.createTrack()

        // Procesar y agregar notas de cada canal
        channelTexts.forEachIndexed { channelIndex, channelText ->
            val events = parseChannelText(channelText, channelIndex)
            events.forEach { track.add(it) }
        }

        // Agregar BPM
        val bpm = bpmText.toIntOrNull() ?: 120 // Valor predeterminado si no se puede convertir
        val microsecondsPerQuarterNote = 60000000 / bpm
        val bpmMessage = MetaMessage()
        val data = byteArrayOf(
            ((microsecondsPerQuarterNote shr 16) and 0xFF).toInt().toByte(),
            ((microsecondsPerQuarterNote shr 8) and 0xFF).toInt().toByte(),
            (microsecondsPerQuarterNote and 0xFF).toInt().toByte()
        )
        bpmMessage.setMessage(0x51, data, data.size)
        track.add(javax.sound.midi.MidiEvent(bpmMessage, 0))

        // Agregar Time Signature
        val signatureParts = signatureText.split("/")
        if (signatureParts.size == 2) {
            val numerator = signatureParts[0].toIntOrNull() ?: 4
            val denominator = signatureParts[1].toIntOrNull() ?: 4

            val signatureMessage = MetaMessage()
            val signatureData = byteArrayOf(
                numerator.toByte(),
                (Math.log(denominator.toDouble()) / Math.log(2.0)).toInt().toByte(),
                24.toByte(),  // MIDI clocks per metronome click
                8.toByte()    // 32nd notes per 24 MIDI clocks
            )
            signatureMessage.setMessage(0x58, signatureData, signatureData.size)
            track.add(javax.sound.midi.MidiEvent(signatureMessage, 0))
        }

        return sequence
    }

    // Función para parsear el texto de los canales
    private fun parseChannelText(text: String, channel: Int): List<MidiEvent> {
        val events = mutableListOf<MidiEvent>()
        var tick = 0L

        text.split(",").forEach { noteData ->
            val trimmedNoteData = noteData.trim()

            // Verifica si la cadena no está vacía
            if (trimmedNoteData.isNotEmpty()) {
                val noteParts = trimmedNoteData.split(" ").map { it.trim() }

                // Asegúrate de que el texto tenga tres partes (nota, duración, velocidad)
                if (noteParts.size == 3) {
                    try {
                        val note = noteParts[0].toInt()
                        val duration = noteParts[1].toInt()
                        val velocity = noteParts[2].toInt()

                        val messageOn = ShortMessage(ShortMessage.NOTE_ON, channel, note, velocity)
                        val messageOff = ShortMessage(ShortMessage.NOTE_OFF, channel, note, velocity)

                        events.add(MidiEvent(messageOn, tick))
                        tick += duration.toLong()
                        events.add(MidiEvent(messageOff, tick))
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        // Puedes manejar el error aquí, como saltar esta entrada o mostrar un mensaje al usuario
                    }
                } else {
                    // Manejo de entradas mal formadas
                    println("Entrada malformada: $trimmedNoteData")
                }
            }
        }

        return events
    }

    // Función para verificar si el texto contiene datos válidos
    private fun containsValidData(text: String): Boolean {
        // Verificar si el texto contiene al menos una entrada válida
        return text.split(",").any { noteData ->
            val trimmedNoteData = noteData.trim()
            if (trimmedNoteData.isNotEmpty()) {
                val noteParts = trimmedNoteData.split(" ").map { it.trim() }
                if (noteParts.size == 3) {
                    try {
                        noteParts.map { it.toInt() }
                        return true
                    } catch (e: NumberFormatException) {
                        return false
                    }
                }
            }
            false
        }
    }

    // Función para mostrar un aviso
    private fun showAlert(title: String, message: String) {
        val alert = Alert(Alert.AlertType.WARNING)
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }

    // Función para guardar el archivo MIDI
    private fun saveMidiFile(sequence: Sequence) {
        // Crear un FileChooser
        val fileChooser = FileChooser()
        fileChooser.title = "Save the MIDI file"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Files MIDI", "*.mid"))

        // Mostrar el diálogo para guardar archivo usando el Stage actual
        val file: File? = fileChooser.showSaveDialog(btn_save.scene.window as Stage)

        // Verificar si se seleccionó un archivo
        if (file != null) {
            MidiSystem.write(sequence, 1, file)
        }
    }
}