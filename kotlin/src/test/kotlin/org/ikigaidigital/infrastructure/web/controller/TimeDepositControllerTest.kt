package org.ikigaidigital.infrastructure.web.controller

import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.ikigaidigital.application.usecase.GetAllDepositsUseCase
import org.ikigaidigital.application.usecase.UpdateAllBalancesUseCase
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.model.TimeDeposit
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TimeDepositController::class)
@DisplayName("TimeDepositController Web Layer Tests")
class TimeDepositControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var updateAllBalancesUseCase: UpdateAllBalancesUseCase

    @MockitoBean
    private lateinit var getAllDepositsUseCase: GetAllDepositsUseCase

    @Test
    fun `PUT update-balances should return 204 No Content`() {
        mockMvc.perform(
            put("/api/v1/time-deposits/update-balances")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)

        verify(updateAllBalancesUseCase).execute()
    }

    @Test
    fun `GET time-deposits should return list of deposits`() {
        val deposit1 = TimeDeposit(1L, PlanType.BASIC, Money.of(1000.0), 31)
        val deposit2 = TimeDeposit(2L, PlanType.STUDENT, Money.of(2000.0), 61)
        given(getAllDepositsUseCase.execute()).willReturn(listOf(deposit1, deposit2))

        mockMvc.perform(
            get("/api/v1/time-deposits")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].id", equalTo(1)))
            .andExpect(jsonPath("$[0].planType", equalTo("BASIC")))
            .andExpect(jsonPath("$[0].balance", equalTo(1000.0)))
            .andExpect(jsonPath("$[0].days", equalTo(31)))
            .andExpect(jsonPath("$[1].id", equalTo(2)))
            .andExpect(jsonPath("$[1].planType", equalTo("STUDENT")))
            .andExpect(jsonPath("$[1].balance", equalTo(2000.0)))
            .andExpect(jsonPath("$[1].days", equalTo(61)))

        verify(getAllDepositsUseCase).execute()
    }

    @Test
    fun `GET time-deposits should return empty list`() {
        given(getAllDepositsUseCase.execute()).willReturn(emptyList())

        mockMvc.perform(
            get("/api/v1/time-deposits")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(0)))

        verify(getAllDepositsUseCase).execute()
    }
}
