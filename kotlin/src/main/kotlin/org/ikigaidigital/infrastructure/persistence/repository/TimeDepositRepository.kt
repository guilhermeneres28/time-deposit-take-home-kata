package org.ikigaidigital.infrastructure.persistence.repository

import org.ikigaidigital.infrastructure.persistence.entity.TimeDepositEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TimeDepositRepository : JpaRepository<TimeDepositEntity, Long>
