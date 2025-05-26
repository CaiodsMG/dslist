package com.devsuperior.dslist.services;

import com.devsuperior.dslist.dto.GameListDTO;
import com.devsuperior.dslist.entities.GameList;
import com.devsuperior.dslist.exceptions.GameListNotFound;
import com.devsuperior.dslist.projections.GameMinProjection;
import com.devsuperior.dslist.repositories.GameListRepository;
import com.devsuperior.dslist.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
