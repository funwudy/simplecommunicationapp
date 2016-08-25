package com.example.simplecommunicationapp.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.example.simplecommunicationapp.activity.MainActivity;
import com.example.simplecommunicationapp.model.Msg;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ClientThread extends Thread {

	public static final int PORT = 22222;
	public static final String SERVER_ADDR = "10.0.2.2";
	public static final int UPDATE_MSG = 1;
	public static final int SEND_MSG = 2;
	
	private static String userName;
	
	public static String getUserName() {
		return userName;
	}
	
	private Selector selector;

	private SocketChannel sc = null;

	private Handler uiHandler;
	private Handler netHandler;

	public ClientThread(Handler handler) {
		this.uiHandler = handler;
	}

	public Handler getNetHandler() {
		return netHandler;
	}

	public void run() {
		try {
			selector = Selector.open();
			InetSocketAddress isa = new InetSocketAddress(SERVER_ADDR, PORT);
			sc = SocketChannel.open(isa);
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_READ);
			
			userName = "User" + sc.socket().getLocalSocketAddress();

			new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();// do not forget!!!
					netHandler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							switch (msg.what) {
							case SEND_MSG:
								String message = ((Msg) msg.obj).serialize();
								try {
									sc.write(MainActivity.charset.encode(message));
								} catch (IOException e) {
									e.printStackTrace();
								}
								break;
							default:
								break;
							}
						}
					};
					Looper.loop();// do not forget!!!
				}
			}).start();

			while (selector.select() > 0) {
				for (SelectionKey key : selector.selectedKeys()) {
					selector.selectedKeys().remove(key);
					if (key.isReadable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						String message = "";
						//updateMsg("before read, buffer.pos = " + buffer.position() + ", buffer.limit = " + buffer.limit());	
						while (sc.read(buffer) > 0) {
							//updateMsg("after read, buffer.pos = " + buffer.position() + ", buffer.limit = " + buffer.limit());
							buffer.flip();
							//updateMsg("after flip, buffer.pos = " + buffer.position() + ", buffer.limit = " + buffer.limit());
							message += MainActivity.charset.decode(buffer);
							//updateMsg("after decode, buffer.pos = " + buffer.position() + ", buffer.limit = " + buffer.limit());
						}
						Msg msg = new Msg(message);
						updateMsg(msg);
						key.interestOps(SelectionKey.OP_READ);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (sc != null)
				try {
					sc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private boolean updateMsg(Msg msg) {
		Message message = new Message();
		message.what = UPDATE_MSG;
		message.obj = msg;
		return uiHandler.sendMessage(message);
	}
}