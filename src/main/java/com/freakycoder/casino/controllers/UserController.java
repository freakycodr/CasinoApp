package com.freakycoder.casino.controllers;

import com.freakycoder.casino.models.GameBetDetail;
import com.freakycoder.casino.models.UserDetails;
import com.freakycoder.casino.services.UserService;
import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity registerUser(@RequestBody UserDetails userDetails) {
        return Try.success(userDetails)
                .map(userService::registerUser)
                .toEither()
                .fold(this::error, this::success);
    }

    @PutMapping(value = "/{userId}/enter-casino")
    @ResponseBody
    public ResponseEntity enterCasino(@PathVariable("userId") Long userId, @RequestParam("casinoId") Long casinoId) {
        return Try.of(() -> userService.enterCasino(casinoId, userId))
                .toEither()
                .fold(this::error, this::success);
    }

    @PutMapping(value = "/{userId}/recharge")
    @ResponseBody
    public ResponseEntity rechargeUserBalanceAmount(@PathVariable("userId") Long userId, @RequestParam("amount") Double rechargeAmount) {
        return Try.of(() -> userService.rechargeUserBalanceAmount(userId, rechargeAmount))
                .toEither()
                .fold(this::error, this::success);
    }

    @GetMapping(value = "/{userId}/games")
    @ResponseBody
    public ResponseEntity fetchBettableGames(@PathVariable("userId") Long userId) {
        return userService.fetchBettableGames(userId)
                .fold(this::error, this::success);
    }

    @PutMapping(value = "/{userId}/cashout")
    @ResponseBody
    public ResponseEntity userCashOut(@PathVariable("userId") Long userId) {
        return Try.of(() -> userService.userCashOut(userId))
                .toEither()
                .fold(this::error, this::success);
    }

    @PostMapping(value = "/{userId}/bet-game")
    @ResponseBody
    public ResponseEntity betGame(@PathVariable("userId") Long userId, GameBetDetail gameBetDetail) {
        return Try.of(() -> userService.betOnGame(userId, gameBetDetail))
                .toEither()
                .fold(this::error, this::success);
    }

}
