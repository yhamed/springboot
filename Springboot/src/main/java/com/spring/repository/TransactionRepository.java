package com.spring.repository;
import com.spring.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
