package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;

import java.util.List;

public interface TransferDao {

    int updateTransfer(TransferDto transferDto);
    List<Transfer> getTransfers();

    Transfer getTransferById(int transfer_id);

    Transfer getTransfersByUser(int user_id);

    // TODO: Add method to get transfers by type AND user
    // getTransfersByTypeAndUserId
}