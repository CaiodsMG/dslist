package com.devsuperior.dslist.services;

import com.devsuperior.dslist.dto.GameListDTO;
import com.devsuperior.dslist.entities.Game;
import com.devsuperior.dslist.entities.GameList;
import com.devsuperior.dslist.exceptions.GameDeleteException;
import com.devsuperior.dslist.exceptions.GameListNotFound;
import com.devsuperior.dslist.exceptions.GameNotFound;
import com.devsuperior.dslist.projections.GameMinProjection;
import com.devsuperior.dslist.repositories.GameListRepository;
import com.devsuperior.dslist.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameListService {

    @Autowired
    private GameListRepository gameListRepository;

    @Autowired
    private GameRepository gameRepository;

    public List<GameListDTO> findAll(){
        List<GameList> result = gameListRepository.findAll();
        return result.stream().map(x -> new GameListDTO(x)).toList();
    }

    public GameListDTO findById(Long id){
        GameList gameList = gameListRepository.findById(id)
                .orElseThrow(() -> new GameListNotFound("GameList com o id " + id + " Não foi encontrado." ));

        return new GameListDTO(gameList);
    }

    public List<GameListDTO> findAllDesc(){
        List<GameList> result = gameListRepository.findAllByOrderByIdDesc();
        return result.stream().map(x -> new GameListDTO(x)).toList();
    }

    public GameListDTO create(GameList gameList){
        GameList newGameList = gameListRepository.save(gameList);
        return new GameListDTO(newGameList);
    }

    public GameListDTO update(Long id, GameList newGameList){
        GameList gameList = gameListRepository.findById(id).
                orElseThrow(() -> new GameListNotFound("GameList com o id " + id + " Não foi encontrado."));
        gameList.setName(newGameList.getName());

        gameListRepository.save(gameList);

        return new GameListDTO(gameList);
    }

    public void delete(long id) {
        try{

            GameList gameList = gameListRepository.findById(id)
                    .orElseThrow(() -> new GameListNotFound("GameList com o id " + id + " não foi encontrado."));

            gameListRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new GameDeleteException("Não foi possivel deletar o GameList. Ele está relacionado a outros registros.");
        }
    }


    public void move(Long listId, int posicaoOrigem, int posicaoDestino){
        List<GameMinProjection> list = gameRepository.searchByList(listId);

        GameMinProjection obj = list.remove(posicaoOrigem);
        list.add(posicaoDestino, obj);

        int min = posicaoOrigem < posicaoDestino ? posicaoOrigem : posicaoDestino;
        int max = posicaoOrigem < posicaoDestino ? posicaoDestino : posicaoOrigem;

        for (int i = min; i <= max; i++) {
            gameListRepository.updateBelongingPosition(listId, list.get(i).getId(), i);
        }



    }
}
