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

    /**
     * Write a certain game to the database
     *
     * @param gameId    gameId to write into the database
     * @param gameBoard gameBoard to write into the database
     */
    public void writeToDB(String gameId, GameBoard gameBoard) {
        SaveGame saveGame = transformToSaveGame(gameId, gameBoard);
        saveGameRepository.save(saveGame);
    }

    /**
     * Load a certain game from the database
     *
     * @param gameId game id of the game to load
     * @return the loaded game Board
     */
    public GameBoard readFromDB(String gameId) {
        SaveGame game = saveGameRepository.findByUuid(gameId);
        if (game != null) {
            return transformToGameBoard(game);
        } else {
            return null;
        }
    }

    /**
     * Transform a game board and id to a save game
     *
     * @param gameId    game id of the game board
     * @param gameBoard game board to transform
     * @return the transformed save game containing game board and game id
     */
    private SaveGame transformToSaveGame(String gameId, GameBoard gameBoard) {
        String board = boardToString(gameBoard);
        String lastPlayer = lastPlayerToString(gameBoard);
        int size = gameBoard.getSize();
        return new SaveGame(gameId, size, lastPlayer, board);
    }

    /**
     * Transform a save game to separate game board and id
     *
     * @param saveGame the loaded save game to transform
     * @return the transformed game board, not including the game id
     */
    private GameBoard transformToGameBoard(SaveGame saveGame) {
        String board = saveGame.getGameBoard();
        String lastPlayer = saveGame.getLastPlayer();
        int size = saveGame.getSize();
        List<GameMark> gameBoard = stringToBoard(board);
        GamePlayer gamePlayer = stringToLastPlayer(lastPlayer);
        return new GameBoard(size, gameBoard, gamePlayer);
    }

    private GamePlayer stringToLastPlayer(String lastPlayer) {
        return GamePlayer.valueOf(lastPlayer);
    }

    private String lastPlayerToString(GameBoard board) {
        return board.getLastPlayer().toString();
    }

    /**
     * Perform a string transformation for a game board string representation to a GameBoard
     *
     * @param boardString the game board in string representation
     * @return the game board as list of game marks
     */
    private List<GameMark> stringToBoard(String boardString) {
        return Arrays.stream(boardString.split(",", -1))
                .map(GameMark::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Perform a string transformation from a GameBoard to a string representation
     *
     * @param board the game board as list of game marks
     * @return a string representation of the game board, comma separated
     */
    private String boardToString(GameBoard board) {
        return board.getBoard().stream()
                .map(GameMark::toString)
                .collect(Collectors.joining(","));

    }

    /**
     * Load all saved games
     *
     * @return all games (id and game board) currently stored in the database
     */
    public Map<String, GameBoard> loadAllFromDB() {
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

    /**
     * Write all current games to the database
     *
     * @param entries all games (id and game board) to be stored in the database
     */
    public void writeAllToDB(Set<Map.Entry<String, GameBoard>> entries) {
        List<SaveGame> saveGames = entries.stream()
                .map(entry -> transformToSaveGame(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        saveGameRepository.saveAll(saveGames);
    }

    /**
     * Delete all game entries
     */
    public void deleteAllSaveGames() {
        saveGameRepository.deleteAll();
    }
}
