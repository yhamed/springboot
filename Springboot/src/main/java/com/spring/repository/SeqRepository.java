package com.spring.repository;

import com.spring.domain.CustomSequences;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeqRepository extends CrudRepository<CustomSequences, String> {
}
