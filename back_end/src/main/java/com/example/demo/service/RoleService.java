package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAll() {
        System.out.println("Get All Roles !!!");
        return roleRepository.findAll();
    }
}