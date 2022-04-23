package com.catfisher.multiarielle;

import com.catfisher.multiarielle.clientServer.ModelClient;
import com.catfisher.multiarielle.clientServer.ProxyServer;
import com.catfisher.multiarielle.clientServer.event.client.ClientChatEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class ChatHandler {
    private final MessageHolder message;
    private final ModelClient client;

    private boolean inChat = false;

    public void startChatting() {
        message.clearMessage();
        inChat = true;
    }

    public void acceptChatMessage() {
        inChat = false;
        String toSend = message.getCurrentMessage();
        client.forwardChatToServer(toSend);
        message.clearMessage();
    }

}
