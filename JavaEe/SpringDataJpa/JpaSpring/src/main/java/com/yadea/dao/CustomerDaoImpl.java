package com.yadea.dao;

import com.yadea.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl implements CustomerDao {
    public <S extends Customer> S save(S s) {
        return null;
    }

    public <S extends Customer> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    public Optional<Customer> findById(Long aLong) {
        return Optional.empty();
    }

    public boolean existsById(Long aLong) {
        return false;
    }

    public List<Customer> findAll() {
        return null;
    }

    public Iterable<Customer> findAllById(Iterable<Long> iterable) {
        return null;
    }

    public long count() {
        return 0;
    }

    public void deleteById(Long aLong) {

    }

    public void delete(Customer customer) {

    }

    public void deleteAll(Iterable<? extends Customer> iterable) {

    }

    public void deleteAll() {

    }

    public List<Customer> findAll(Sort sort) {
        return null;
    }

    public Page<Customer> findAll(Pageable pageable) {
        return null;
    }

    public List<Customer> findAll(Iterable<Long> iterable) {
        return null;
    }

    public <S extends Customer> List<S> save(Iterable<S> iterable) {
        return null;
    }

    public void flush() {

    }

    public <S extends Customer> S saveAndFlush(S s) {
        return null;
    }

    public void deleteInBatch(Iterable<Customer> iterable) {

    }

    public void deleteAllInBatch() {

    }

    public Customer getOne(Long aLong) {
        return null;
    }

    public Customer findOne(Specification<Customer> specification) {
        return null;
    }

    public List<Customer> findAll(Specification<Customer> specification) {
        return null;
    }

    public Page<Customer> findAll(Specification<Customer> specification, Pageable pageable) {
        return null;
    }

    public List<Customer> findAll(Specification<Customer> specification, Sort sort) {
        return null;
    }

    public long count(Specification<Customer> specification) {
        return 0;
    }
}
