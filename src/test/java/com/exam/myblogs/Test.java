package com.exam.myblogs;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@SpringBootTest
public class Test {
    public static void main(String[] args) {
        String password = "123456";
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        String md5 = DigestUtils.md5DigestAsHex(passwordBytes);
        System.out.println(md5);
    }
}
