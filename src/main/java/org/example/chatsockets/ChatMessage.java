package org.example.chatsockets;

public class ChatMessage {
    // Tipos de mensajes para los estilos
    public enum Type { INFO, ME, OTHER };

    private Type type;
    private String text;

    public ChatMessage(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public Type getType() {
        return type;
    }
    public String getMessage() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}