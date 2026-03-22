package org.ikigaidigital.application.usecase

import org.ikigaidigital.domain.port.TimeDepositPort
import org.ikigaidigital.domain.service.TimeDepositService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateAllBalancesUseCase(
    private val timeDepositPort: TimeDepositPort,
    private val timeDepositService: TimeDepositService
) {
    
    @Transactional
    fun execute() {
        val deposits = timeDepositPort.findAll()
        
        val updatedDeposits = deposits.mapNotNull { deposit ->
            val result = timeDepositService.updateBalance(deposit)
            result.getOrNull()
        }
        
        timeDepositPort.saveAll(updatedDeposits)
    }
}
