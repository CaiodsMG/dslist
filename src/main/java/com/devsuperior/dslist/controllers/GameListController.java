package com.devsuperior.dslist.controllers;

import com.devsuperior.dslist.dto.GameListDTO;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.dto.ReplacementDTO;
import com.devsuperior.dslist.entities.GameList;
import com.devsuperior.dslist.services.GameListService;
import com.devsuperior.dslist.services.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(description = "Busca todas as listas de jogos")
    @ApiResponse(responseCode = "200", description = "Retorna todas as listas de jogos")
    @GetMapping
    public List<GameListDTO> findAll(){
        return gameListService.findAll();
    }

    @Operation(description = "Busca uma lista de jogos por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista pelo id"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    @GetMapping("/{gameListId}")
    public GameListDTO findById(@PathVariable Long gameListId){
        return gameListService.findById(gameListId);
    }

    @Operation(description = "Busca todas as listas de jogos em ordem decrescente")
    @ApiResponse(responseCode = "200", description = "Retorna todas as listas em ordem decrescente")
    @GetMapping("/orderDesc")
    public List<GameListDTO> findAllDesc(){
        return gameListService.findAllDesc();
    }

    @Operation(description = "Busca todos os jogos de uma lista específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna os jogos da lista"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    @GetMapping("/{listId}/games")
    public List<GameMinDTO> findByList(@PathVariable Long listId){
        List<GameMinDTO> result = gameService.findByList(listId);
        return result;
    }

    @Operation(description = "Cria uma nova lista de jogos")
    @ApiResponse(responseCode = "201", description = "Lista criada com sucesso")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameListDTO create(@RequestBody GameList gameList){
        return gameListService.create(gameList);
    }

    @Operation(description = "Atualiza uma lista de jogos pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Lista atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    @PutMapping("/{gameListId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GameListDTO update(@PathVariable Long gameListId,
                              @RequestBody GameList gameList){
        return gameListService.update(gameListId, gameList);
    }

    @Operation(description = "Exclui uma lista de jogos pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lista excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada"),
            @ApiResponse(responseCode = "409", description = "Não pode excluir uma lista relacionada a outros registros.")
    })
    @DeleteMapping("/{gameListId}")
    public ResponseEntity<Void> delete(@PathVariable Long gameListId) {
        gameListService.delete(gameListId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(description = "Move um jogo de posição dentro da lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogo movido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    @PostMapping("/{listId}/replacement")
    public void move(@PathVariable Long listId, @RequestBody ReplacementDTO corpo){
        gameListService.move(listId, corpo.getPosicaoOrigem(), corpo.getPosicaoDestino());
    }
}
