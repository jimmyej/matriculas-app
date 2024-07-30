package com.courses.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class SecurityUtil {

    public static String decodeUTF8(String encodeString) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(encodeString, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decoded;
    }

}
