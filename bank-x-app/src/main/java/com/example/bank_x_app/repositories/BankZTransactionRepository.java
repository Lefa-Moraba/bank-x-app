package com.example.bank_x_app.repositories;

import com.example.bank_x_app.entities.BankZTransactionEntity;
import com.example.bank_x_app.enums.ReconciliationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankZTransactionRepository extends JpaRepository<BankZTransactionEntity, Long> {

    List<BankZTransactionEntity> findByExternalReferenceIn(List<String> externalReferences);

    List<BankZTransactionEntity> findByReconciliationStatus(ReconciliationStatus reconciliationStatus);

}

