package com.catfisher.multiarielle;

import com.catfisher.multiarielle.clientServer.ModelClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatHandler {
    private final MessageHolder message;
    private final ModelClient client;

    private boolean inChat = false;

    public void startChatting() {
        message.clearMessage();
        message.focus();
        inChat = true;
    }

    public void acceptChatMessage() {
        inChat = false;
        String toSend = message.readAndUnfocus();
        client.forwardChatToServer(toSend);
        message.clearMessage();
    }

}
