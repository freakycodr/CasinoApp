package com.freakycoder.casino.controllers;

import com.freakycoder.casino.models.GameDetails;
import com.freakycoder.casino.services.GameService;
import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/v1/game")
public class GameController extends BaseController {

    @Autowired
    private GameService gameService;

    @PostMapping(value = "/start-game")
    public @ResponseBody
    ResponseEntity startGame(@RequestBody GameDetails gameDetails) {
        return Try.success(gameDetails)
                .map(gameService::startGame)
                .toEither()
                .fold(this::error, this::success);
    }

    @PutMapping(value = "/{gameId}/end-game")
    public @ResponseBody
    ResponseEntity stopGame(@PathVariable("gameId") Long gameId) {
        return Try.success(gameId)
                .map(gameService::stopGame)
                .toEither()
                .fold(this::error, this::success);
    }

    @GetMapping(value = "/{gameId}/throwBall")
    public @ResponseBody
    ResponseEntity throwBall(@PathVariable("gameId") Long gameId) {
        return Try.success(gameId)
                .map(gameService::throwBall)
                .toEither()
                .fold(this::error, this::success);
    }


}
