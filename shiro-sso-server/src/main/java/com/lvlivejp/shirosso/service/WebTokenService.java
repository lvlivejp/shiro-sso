package com.lvlivejp.shirosso.service;

public interface WebTokenService {

    String generateToken(String sessionId);

    boolean checkToken(String token);
}
