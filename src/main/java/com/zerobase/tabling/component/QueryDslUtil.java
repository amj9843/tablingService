package com.zerobase.tabling.component;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QueryDslUtil {
    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);

        return new OrderSpecifier(order, fieldPath);
    }

    public List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable, Path<?> parent) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            pageable.getSort().stream().forEach(order -> {
                Order direction = (order.getDirection().isAscending()) ? Order.ASC : Order.DESC;
                orders.add(getSortedColumn(direction, parent, order.getProperty()));
            });
        }

        return orders;
    }
}
