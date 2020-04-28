package com.freakycoder.casino.daos;

import com.freakycoder.casino.models.BettableGameDetails;
import com.freakycoder.casino.models.GameBetDetail;
import com.freakycoder.casino.models.UserDetails;
import com.freakycoder.casino.utils.QueryParser;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDAO {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private QueryParser queries;

    public UserDetails registerUser(UserDetails userDetails) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user_details")
                .usingGeneratedKeyColumns("user_id")
                .usingColumns("user_name", "status");

        Map<String, Object> param = new HashMap<>();
        param.put("user_name", userDetails.getUserName());
        param.put("status", userDetails.getStatus());
        Long userId = jdbcInsert.executeAndReturnKey(param).longValue();
        userDetails.setUserId(userId);
        return userDetails;
    }

    public void updateUserCurrentCasino(Long userId, Long casinoId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.update(queries.forId("api.update_user_current_casino_query"), ImmutableMap.of("casinoId", casinoId, "userId", userId));
    }

    public void updateUserBalanceAmount(Long userId, Double amount) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.update(queries.forId("api.update_user_balance_amount_query"), ImmutableMap.of("amount", amount, "userId", userId));
    }

    public List<BettableGameDetails> fetchBettableGames(Long userId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return jdbcTemplate.query(queries.forId("api.fetch_list_of_bettable_game_query"), ImmutableMap.of("userId", userId), this::mapToBettableGameDetails);
    }

    private BettableGameDetails mapToBettableGameDetails(ResultSet rs, int __) throws SQLException {
        BettableGameDetails bettableGameDetails = new BettableGameDetails();
        bettableGameDetails.setGameDealerId(rs.getLong("game_id"));
        bettableGameDetails.setGameStartTime(rs.getTimestamp("start_time"));
        bettableGameDetails.setGameDealerId(rs.getLong("dealer_id"));
        bettableGameDetails.setGameDealerName(rs.getString("dealer_name"));
        return bettableGameDetails;
    }

    public void updateUserCashOut(Long userId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.update(queries.forId("api.update_user_cashout_query"), ImmutableMap.of("userId", userId));
    }

    public GameBetDetail saveBetDetails(Long userId, GameBetDetail gameBetDetail) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("bet_details")
                .usingGeneratedKeyColumns("bet_id")
                .usingColumns("bet_amount", "bet_number", "betting_time", "game_id", "user_id");

        Map<String, Object> param = new HashMap<>();
        param.put("bet_amount", gameBetDetail.getBetAmount());
        param.put("bet_number", gameBetDetail.getBetNumber());
        param.put("user_id", userId);
        param.put("betting_time", gameBetDetail.getBetTime());
        param.put("game_id", gameBetDetail.getGameId());
        Long betId = jdbcInsert.executeAndReturnKey(param).longValue();
        gameBetDetail.setBetId(betId);
        return gameBetDetail;
    }

    public Double fetchBalanceAmountOfUser(Long userId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(queries.forId("fetch_balance_of_user_query"), ImmutableMap.of("userId", userId), Double.class);
    }

}
