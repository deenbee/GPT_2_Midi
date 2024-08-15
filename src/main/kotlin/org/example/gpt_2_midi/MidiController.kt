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
import javafx.stage.Stage
import javafx.scene.control.MenuItem
import javafx.scene.control.Tooltip
import javafx.application.Platform
import javafx.fxml.LoadException
import javafx.scene.image.Image


class MidiController {

    lateinit var btn_save: Button // Botón para guardar el fichero .midi

    @FXML
    private lateinit var btn_gen: Button

    @FXML
    private lateinit var channel1: TextArea


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
    private lateinit var menu_genre: MenuButton

    @FXML
    private lateinit var menu_btn_bpm: MenuButton

    @FXML
    private lateinit var menu_btn_sign: MenuButton

    @FXML
    private lateinit var menu_btn_key: MenuButton

    private var aboutWin: Boolean = false

    var version: String = "0.5 Beta"

    // Variables que se actualizan automáticamente al llenar los cuadros de textos
    private var genreText: String = ""
    private var keyText: String = ""
    private var bpmText: String = ""
    private var signatureText: String = ""
    private var timeText: String = ""


    private var Midi_obj = Midi()


    @FXML
    fun initialize() {

        enable_text_area()

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


    private fun disable_text_area(){
        channel1.isDisable = true

    }

    fun enable_text_area(){
        channel1.isDisable = false

    }


    // Accion del boton about
    @FXML
    private fun handleAbout() {

        if (!aboutWin) {

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

                aboutWin = true

                // Agregar un listener para limpiar la referencia cuando la ventana se cierre
                aboutStage.setOnHiding {
                    aboutWin = false
                }

            } catch (e: LoadException) {
                e.printStackTrace()
                println("Error loading FXML file: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                println("General error: ${e.message}")
            }

        }
    }




    // Accion del boton de generar el prompt
    @FXML
    private fun handleGeneratePrompt() {

        val pase = prompt_verification()

        if (pase) {

            // Crear el texto predeterminado basado en las entradas
            val promptText = "Create a song, based on this format (for example):\n" +
                    "\n" +
                    "Let's say you want the following notes format to be played on my TextArea:\n" +
                    "\n" +
                    "Number of channel midi (1-16).\n" +
                    "Instrument (0-127).\n" +
                    "Position of note (ticks).\n" +
                    "Note E4 (64).\n" +
                    "Duration (ticks).\n" +
                    "Velocity (0-127).\n" +
                    "\n" +
                    "The text you would paste in the first text box would be:\n" +
                    "01 00 0 60 24 93, 02 00 24 62 24 80, 03 41 48 64 24 100, 10 00 72 35 48 127, .......\n" +
                    "NOTE: DO NOT USE COMMENTS ON THE PROMPT GENERATED \n" +
                    "\n" +
                    "Prompt for the generation is:\n" +
                    "Now I have a TextArea and I want you to create a song with characteristics of $keyText key signature, $bpmText bpm and time signature $signatureText of $genreText music genre, with a duration of the song approximately $timeText seconds."

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

        } else {
            // Mostrar el aviso
            val tooltip = Tooltip("Error: empty data, please check bpm/signature data")
            Tooltip.install(btn_gen, tooltip)
        }



    }



    // Accion del boton de guardar el fichero
    @FXML
    private fun handleGenerateButton() {

        try {

            disable_text_area()

            val pasar = verifificar()

            if (pasar) {
                println("Generando secuencia")
                Midi_obj.generar_secuencia(channel1, bpmText, signatureText)

                Midi_obj.save_file(btn_save) // Guardar fichero
                enable_text_area()
            } else {
                enable_text_area()
            }


        } catch (e: Exception) {
            e.printStackTrace()
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

   // Verificar datos antes de mostrar aviso
   private fun verifificar(): Boolean {
        // Verificar si el canal 1 está vacío o no contiene datos válidos
        if (channel1.text.isBlank() || !containsValidData(channel1.text) || (signatureText.isBlank()) || bpmText.isBlank()) {
            showAlert("Channel 1 empty", "The channel/signature/bpm do not have a valid data, check signature, channel and bpm.")

            return false
        } else {
            return true
        }
    }

    private fun prompt_verification(): Boolean {
        if (signatureText.isBlank() || bpmText.isBlank() || genreText.isBlank() || timeText.isBlank() || keyText.isBlank() ) {
            showAlert("Prompt generation error", "Maybe the signature, bmp, key, song time or music genre are empty, please add a data")

            return false
        } else {
            return true
        }
    }

    @FXML
    private fun play_seq() {

        val pase = verifificar()

        if (pase){

            Midi_obj.generar_secuencia(channel1, bpmText, signatureText)
            Midi_obj.play()

        }

    }

    @FXML
    private fun stop_seq (){
        enable_text_area()
        Midi_obj.stop()
    }


    // Función para verificar si el texto contiene datos válidos
    private fun containsValidData(text: String): Boolean {
        return text.split(",").any { noteData ->
            val trimmedNoteData = noteData.trim()
            if (trimmedNoteData.isNotEmpty()) {
                val noteParts = trimmedNoteData.split(" ").map { it.trim() }
                if (noteParts.size == 6) {
                    try {
                        noteParts.forEach { it.toInt() }
                        true
                    } catch (e: NumberFormatException) {
                        false
                    }
                } else {
                    false
                }
            } else {
                false
            }
        }
    }





}