module org.example.gpt_2_midi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop; // Add this line to require the java.desktop module
    requires kotlin.stdlib;


    opens org.example.gpt_2_midi to javafx.fxml;
    exports org.example.gpt_2_midi;
}