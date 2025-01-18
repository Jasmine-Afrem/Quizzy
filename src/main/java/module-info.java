module com.jasmine.quizzy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires mysql.connector.java;
    requires java.desktop;
    requires org.junit.jupiter.api;  // Add this line for JUnit dependency

    opens com.jasmine.quizzy to javafx.fxml;
    exports com.jasmine.quizzy;
}
