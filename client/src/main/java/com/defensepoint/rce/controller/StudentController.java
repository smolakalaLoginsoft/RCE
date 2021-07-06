package com.defensepoint.rce.controller;

import com.defensepoint.rce.entity.Department;
import com.defensepoint.rce.entity.Student;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@RestController
public class StudentController {

    @GetMapping("/student/safe-input/safe-test")
    public String studentUnsafe() throws IOException {
        Student student = new Student("bob", new Department("engineering"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(student);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/student/safe-input/safe-test", entity, String.class);

        return response.getBody();
    }
}
