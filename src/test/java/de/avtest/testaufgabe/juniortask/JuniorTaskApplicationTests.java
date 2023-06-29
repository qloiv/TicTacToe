package de.avtest.testaufgabe.juniortask;

import de.avtest.testaufgabe.juniortask.data.GameBoard;
import de.avtest.testaufgabe.juniortask.data.SaveGame;
import de.avtest.testaufgabe.juniortask.data.SaveGameController;
import de.avtest.testaufgabe.juniortask.data.SaveGameRepository;
import de.avtest.testaufgabe.juniortask.data.enums.GameMark;
import de.avtest.testaufgabe.juniortask.data.enums.GamePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class JuniorTaskApplicationTests {
    @Mock
    private SaveGameRepository saveGameRepository;

    @InjectMocks
    private SaveGameController saveGameController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        saveGameController = new SaveGameController(saveGameRepository);
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void readFromDBTrue() {
        String uuid = "readFromDBTest";
        // The save game we mock to be in the database
        SaveGame saveGame = new SaveGame(uuid, 3, "HUMAN", "NONE,NONE,CROSS,NONE,NONE,NONE,NONE,NONE,NONE");
        // The game board which we should read from the database
        ArrayList<GameMark> gameBoard = new ArrayList<>(9);
        IntStream.range(0, 9).forEach(value -> gameBoard.add(GameMark.NONE));
        gameBoard.set(2, GameMark.CROSS);

        when(saveGameRepository.findByUuid(uuid)).thenReturn(saveGame);
        GameBoard board = saveGameController.readFromDB(uuid);

        assertEquals(board.getBoard(), gameBoard);
        assertEquals(board.getLastPlayer(), GamePlayer.HUMAN);
        assertEquals(board.getSize(), 3);
    }

    @Test
    public void readFromDBNullTrue() {
        String uuid = "readFromDBTest";
        // The save game we mock to be in the database

        when(saveGameRepository.findByUuid(uuid)).thenReturn(null);
        GameBoard board = saveGameController.readFromDB(uuid);

        assertNull(board);
    }

    @Test
    public void writeToDBTrue() {
        // Create Game Board to write into the db
        int size = 3;
        ArrayList<GameMark> board = new ArrayList<>(size * size);
        IntStream.range(0, size * size).forEach(value -> board.add(GameMark.NONE));
        board.set(2, GameMark.CIRCLE);
        GamePlayer lastPlayer = GamePlayer.HUMAN;
        GameBoard gameBoard = new GameBoard(size, board, lastPlayer);
        String uuid = "writeToDBTest";

        // read the save game that was passed into the database
        ArgumentCaptor<SaveGame> saveGameCaptor = ArgumentCaptor.forClass(SaveGame.class);
        saveGameController.writeToDB("writeToDBTest", gameBoard);

        verify(saveGameRepository).save(saveGameCaptor.capture());
        SaveGame capturedSaveGame = saveGameCaptor.getValue();

        // the save game should have the transformed values of our game board
        assertEquals("writeToDBTest", capturedSaveGame.getUuid());
        assertEquals(3, capturedSaveGame.getSize());
        assertEquals("HUMAN", capturedSaveGame.getLastPlayer());
        assertEquals("NONE,NONE,CIRCLE,NONE,NONE,NONE,NONE,NONE,NONE", capturedSaveGame.getGameBoard());
    }

}
