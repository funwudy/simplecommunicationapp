package com.example.simplecommunicationapp.model;

import com.example.simplecommunicationapp.log.LogUtil;

import android.text.TextUtils;

public class Msg {
	
	public static final String TAG = "Msg";

	private static final String SPLITTER = "¡·£¡£¿£¡¡¶";

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
		if (!TextUtils.isEmpty(str)) {
			String[] members = str.split(SPLITTER);
			speaker = members[0];
			if (members.length > 1) {
				content = members[1];
			} else {
				content = "";
			}
		} else {
			LogUtil.e(TAG, "Network receive error(=null)");
		}
	}

}
