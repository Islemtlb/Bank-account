package com.exalt.banking.account.infrastructure.db.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exalt.banking.account.infrastructure.db.jpa.entity.BankAccountEntity;

@Repository
public interface JpaAccountRepository extends JpaRepository<BankAccountEntity, Long> {

}
