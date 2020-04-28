package com.freakycoder.casino.daos;

import com.freakycoder.casino.models.CasinoDetails;
import com.freakycoder.casino.models.DealerDetails;
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
public class CasinoDAO {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private QueryParser queries;


    public CasinoDetails registerCasino(CasinoDetails casinoDetails) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("casino_details")
                .usingGeneratedKeyColumns("casino_id")
                .usingColumns("casino_name", "status");

        Map<String, Object> param = new HashMap<>();
        param.put("casino_name", casinoDetails.getCasinoName());
        param.put("status", casinoDetails.getStatus());
        Long casinoId = jdbcInsert.executeAndReturnKey(param).longValue();
        casinoDetails.setCasinoId(casinoId);
        return casinoDetails;
    }


    public DealerDetails registerDealer(DealerDetails dealerDetails) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("dealer_details")
                .usingGeneratedKeyColumns("dealer_id")
                .usingColumns("dealer_name", "casino_id", "status");

        Map<String, Object> param = new HashMap<>();
        param.put("dealer_name", dealerDetails.getDealerName());
        param.put("casino_id", dealerDetails.getCasinoId());
        param.put("status", dealerDetails.getStatus());
        Long dealerId = jdbcInsert.executeAndReturnKey(param).longValue();
        dealerDetails.setDealerId(dealerId);
        return dealerDetails;
    }

    public List<DealerDetails> fetchDealers(Long casinoId) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return jdbcTemplate.query(queries.forId("api.fetch_casino_dealers_query"), ImmutableMap.of("casinoId", casinoId), this::mapToDealerDetails);
    }

    private DealerDetails mapToDealerDetails(ResultSet rs, int __) throws SQLException {
        DealerDetails dealerDetails = new DealerDetails();
        dealerDetails.setDealerId(rs.getLong("dealer_id"));
        dealerDetails.setDealerName(rs.getString("dealer_name"));
        dealerDetails.setCasinoId(rs.getLong("casino_id"));
        dealerDetails.setStatus(rs.getBoolean("status"));
        return dealerDetails;
    }

    public void updateCasinoBalanceAmount(Long casinoId, Double amount) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.update(queries.forId("api.update_casino_balance_amount_query"),ImmutableMap.of("amount",amount,"casinoId",casinoId));
    }
}
