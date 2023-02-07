package com.example.reggie.uitl;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;
@Component
public class UuidUitl {
    private static Random random = new Random();

    public static long uniqId(){
        String nanoRandom = System.nanoTime() + "" + random.nextInt(9999);
        int hash = Math.abs(UUID.randomUUID().hashCode());
        int needAdd = 19 - String.valueOf(hash).length() + 1;
        return Long.valueOf(hash + "" + nanoRandom.substring(needAdd));
    }

}
