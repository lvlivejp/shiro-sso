package com.lvlivejp.shirosso.core.shiro;

import com.lvlivejp.shirosso.core.utils.JedisUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisSessionDao extends AbstractSessionDAO {

    @Autowired
    private JedisUtils jedisUtils;

    private final String SHIRO_SESSION_PREFIX = "shiro_session_id:";

    @Value("${session.timeout}")
    private int SESSION_TIMEOUT;

    private byte[] getRedisKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }

    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getRedisKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtils.setex(key, value, SESSION_TIMEOUT);
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        System.out.println("doReadSession:" + sessionId);
        byte[] key = getRedisKey(sessionId.toString());
        byte[] value = jedisUtils.get(key);
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        byte[] key = getRedisKey(session.getId().toString());
        jedisUtils.del(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtils.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        if (keys.isEmpty()) {
            return null;
        }
        for (byte[] key : keys) {
            sessions.add((Session) SerializationUtils.deserialize(jedisUtils.get(key)));
        }
        return sessions;
    }
}
