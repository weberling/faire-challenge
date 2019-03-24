package com.challenge.faire.statistics;

import com.challenge.faire.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by weberling on 23/03/19.
 */
@Data
@AllArgsConstructor
public class OrderAmount {
    private Integer price;
    private Integer quantity;

    public static OrderAmount getAmmount(Order order) {
        return order.getItems().stream()
                .map(item -> new OrderAmount(item.getPriceCents()*item.getQuantity(), item.getQuantity()))
                .reduce((itemA, itemB) -> new OrderAmount(itemA.getPrice() + itemB.getPrice(),
                        itemA.getQuantity() + itemB.getQuantity())).get();
    }
}
