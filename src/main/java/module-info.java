module org.example.chatsockets {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.chatsockets to javafx.fxml;
    exports org.example.chatsockets;
}