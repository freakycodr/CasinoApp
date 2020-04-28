package com.freakycoder.casino.daos;

import com.freakycoder.casino.models.GameDetails;
import com.freakycoder.casino.models.GameBetDetail;
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
import java.util.Set;

@Component
public class GameDAO {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private QueryParser queries;


    public GameDetails insertGameDetails(GameDetails gameDetails) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("game_details")
                .usingGeneratedKeyColumns("game_id")
                .usingColumns("start_time", "status", "dealer_id");

        Map<String, Object> param = new HashMap<>();
        param.put("start_time", gameDetails.getStartTime());
        param.put("status", "OPEN");
        param.put("dealer_id", gameDetails.getDealerId());
        Long gameId = jdbcInsert.executeAndReturnKey(param).longValue();
        gameDetails.setGameId(gameId);
        return gameDetails;
    }

    public void stopGame(Long gameId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.update(queries.forId("api.stop_game_using_game_id_query"), ImmutableMap.of("gameId", gameId));
    }

    public void updateThrownNumberToGame(Long gameId, Integer thrownNumber) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.update(queries.forId("api.update_thrown_number_to_game_query"), ImmutableMap.of("thrownNumber", thrownNumber, "gameId", gameId));
    }

    public Boolean isGameOpeningForBet(Long gameId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        Long result = jdbcTemplate.queryForObject(queries.forId("check_game_open_for_bet_query"), ImmutableMap.of("gameId", gameId), Long.class);
        return result > 0;
    }

    public List<GameBetDetail> fetchGameBetDetails(Long gameId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return jdbcTemplate.query(queries.forId("fetch_list_of_bets_for_game_id_query"), ImmutableMap.of("gameId", gameId), this::mapToGameBetDetail);
    }

    private GameBetDetail mapToGameBetDetail(ResultSet rs, int __) throws SQLException {
        GameBetDetail gameBetDetail = new GameBetDetail();
        gameBetDetail.setBetId(rs.getLong("bet_id"));
        gameBetDetail.setBetAmount(rs.getDouble("bet_amount"));
        gameBetDetail.setBetNumber(rs.getInt("bet_number"));
        gameBetDetail.setUserId(rs.getLong("user_id"));
        return gameBetDetail;
    }

    public Long fetchCasinoIdUsingGameId(Long gameId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(queries.forId("fetch_casino_id_using_game_id_query"), ImmutableMap.of("gameId", gameId), Long.class);
    }

    public void updateBetStats(Set<Long> betIds, String status) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.update(queries.forId(""), ImmutableMap.of("status", status, "betIds", betIds));
    }

}
