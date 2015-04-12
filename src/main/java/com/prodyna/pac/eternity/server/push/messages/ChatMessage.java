package com.prodyna.pac.eternity.server.push.messages;

public class ChatMessage extends Message {

	private String message;
	private ChatMessageType type;

	public ChatMessage(String message, ChatMessageType type) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public ChatMessageType getType() {
		return type;
	}
}
