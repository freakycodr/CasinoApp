package com.freakycoder.casino.services;

import com.freakycoder.casino.daos.CasinoDAO;
import com.freakycoder.casino.models.CasinoDetails;
import com.freakycoder.casino.models.DealerDetails;
import com.google.common.collect.ImmutableMap;
import javaslang.control.Either;
import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class CasinoService {

    @Autowired
    private CasinoDAO casinoDAO;

    @Transactional
    public CasinoDetails registerCasino(CasinoDetails casinoDetails) {
        return Try.success(casinoDetails)
                .map(casinoDAO::registerCasino)
                .get();
    }

    @Transactional
    public DealerDetails registerDealer(DealerDetails dealerDetails) {
        return Try.success(dealerDetails)
                .map(casinoDAO::registerDealer)
                .get();
    }

    public Either<Throwable, List<DealerDetails>> fetchDealers(Long casinoId) {
        return Try.success(casinoId)
                .map(casinoDAO::fetchDealers)
                .toEither();
    }

    public Map<String, String> rechargeCasinoBalanceAmount(Long casinoId, Double rechargeAmount) {
        return Try.of(() -> {
            casinoDAO.updateCasinoBalanceAmount(casinoId, rechargeAmount);
            return ImmutableMap.of("status", "success");
        }).get();
    }
}
