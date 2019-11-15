package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.Contact;
import com.example.demo.model.Employee;
import com.example.demo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/all")
    public List<Contact> getAll() {
        return contactService.getAll();
    }

    @PostMapping(value = "/add")
    public String addEmployee(@RequestBody Contact contact) {
        return contactService.addContact(contact);
    }

    @PostMapping(value = "/employee")
    public List<Contact> getOne(@RequestBody Employee employee) {
        return contactService.getOne(employee);
    }

    @GetMapping("/delete/{id}")
    public String DeleteById(@PathVariable("id") Contact contact) {
        Integer contact_id = contact.getContact_id();
        return contactService.deleteContact(contact_id);
    }
}