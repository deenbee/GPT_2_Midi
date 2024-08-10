package org.example.gpt_2_midi

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.scene.image.Image


class MidiApp : Application() {


    override fun start(primaryStage: Stage) {

        val fxmlLoader = FXMLLoader(javaClass.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 650.0, 700.0)

        // Establecer el icono de la aplicación
        val icon = Image(javaClass.getResourceAsStream("logo.png"))
        primaryStage.icons.add(icon)

        primaryStage.title = "GPT 2 MIDI"
        primaryStage.scene = scene
        primaryStage.isResizable = false
        primaryStage.show()
    }
}
