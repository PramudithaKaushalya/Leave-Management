package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.demo.model.Contact;
import com.example.demo.model.Employee;

public interface ContactRepository extends JpaRepository<Contact, Integer>{

	List<Contact> findByEmployee(Employee employee);
}