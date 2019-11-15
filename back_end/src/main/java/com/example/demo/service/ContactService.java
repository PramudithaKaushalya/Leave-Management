package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Contact;
import com.example.demo.model.Employee;
import com.example.demo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getAll() {
        System.out.println("Get All Contacts !!!");
        return contactRepository.findAll();
    }

    public List<Contact> getOne(Employee employee) {
        System.out.println("Get contacts of one employee !!!");
        return contactRepository.findByEmployee(employee);
    }

    public String addContact(Contact contact) {
        contactRepository.save(contact);
        return "Contact is added !!!";
    }

    public String deleteContact(Integer id) {
        contactRepository.deleteById(id);
        return "Contact is Deleted!!!";
    }
}