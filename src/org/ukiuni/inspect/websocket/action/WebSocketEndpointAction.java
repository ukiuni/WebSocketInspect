package org.ukiuni.inspect.websocket.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/loadMessage")
public class WebSocketEndpointAction {
	public static List<Session> sessions = new ArrayList<Session>();

	@OnOpen
	public void onOpen(Session session) {
		// 開始時
		sessions.add(session);
	}

	@OnMessage
	public void onMessage(String message) throws IOException {
		// クライアントからの受信時
		for (Session session : sessions) {
			session.getBasicRemote().sendText("{\"command\":\"message\", \"text\": \"" + message.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}");
		}
	}

	@OnError
	public void onError(Throwable t) throws IOException {
		// エラー発生時
		for (Session session : sessions) {
			session.getBasicRemote().sendText("{\"command\":\"error\", \"text\": \"" + t.getMessage().replace("\\", "\\\\").replace("\"", "\\\"") + "\"}");
		}
	}

	@OnClose
	public void onClose(Session session) {
		// 完了時
		sessions.remove(session);
	}
}
