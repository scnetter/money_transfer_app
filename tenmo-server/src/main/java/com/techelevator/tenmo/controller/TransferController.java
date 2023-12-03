package com.techelevator.tenmo.controller;
import javax.validation.Valid;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

    private TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    // TODO: Add auth to hasRole('ADMIN') to restrict full list of transfers to admin only
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfers() {
        List<Transfer> transfers = transferDao.getTransfers();
        return transfers;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public void addTransfer(@RequestBody TransferDto transferDto) {

        Transfer newTransfer = null;
        newTransfer = transferDao.addTransfer(transferDto);
    }

//    @RequestMapping(path="/accounts", method = RequestMethod.GET)
//    public Account getAccountByUserId(@RequestParam int user_id) {
//        Account account = accountDao.getAccountByUserId(user_id);
//        return account;
//    }

}