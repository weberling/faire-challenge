package com.challenge.faire.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by weberling on 23/03/19.
 */
@Data
@AllArgsConstructor
public class InventoryRequest
{
    private List<Inventory> inventories;
}
