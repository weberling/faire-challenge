package com.challenge.faire.service;

import com.challenge.faire.gateway.FaireAdapter;
import com.challenge.faire.model.InventoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by weberling on 23/03/19.
 */
@Service
public class InventoryService {

    @Autowired
    private FaireAdapter faireAdapter;

    public void update(InventoryRequest inventoryRequest){
        faireAdapter.updateInventory(inventoryRequest);
    }
}
