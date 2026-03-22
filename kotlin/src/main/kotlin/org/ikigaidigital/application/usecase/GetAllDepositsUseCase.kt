package org.ikigaidigital.application.usecase

import org.ikigaidigital.domain.model.TimeDeposit
import org.ikigaidigital.domain.port.TimeDepositPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetAllDepositsUseCase(
    private val timeDepositPort: TimeDepositPort
) {
    
    @Transactional(readOnly = true)
    fun execute(): List<TimeDeposit> {
        return timeDepositPort.findAll()
    }
}
