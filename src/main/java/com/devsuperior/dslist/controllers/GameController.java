package com.devsuperior.dslist.controllers;

import com.devsuperior.dslist.dto.GameDTO;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.entities.Game;
import com.devsuperior.dslist.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GameMinDTO> findAll(){
        List<GameMinDTO> result = gameService.findAll();
        return result;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameDTO> findById(@PathVariable Long gameId){
        GameDTO result = gameService.findById(gameId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<Game>> containingTitle(@PathVariable String title) {
        List<Game> gamesList = gameService.containingTitle(title);

        if (gamesList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(gamesList);
    }

    @GetMapping("/score/{score}")
    public ResponseEntity<List<Game>> scoreGreaterThan(@RequestParam Double score){

        List<Game> gameList = gameService.scoreGreaterOrEqual(score);

        return ResponseEntity.status(HttpStatus.OK).body(gameList);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameMinDTO create(@RequestBody Game game){
        return gameService.create(game);
    }

    @PutMapping("/{gameId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GameMinDTO update(@PathVariable Long gameId,
                             @RequestBody Game game){

        return gameService.update(gameId, game);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> delete(@PathVariable Long gameId){
        gameService.delete(gameId);
        return ResponseEntity.noContent().build();
    }

}
