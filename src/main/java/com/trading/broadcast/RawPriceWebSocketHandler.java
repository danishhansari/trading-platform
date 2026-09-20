package com.trading.broadcast;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class RawPriceWebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, List<WebSocketSession>> sessionsByCompany = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long companyId = extractCompanyId(session);
        sessionsByCompany
                .computeIfAbsent(companyId, id -> new CopyOnWriteArrayList<>())
                .add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long companyId = extractCompanyId(session);
        List<WebSocketSession> sessions = sessionsByCompany.get(companyId);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    public void broadcast(Long companyId, String payload) {
        List<WebSocketSession> sessions = sessionsByCompany.get(companyId);
        if (sessions == null) return;

        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(payload));
                }
            } catch (Exception e) {
            }
        }
    }

    private Long extractCompanyId(WebSocketSession session) {
        String path = session.getUri().getPath();
        String[] parts = path.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}