package com.kh.it.example.onepay.repository;

import org.springframework.data.repository.CrudRepository;

import com.kh.it.example.onepay.model.Transaction;

//@RepositoryRestResource
public interface TransactionRepository extends CrudRepository<Transaction, Long>{
}
