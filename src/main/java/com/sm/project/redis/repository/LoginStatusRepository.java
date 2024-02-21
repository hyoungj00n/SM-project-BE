package com.sm.project.redis.repository;


import com.sm.project.redis.domain.LoginStatus;
import org.springframework.data.repository.CrudRepository;

public interface LoginStatusRepository extends CrudRepository<LoginStatus, String> {
}
