package com.freakycoder.casino.services;

import com.freakycoder.casino.daos.CasinoDAO;
import com.freakycoder.casino.daos.GameDAO;
import com.freakycoder.casino.daos.UserDAO;
import com.freakycoder.casino.models.GameBetDetail;
import com.freakycoder.casino.models.GameDetails;
import com.google.common.collect.ImmutableMap;
import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GameService {

    @Autowired
    private GameDAO gameDAO;

    @Autowired
    private CasinoDAO casinoDAO;

    @Autowired
    private UserDAO userDAO;

    @Transactional
    public GameDetails startGame(GameDetails gameDetails) {
        return Try.success(gameDetails).
                map(gameDAO::insertGameDetails)
                .get();
    }

    public Map<String, String> stopGame(Long gameId) {
        return Try.of(() -> {
            gameDAO.stopGame(gameId);
            return ImmutableMap.of("status", "success");
        }).get();
    }

    @Transactional
    public Map<String, String> throwBall(Long gameId) {
        return Try.of(() -> {
            Integer thrownNumber = this.fetchARandomNumber();
            gameDAO.updateThrownNumberToGame(gameId, thrownNumber);
            this.updateCasinoBalanceAmountDetails(gameId, thrownNumber);
            this.updateUserBalanceAmountDetails(gameId, thrownNumber);
            this.updateGameBetStatus(gameId, thrownNumber);
            return ImmutableMap.of("status", "success");
        }).get();
    }

    private void updateCasinoBalanceAmountDetails(Long gameId, Integer thrownNumber) {
        Long casinoId = gameDAO.fetchCasinoIdUsingGameId(gameId);
        List<GameBetDetail> betDetails = gameDAO.fetchGameBetDetails(gameId);
        Double winnersAmount = betDetails.stream().filter(bet -> bet.getBetNumber().equals(thrownNumber)).mapToDouble(GameBetDetail::getBetAmount).sum();
        Double loosersAmount = betDetails.stream().filter(bet -> !bet.getBetNumber().equals(thrownNumber)).mapToDouble(GameBetDetail::getBetAmount).sum();
        Double adjustAmount = loosersAmount - winnersAmount;
        casinoDAO.updateCasinoBalanceAmount(casinoId, adjustAmount);
    }

    private void updateUserBalanceAmountDetails(Long gameId, Integer thrownNumber) {
        List<GameBetDetail> betDetails = gameDAO.fetchGameBetDetails(gameId);
        betDetails.forEach(bet -> {
            Double adjustAmount = bet.getBetNumber().equals(thrownNumber) ? bet.getBetAmount() : -bet.getBetAmount();
            userDAO.updateUserBalanceAmount(bet.getUserId(), adjustAmount);
        });
    }

    private void updateGameBetStatus(Long gameId, Integer thrownNumber) {
        List<GameBetDetail> betDetails = gameDAO.fetchGameBetDetails(gameId);
        Set<Long> winBetIds = betDetails.stream().filter(bet -> bet.getBetNumber().equals(thrownNumber)).map(GameBetDetail::getBetId).collect(Collectors.toSet());
        Set<Long> looseBetIds = betDetails.stream().filter(bet -> !bet.getBetNumber().equals(thrownNumber)).map(GameBetDetail::getBetId).collect(Collectors.toSet());
        gameDAO.updateBetStats(winBetIds, "WON");
        gameDAO.updateBetStats(looseBetIds, "LOSS");
    }

    public Boolean isGameOpenForBet(Long gameId) {
        return Try.success(gameId)
                .map(gameDAO::isGameOpeningForBet)
                .get();
    }

    private Integer fetchARandomNumber() {
        return new SecureRandom().nextInt(36) + 1;
    }

}
