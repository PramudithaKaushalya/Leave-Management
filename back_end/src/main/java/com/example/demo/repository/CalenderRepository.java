package com.example.demo.repository;

import com.example.demo.model.Calender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalenderRepository extends JpaRepository<Calender, Long> {

    List<Calender> findAllByOrderByDate();
}