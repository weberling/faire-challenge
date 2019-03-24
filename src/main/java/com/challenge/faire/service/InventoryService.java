package com.challenge.faire.service;

import com.challenge.faire.gateway.FaireGateway;
import com.challenge.faire.model.InventoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by weberling on 23/03/19.
 */
@Service
public class InventoryService {

    @Autowired
    private FaireGateway faireGateway;

    public void update(InventoryRequest inventoryRequest){
        faireGateway.updateInventory(inventoryRequest);
    }
}
