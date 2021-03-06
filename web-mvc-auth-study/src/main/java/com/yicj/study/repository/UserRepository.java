package com.yicj.study.repository;

import com.yicj.study.model.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository <User, Integer>, JpaSpecificationExecutor<User> {

    User findByUsername(String username) ;
}
