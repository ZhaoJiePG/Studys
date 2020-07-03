package com.zj.dao;

import com.zj.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

/**
 * 符合SpringDataJpa的dao层接口规范
 *      JpaRepository<操作的实体类类型，实体类中主键属性的类型>
 *      JpaSpecificationExecutor<操作实体类类型>
 */
@Component
public interface CustomerDao extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
}
