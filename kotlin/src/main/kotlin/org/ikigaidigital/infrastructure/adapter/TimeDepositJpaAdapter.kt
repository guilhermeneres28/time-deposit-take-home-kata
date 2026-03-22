package org.ikigaidigital.infrastructure.adapter

import org.ikigaidigital.domain.model.TimeDeposit
import org.ikigaidigital.domain.port.TimeDepositPort
import org.ikigaidigital.infrastructure.persistence.entity.TimeDepositEntity
import org.ikigaidigital.infrastructure.persistence.repository.TimeDepositRepository
import org.springframework.stereotype.Component

@Component
class TimeDepositJpaAdapter(
    private val repository: TimeDepositRepository
) : TimeDepositPort {
    
    override fun findAll(): List<TimeDeposit> {
        return repository.findAll().map { it.toDomain() }
    }
    
    override fun saveAll(deposits: List<TimeDeposit>): List<TimeDeposit> {
        val entities = deposits.map { TimeDepositEntity.fromDomain(it) }
        return repository.saveAll(entities).map { it.toDomain() }
    }
}
