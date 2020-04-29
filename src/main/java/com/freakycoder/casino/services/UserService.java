package com.freakycoder.casino.services;

import com.freakycoder.casino.daos.UserDAO;
import com.freakycoder.casino.models.BettableGameDetails;
import com.freakycoder.casino.models.GameBetDetail;
import com.freakycoder.casino.models.UserDetails;
import com.google.common.collect.ImmutableMap;
import javaslang.control.Either;
import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GameService gameService;

    @Transactional
    public UserDetails registerUser(UserDetails userDetails) {
        return Try.success(userDetails)
                .map(userDAO::registerUser)
                .get();
    }

    public Map<String, String> enterCasino(Long casinoId, Long userId) {
        return Try.of(() -> {
            userDAO.updateUserCurrentCasino(userId, casinoId);
            return ImmutableMap.of("status", "success");
        }).get();
    }

    public Map<String, String> rechargeUserBalanceAmount(Long userId, Double rechargeAmount) {
        return Try.of(() -> {
            userDAO.updateUserBalanceAmount(userId, rechargeAmount);
            return ImmutableMap.of("status", "success");
        }).get();
    }

    public Either<Throwable, List<BettableGameDetails>> fetchBettableGames(Long userId) {
        return Try.success(userId)
                .map(userDAO::fetchBettableGames)
                .toEither();
    }

    public Map<String, String> userCashOut(Long userId) {
        return Try.of(() -> {
            if (userDAO.fetchInProgressBetCountOfAnUser(userId) > 0) {
                throw new RuntimeException("You have pending bets to be completed, so you can't make cash out now, try again later");
            } else {
                userDAO.updateUserCashOut(userId);
                return ImmutableMap.of("status", "success");
            }
        }).get();
    }

    @Transactional
    public GameBetDetail betOnGame(Long userId, GameBetDetail gameBetDetail) {
        return Try.of(() -> {
            if (!gameService.isGameOpenForBet(gameBetDetail.getGameId())) {
                throw new RuntimeException("Selected Game is already closed, so can't able to make the bet");
            } else if (userDAO.fetchBalanceAmountOfUser(userId) < gameBetDetail.getBetAmount()) {
                throw new RuntimeException("Your bet amount is greater than the balance amount so can't able to make the bet");
            } else {
                return userDAO.saveBetDetails(userId, gameBetDetail);
            }
        }).get();
    }

}
