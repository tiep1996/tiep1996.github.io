package com.iist.qlda.project.config.security;

public class JWTConstants {
    public static final String SECRET = "IIST";
    public static final long EXPIRATION_TIME = 1000*60*1*60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
