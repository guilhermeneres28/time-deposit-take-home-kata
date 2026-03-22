package org.ikigaidigital.infrastructure.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.ikigaidigital.application.usecase.GetAllDepositsUseCase
import org.ikigaidigital.application.usecase.UpdateAllBalancesUseCase
import org.ikigaidigital.infrastructure.web.dto.TimeDepositResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/time-deposits")
@Tag(name = "Time Deposits", description = "Time Deposit Management API")
class TimeDepositController(
    private val updateAllBalancesUseCase: UpdateAllBalancesUseCase,
    private val getAllDepositsUseCase: GetAllDepositsUseCase
) {
    
    @PutMapping("/update-balances")
    @Operation(summary = "Update all time deposit balances", description = "Calculates and updates interest for all time deposits based on their plan type and days")
    fun updateBalances(): ResponseEntity<Unit> {
        updateAllBalancesUseCase.execute()
        return ResponseEntity.noContent().build()
    }
    
    @GetMapping
    @Operation(summary = "Get all time deposits", description = "Retrieves all time deposits with their current balances")
    fun getAllDeposits(): ResponseEntity<List<TimeDepositResponse>> {
        val deposits = getAllDepositsUseCase.execute()
        val response = deposits.map { TimeDepositResponse.fromDomain(it) }
        return ResponseEntity.ok(response)
    }
}
