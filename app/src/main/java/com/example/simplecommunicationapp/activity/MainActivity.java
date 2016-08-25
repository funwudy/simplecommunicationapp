package com.example.simplecommunicationapp.activity;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.example.simplecommunicationapp.R;
import com.example.simplecommunicationapp.R.id;
import com.example.simplecommunicationapp.R.layout;
import com.example.simplecommunicationapp.model.Msg;
import com.example.simplecommunicationapp.model.MsgAdapter;
import com.example.simplecommunicationapp.thread.ClientThread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	public static Charset charset = Charset.forName("UTF-8");

	private Handler uiHandler;

	private EditText editText;
	private Button sendButton;
	private ListView msgView;

	private List<Msg> msgList = new ArrayList<Msg>();

	private MsgAdapter adapter;

	private ClientThread clientThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		msgView = (ListView) findViewById(R.id.msg_view);
		adapter = new MsgAdapter(this, R.layout.msg_item, msgList);
		msgView.setAdapter(adapter);

		editText = (EditText) findViewById(R.id.input_text);

		sendButton = (Button) findViewById(R.id.send_button);
		sendButton.setOnClickListener(this);

		uiHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ClientThread.UPDATE_MSG:
					Msg newMsg = (Msg) msg.obj;
					updateMsgView(newMsg);
					break;
				default:
					break;
				}
			}
		};

		clientThread = new ClientThread(uiHandler);
		clientThread.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_button:
			if (TextUtils.isEmpty(editText.getText())) {
				Toast.makeText(this, "Message cannot be empty!", Toast.LENGTH_SHORT).show();
			} else {
				Handler netHandler = clientThread.getNetHandler();
				if (netHandler != null) {
					final String content = editText.getText().toString();
					updateMsgView(new Msg(ClientThread.getUserName(), content));
					editText.setText("");

					Message message = new Message();
					message.what = ClientThread.SEND_MSG;
					message.obj = new Msg(ClientThread.getUserName(), content);
					netHandler.sendMessage(message);
				} else {
					Toast.makeText(this, "The network is not working", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		default:
			break;
		}
	}

	private void updateMsgView(Msg msg) {
		msgList.add(msg);
		adapter.notifyDataSetChanged();
		msgView.setSelection(msgList.size());
	}

}
