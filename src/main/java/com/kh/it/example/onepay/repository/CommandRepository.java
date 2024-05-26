package com.kh.it.example.onepay.repository;

import org.springframework.data.repository.CrudRepository;

import com.kh.it.example.onepay.model.Command;

public interface CommandRepository extends CrudRepository<Command, Long>{
}
