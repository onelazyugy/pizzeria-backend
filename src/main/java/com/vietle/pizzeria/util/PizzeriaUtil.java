package com.vietle.pizzeria.util;

import com.google.common.hash.Hashing;
import com.vietle.pizzeria.constant.Constant;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PizzeriaUtil {
    private PizzeriaUtil() {}

    public static String getTimestamp() {
        return new Timestamp(new Date().getTime()).toString();
    }

    public static String hash(String string) {
        String sha256hexString = Hashing.sha256()
                .hashString(string, StandardCharsets.UTF_8)
                .toString();
        return sha256hexString;
    }
}
