package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QAuthDto_ForResponse is a Querydsl Projection type for ForResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAuthDto_ForResponse extends ConstructorExpression<AuthDto.ForResponse> {

    private static final long serialVersionUID = -1194615189L;

    public QAuthDto_ForResponse(com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> username) {
        super(AuthDto.ForResponse.class, new Class<?>[]{long.class, String.class}, userId, username);
    }

}

