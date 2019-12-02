package com.ealen.dao;

import com.ealen.Entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Ma on 2019/12/1 21：22
 */
public interface JobDao extends JpaRepository<JobEntity, Integer> {
    List<JobEntity> findByStatus(Integer status);
}
