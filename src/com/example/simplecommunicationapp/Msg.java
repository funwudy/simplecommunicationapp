package com.example.simplecommunicationapp;

import java.util.List;

public class Msg {
	
	private static final String SPLITTER = "£¡£¿£¡";

	private String speaker;
	private String content;
	
	public Msg(String speaker, String content) {
		this.speaker = speaker;
		this.content = content;
	}
	
	public Msg(String str) {
		deserialize(str);
	}

	public String getSpeaker() {
		return speaker;
	}

	public String getContent() {
		return content;
	}
	
	public String serialize() {
		String str = speaker + SPLITTER + content;
		return str;
	}
	
	public void deserialize(String str) {
		String[] members = str.split(SPLITTER);
		speaker = members[0];
		content = members[1];
	}
	
}
