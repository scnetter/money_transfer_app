package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {


    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfers(){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    public Transfer addTransfer(TransferDto transferDto) {
        Transfer newTransfer = null;
        String sql = "";
        if(transferDto.getTransferTypeId() == 2){
            if(validateTransfer(transferDto)) {
                sql = "INSERT into transfer(transfer_id, transfer_type_id, account_from, account_to, amount, transfer_status_id)" +
                        "VALUES(DEFAULT, ?, ?, ?, ?, 2) RETURNING transfer_id;";
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Funds for Transfer.");
            }
        } else {
            sql = "INSERT into transfer(transfer_id, transfer_type_id, account_from, account_to, amount, transfer_status_id)" +
                    "VALUES(DEFAULT, 1, ?, ?, ?, 1) RETURNING transfer_id;";
        }

        Integer newTransferId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                transferDto.getTransferTypeId(),
                transferDto.getAccountFrom(),
                transferDto.getAccountTo(),
                transferDto.getTransferAmount());

        newTransfer = getTransferById(newTransferId);

        return newTransfer;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfer;
    }


    @Override
    public Transfer getTransfersByUser(int user_id) {
        Transfer transfer = null;


        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatus(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setTransferAmount(rs.getBigDecimal("amount"));
        return transfer;
    }

    private boolean validateTransfer(TransferDto transferDto) {
        String sql = "Select * from account where user_id = ?;";
        BigDecimal balance = new BigDecimal(0);
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferDto.getAccountFrom());
            if(results.next()){
                balance = results.getBigDecimal("balance");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        if(balance.compareTo(transferDto.getTransferAmount()) == -1){
            return false;
        }
        return true;
    }
}
