package com.devsuperior.dslist.services;

import com.devsuperior.dslist.dto.GameDTO;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.entities.Game;
import com.devsuperior.dslist.exceptions.GameDeleteException;
import com.devsuperior.dslist.exceptions.GameNotFound;
import com.devsuperior.dslist.exceptions.ListNotFound;
import com.devsuperior.dslist.exceptions.ScoreNotBetween;
import com.devsuperior.dslist.projections.GameMinProjection;
import com.devsuperior.dslist.repositories.GameListRepository;
import com.devsuperior.dslist.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class GameService{

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameListRepository gameListRepository;


    @Transactional(readOnly = true)
    public List<GameMinDTO> findAll(){
        List<Game> result = gameRepository.findAll();
        List<GameMinDTO> dto = result.stream().map(x -> new GameMinDTO(x)).toList();
        return dto;
    }

    @Transactional(readOnly = true)
    public GameDTO findById(Long id){
        Game result = gameRepository.findById(id).
                orElseThrow(() -> new GameNotFound("Game com o ID " + id + " não foi encontrado."));
        return new GameDTO(result);
    }

    public List<Game> containingTitle(String name){
        return gameRepository.findByTitleContainingIgnoreCase(name);
    }

    public List<Game> scoreGreaterOrEqual(Double score){
        Double minScore = 0.0;
        Double maxScore = 5.0;

        if (score >= minScore && score <= maxScore){
            return gameRepository.findByScoreGreaterThanEqual(score);
        }

        throw new ScoreNotBetween("O Score deve estar entre 0 e 5");
    }

    public GameMinDTO create(Game game){
        Game result = gameRepository.save(game);
        return new GameMinDTO(result);
    }

    public GameMinDTO update(Long id, Game newGame){
        Game game = gameRepository.findById(id).orElseThrow(() -> new GameNotFound("Game com o id " + id + " não foi encontrado."));

        game.setTitle(newGame.getTitle());
        game.setGenre(newGame.getGenre());
        game.setPlatforms(newGame.getPlatforms());
        game.setScore(newGame.getScore());
        game.setImgUrl(newGame.getImgUrl());
        game.setShortDescription(newGame.getShortDescription());
        game.setLongDescription(newGame.getLongDescription());

        gameRepository.save(game);

        return new GameMinDTO(game);
    }

    public void delete(long id) {
        try{

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFound("Game com o id " + id + " não foi encontrado."));

        gameRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new GameDeleteException("Não foi possivel deletar o jogo. Ele está relacionado a outros registros.");
        }
    }



    @Transactional(readOnly = true)
    public List<GameMinDTO> findByList(Long listId){

        if (!gameListRepository.existsById(listId)){
            throw new ListNotFound("Lista com o ID " + listId + " não foi encontrada.");
        }

        List<GameMinProjection> result = gameRepository.searchByList(listId);
        List<GameMinDTO> dto = result.stream().map(x -> new GameMinDTO(x)).toList();
        return dto;
    }
}
