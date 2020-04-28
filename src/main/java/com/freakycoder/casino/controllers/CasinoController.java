package com.freakycoder.casino.controllers;

import com.freakycoder.casino.models.CasinoDetails;
import com.freakycoder.casino.models.DealerDetails;
import com.freakycoder.casino.services.CasinoService;
import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/casino")
public class CasinoController extends BaseController {

    @Autowired
    private CasinoService casinoService;

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity registerCasino(@RequestBody CasinoDetails casinoDetails) {
        return Try.success(casinoDetails)
                .map(casinoService::registerCasino)
                .toEither()
                .fold(this::error, this::success);
    }

    @PostMapping(value = "/dealer")
    @ResponseBody
    public ResponseEntity registerDealer(@RequestBody DealerDetails dealerDetails) {
        return Try.success(dealerDetails)
                .map(casinoService::registerDealer)
                .toEither()
                .fold(this::error, this::success);
    }

    @GetMapping(value = "/{casinoId}/dealer")
    @ResponseBody
    public ResponseEntity fetchDealers(@PathVariable("casinoId") Long casinoId) {
        return casinoService.fetchDealers(casinoId)
                .fold(this::error, this::success);
    }

    @PutMapping(value = "/{casinoId}/recharge")
    @ResponseBody
    public ResponseEntity rechargeCasinoBalanceAmount(@PathVariable("casinoId") Long casinoId, @RequestParam("amount") Double rechargeAmount) {
        return Try.of(() -> casinoService.rechargeCasinoBalanceAmount(casinoId, rechargeAmount))
                .toEither()
                .fold(this::error, this::success);
    }


}
