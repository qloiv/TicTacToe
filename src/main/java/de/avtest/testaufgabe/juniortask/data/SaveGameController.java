package de.avtest.testaufgabe.juniortask.data;

import de.avtest.testaufgabe.juniortask.data.enums.GameMark;
import de.avtest.testaufgabe.juniortask.data.enums.GamePlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaveGameController {
    private final SaveGameRepository saveGameRepository;

    @Autowired
    public SaveGameController(SaveGameRepository saveGameRepository) {
        this.saveGameRepository = saveGameRepository;
    }

    public void writeToDB(String uuid, GameBoard gameBoard) {
        SaveGame saveGame = transformToEntity(uuid, gameBoard);
        saveGameRepository.save(saveGame);
    }
    public GameBoard loadFromDB(String gameId) {
        SaveGame game = saveGameRepository.findByUuid(gameId);
        if (game != null) {
            return transformToGameBoard(game);
        } else {
            return new GameBoard(3); // return a standard game board
        }
    }

    private SaveGame transformToEntity(String uuid, GameBoard gameBoard) {
        String board = boardToString(gameBoard);
        String lastPlayer = lastPlayerToString(gameBoard);
        int size = gameBoard.getSize();
        return new SaveGame(uuid,size,lastPlayer,board);
    }



    private GameBoard transformToGameBoard(SaveGame game) {
        String board = game.getGameBoard();
        String lastPlayer = game.getLastPlayer();
        int size = game.getSize();
        List<GameMark> gameBoard = stringToBoard(board);
        GamePlayer lastPlayr = stringToLastPlayer(lastPlayer);
        return new GameBoard(size, gameBoard, lastPlayr);
    }

    private GamePlayer stringToLastPlayer(String lastPlayer){
        return GamePlayer.valueOf(lastPlayer);
    }
    public String lastPlayerToString(GameBoard board) {
        return board.getLastPlayer().toString();
    }
    private List<GameMark> stringToBoard(String boardString) {
        return Arrays.stream(boardString.split(",", -1))
                .map(GameMark::valueOf)
                .collect(Collectors.toList());
    }
    private String boardToString(GameBoard board) {
        return board.getBoard().stream()
                .map(GameMark::toString)
                .collect(Collectors.joining(","));

    }

    public Map<String,GameBoard> loadAllFromDB() {
        Iterable<SaveGame> saveGames = saveGameRepository.findAll();
        Map<String, GameBoard> map = new LinkedHashMap<>();
        for (SaveGame saveGame : saveGames) {
            if (saveGame == null) {
                continue;
            }
            GameBoard gameBoard = transformToGameBoard(saveGame);
            map.put(saveGame.getUuid(), gameBoard);
        }
        return map;
    }

    public void writeAllToDB(Set<Map.Entry<String, GameBoard>> entries) {
        List<SaveGame> saveGames = entries.stream()
                .map(entry -> transformToEntity(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        saveGameRepository.saveAll(saveGames);
    }
}
