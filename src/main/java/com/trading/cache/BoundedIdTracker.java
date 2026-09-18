package com.trading.cache;
import java.util.LinkedHashMap;
import java.util.Map;


public class BoundedIdTracker {

    private final int maxSize;
    private final Map<Long, Boolean> ids;

    public BoundedIdTracker(int maxSize) {
        this.maxSize = maxSize;
        this.ids = new LinkedHashMap<>(16, 0.75f, false) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Boolean> eldest) {
                return size() > BoundedIdTracker.this.maxSize;
            }
        };
    }

    public synchronized boolean alreadyProcessed(Long id) {
        return ids.containsKey(id);
    }

    public synchronized void markProcessed(Long id) {
        ids.put(id, Boolean.TRUE);
    }

    public synchronized boolean remove(Long id) {
        return ids.remove(id) != null;
    }
}