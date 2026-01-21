package org.example.chatsockets;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ChatController {
    // Referencias de la vista
    @FXML private ListView<ChatMessage> listMessages;
    @FXML private TextField inputName;
    @FXML private TextField inputMessage;
    @FXML private Button buttonConnect;
    @FXML private Button buttonSend;

    // Cliente de socket, maneja la conexión y recibe/envía mensajes
    private Client client = new Client();
    // Nombre del usuario
    private String myName = "";

    @FXML public void initialize() {
        // Activar y desactivar elementos de la UI al no estar conectado
        buttonConnect.setDisable(false);
        buttonSend.setDisable(true);
        inputMessage.setDisable(true);

        // Define cómo se muestra cada mensaje en la UI
        listMessages.setCellFactory(listView -> new ListCell<>() {
            private Label bubble = new Label();
            {
                bubble.getStyleClass().add("bubble");
                bubble.setWrapText(true);
            }
            @Override
            protected void updateItem(ChatMessage item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("info", "me", "other");

                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                // Mostrar el texto
                bubble.setText(item.getMessage());
                setGraphic(bubble);

                // Asignar la clase según el tipo
                if (item.getType() == ChatMessage.Type.INFO) {
                    getStyleClass().add("info");
                } else if (item.getType() == ChatMessage.Type.ME) {
                    getStyleClass().add("me");
                } else {
                    getStyleClass().add("other");
                }
            }
        });
    }

    // Conectarse al chat
    @FXML public void connectToChat() {
        // Impedir que el usuario se conecte más de una vez
        if (client.isConnected()) return;
        // Validar el nombre
        String name = inputName.getText();
        if (name.isEmpty()) {
            addInfo("Info: Tienes que escribir tu nombre.");
            return;
        }
        myName = name;

        try {
            // Conectar y escuchar mensajes del servidor
            client.connect("localhost", 8080, myName, this::serverMessage);

            // Activar y desactivar elementos de la UI al conectar
            buttonConnect.setDisable(true);
            inputName.setDisable(true);
            buttonSend.setDisable(false);
            inputMessage.setDisable(false);

            addInfo("Info: Conectado como " + myName);
        } catch (Exception e) {
            addInfo("Info: Error al conectar: " + e.getMessage());
        }
    }

    // Enviar mensajes
    @FXML public void sendMessage() {
        // Impedir enviar mensajes si no está conectado
        if (!client.isConnected()) {
            addInfo("Info: No estás conectado al chat.");
            return;
        }

        // Validar el mensaje
        String message = inputMessage.getText();
        if (message.isEmpty()) return;

        // Si escribe "salir" cerramos la ventana
        if (message.equalsIgnoreCase("salir")) {
            client.send("salir");
            client.close();
            Platform.exit();
            return;
        }

        // Enviar mensaje y el servidor lo enviará a todos
        client.send(message);
        inputMessage.clear();
    }

    // Mensajes que vienen del servidor
    private void serverMessage(String message) {
        // Actualizar la UI
        Platform.runLater(() -> {
            // Mensajes del sistema
            if (message.startsWith("Info: ")) {
                addInfo(message);
                return;
            }

            // Mensajes de los usuarios tanto propios como ajenos
            if (message.startsWith(myName + ": ")) {
                addMe(message);
            } else {
                addOther(message);
            }
        });
    }

    // Agregar mensajes al chat
    private void addInfo(String text) {
        listMessages.getItems().add(new ChatMessage(ChatMessage.Type.INFO, text));
        listMessages.scrollTo(listMessages.getItems().size() - 1);
    }
    private void addMe(String text) {
        String message = text.replaceFirst("^" + java.util.regex.Pattern.quote(myName) + ":\\s*", "");
        listMessages.getItems().add(new ChatMessage(ChatMessage.Type.ME, message));
        listMessages.scrollTo(listMessages.getItems().size() - 1);
    }
    private void addOther(String text) {
        listMessages.getItems().add(new ChatMessage(ChatMessage.Type.OTHER, text));
        listMessages.scrollTo(listMessages.getItems().size() - 1);
    }
}