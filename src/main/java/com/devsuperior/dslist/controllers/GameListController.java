package com.devsuperior.dslist.controllers;

import com.devsuperior.dslist.dto.GameListDTO;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.dto.ReplacementDTO;
import com.devsuperior.dslist.entities.GameList;
import com.devsuperior.dslist.services.GameListService;
import com.devsuperior.dslist.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lists")
public class GameListController {

    @Autowired
    private GameListService gameListService;

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<GameListDTO> findAll(){

        return gameListService.findAll();
    }

    @GetMapping("/{gameListId}")
    public GameListDTO findById(@PathVariable Long gameListId){
        return gameListService.findById(gameListId);
    }

    @GetMapping("/orderDesc")
    public List<GameListDTO> findAllDesc(){
        return gameListService.findAllDesc();
    }


    @GetMapping("/{listId}/games")
    public List<GameMinDTO> findByList(@PathVariable Long listId){
        List<GameMinDTO> result = gameService.findByList(listId);
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameListDTO create(@RequestBody GameList gameList){
        return gameListService.create(gameList);
    }

    @PutMapping("/{gameListId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GameListDTO update(@PathVariable Long gameListId,
                              @RequestBody GameList gameList){

        return gameListService.update(gameListId, gameList);

    }

    @DeleteMapping("/{gameListId}")
    public ResponseEntity<Void> delete(@PathVariable Long gameListId) {
        gameListService.delete(gameListId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{listId}/replacement")
    public void move(@PathVariable Long listId, @RequestBody ReplacementDTO corpo){
        gameListService.move(listId, corpo.getPosicaoOrigem(), corpo.getPosicaoDestino());
    }

}
