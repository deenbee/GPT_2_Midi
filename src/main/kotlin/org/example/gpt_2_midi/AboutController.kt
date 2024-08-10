package org.example.gpt_2_midi

import javafx.fxml.FXML
import java.net.URI
import java.awt.Desktop

class AboutController {


  @FXML
  private fun handleDonate() {
      try {
          val uri = URI("https://www.paypal.com/paypalme/dnbstartrek")
          Desktop.getDesktop().browse(uri)
      } catch (e: Exception) {
          e.printStackTrace()
      }
  }

}