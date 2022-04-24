package com.catfisher.multiarielle;

public interface MessageHolder {
    String readAndUnfocus();
    void clearMessage();
    void focus();
    void receiveMessage(String message);
}
