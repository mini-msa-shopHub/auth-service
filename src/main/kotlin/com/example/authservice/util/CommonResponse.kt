package com.example.authservice.util

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CommonResponse<T : Any>(
    val result: T,
) {

    companion object {
        val EMPTY = CommonResponse(EmptyDto())
    }

}
