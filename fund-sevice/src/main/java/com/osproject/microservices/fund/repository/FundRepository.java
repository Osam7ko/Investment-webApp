package com.osproject.microservices.fund.repository;

import com.osproject.microservices.fund.entity.FundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepository extends JpaRepository<FundEntity, Integer> {

}
