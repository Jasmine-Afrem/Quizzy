module com.jasmine.quizzy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires mysql.connector.java;
    requires java.desktop;

    opens com.jasmine.quizzy to javafx.fxml;
    exports com.jasmine.quizzy;
}