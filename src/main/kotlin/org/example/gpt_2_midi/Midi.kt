package org.example.gpt_2_midi

import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import javax.sound.midi.*

class Midi  {

    private var secuencia: Sequence? = null
    private var sequencer: Sequencer? = null
    private var seq_running: Boolean = false


    // Función para parsear el texto de los canales
    private fun parseChannelText(text: String): List<MidiEvent> {
        val events = mutableListOf<MidiEvent>()

        text.split(",").forEach { entry ->
            val trimmedEntry = entry.trim()

            if (trimmedEntry.isNotEmpty()) {
                val parts = trimmedEntry.split(" ").map { it.trim() }

                if (parts.size == 6) {
                    try {
                        val channel = parts[0].toInt() - 1
                        val instrument = parts[1].toInt()
                        val position = parts[2].toInt().toLong()
                        val note = parts[3].toInt()
                        val duration = parts[4].toInt()
                        val velocity = parts[5].toInt()

                        val programChange = ShortMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0)
                        events.add(MidiEvent(programChange, 0))

                        val messageOn = ShortMessage(ShortMessage.NOTE_ON, channel, note, velocity)
                        val messageOff = ShortMessage(ShortMessage.NOTE_OFF, channel, note, 0)

                        events.add(MidiEvent(messageOn, position))
                        events.add(MidiEvent(messageOff, position + duration.toLong()))
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                } else {
                    println("Entrada malformada: $trimmedEntry")
                }
            }
        }

        return events
    }


    // Función para generar la secuencia MIDI
    private fun generateMidiSequence(channelTexts: List<String>, bpmText: String, signatureText: String): Sequence {
        val sequence = Sequence(Sequence.PPQ, 24)
        val track = sequence.createTrack()

        // Procesar y agregar notas
        val events = parseChannelText(channelTexts.first())
        events.forEach { track.add(it) }

        // Agregar BPM
        val bpm = bpmText.toIntOrNull() ?: 120 // Valor predeterminado si no se puede convertir
        val microsecondsPerQuarterNote = 60000000 / bpm
        val bpmMessage = MetaMessage()
        val data = byteArrayOf(
            ((microsecondsPerQuarterNote shr 16) and 0xFF).toByte(),
            ((microsecondsPerQuarterNote shr 8) and 0xFF).toByte(),
            (microsecondsPerQuarterNote and 0xFF).toByte()
        )
        bpmMessage.setMessage(0x51, data, data.size)
        track.add(MidiEvent(bpmMessage, 0))

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
            track.add(MidiEvent(signatureMessage, 0))
        }

        secuencia = sequence
        return sequence

    }

    fun generar_secuencia(textArea: TextArea, bpmText: String, signatureText: String) {
        val sequence = generateMidiSequence(listOf(textArea.text), bpmText, signatureText)
        secuencia = sequence
    }


    fun play() {

        try {
            // Obtener un secuenciador y abrirlo
            sequencer = MidiSystem.getSequencer().apply {
                open()
                sequence = secuencia

                start()

                // Esperar hasta que la reproducción termine o se alcance el límite de duración
                while (isRunning) {
                   Thread.sleep(100)
                }
            }
            seq_running = true
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            // Asegurarse de cerrar el secuenciador
            try {
                sequencer?.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            seq_running = false
        }
    }



    fun stop() {
        if (seq_running) {
            sequencer?.stop()
            sequencer?.close()
            seq_running = false
        }
    }

    // Función para guardar el archivo MIDI
    private fun saveMidiFile(sequence: Sequence, btn_save: Button) {
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

    fun save_file(btn_save: Button){
        secuencia?.let {
            saveMidiFile(it, btn_save)
            println("Guardando secuencia en un archivo")
        } ?: println("Error: La secuencia es nula y no se puede guardar.")
    }

}