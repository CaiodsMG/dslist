package com.devsuperior.dslist.controllers;

import com.devsuperior.dslist.dto.GameDTO;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.entities.Game;
import com.devsuperior.dslist.services.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Operation(description = "Busca todos os jogos")
    @ApiResponse(responseCode = "200", description = "Retorna todos os jogos")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GameMinDTO> findAll(){
        List<GameMinDTO> result = gameService.findAll();
        return result;
    }

    @Operation(description = "Busca um jogo por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o jogo pelo id"),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado")
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<GameDTO> findById(@PathVariable Long gameId){
        GameDTO result = gameService.findById(gameId);
        return ResponseEntity.ok(result);
    }

    @Operation(description = "Busca um jogo por titulo ou parte do titulo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o jogo pelo titulo"),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado")
    })
    @GetMapping("/search/{title}")
    public ResponseEntity<List<Game>> containingTitle(@PathVariable String title) {
        List<Game> gamesList = gameService.containingTitle(title);

        if (gamesList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(gamesList);
    }

    @Operation(description = "Busca jogos por score e acima do score.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna os jogos pelo score"),
            @ApiResponse(responseCode = "400", description = "Retorna que o score deve estar entre 0 e 5")
    })
    @GetMapping("/score/{score}")
    public ResponseEntity<List<Game>> scoreGreaterThan(@RequestParam Double score){

        List<Game> gameList = gameService.scoreGreaterOrEqual(score);

        return ResponseEntity.status(HttpStatus.OK).body(gameList);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Cria um novo jogo")
    @ApiResponse(responseCode = "201", description = "Jogo criado com sucesso.")
    public GameMinDTO create(
            @RequestBody Game game){
        return gameService.create(game);
    }

    @Operation(description = "Atualiza um jogo pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Jogo atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado.")
    })
    @PutMapping("/{gameId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GameMinDTO update(@PathVariable Long gameId,
                             @RequestBody Game game){

        return gameService.update(gameId, game);
    }

    @Operation(description = "Exclui um jogo passando o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Jogo excluido."),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado."),
            @ApiResponse(responseCode = "409", description = "Não pode excluir um jogo relacionado a outros registro.")
    })
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> delete(@PathVariable Long gameId){
        gameService.delete(gameId);
        return ResponseEntity.noContent().build();
    }

}
