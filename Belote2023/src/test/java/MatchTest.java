import com.github.itellijlover.model.Match;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchTest {

    @Test
    public void testGetId() {
        Match match = new Match(1, 2, 3, 5, 8, 4);
        int result = match.getId();
        assertEquals(1, result);
    }

    @Test
    public void testGetEquipe1() {
        Match match = new Match(1, 2, 3, 5, 8, 4);
        int result = match.getEq1();
        assertEquals(2, result);
    }

    @Test
    public void testGetEquipe2() {
        Match match = new Match(1, 2, 3, 5, 8, 4);
        int result = match.getEq2();
        assertEquals(3, result);
    }

    @Test
    public void testGetScore1() {
        Match match = new Match(1, 2, 3, 5, 8, 4);
        int result = match.getScore1();
        assertEquals(5, result);
    }

    @Test
    public void testGetScore2() {
        Match match = new Match(1, 2, 3, 5, 8, 4);
        int result = match.getScore2();
        assertEquals(8, result);
    }

    @Test
    public void testGetNumTour() {
        Match match = new Match(1, 2, 3, 5, 8, 4);
        int result = match.getNumTour();
        assertEquals(4, result);
    }

    @Test
    public void testSetScore1() {
        Match match = new Match(1, 2, 3, 5, 8, 4);
        match.setScore1(19);
        int result = match.getScore1();
        assertEquals(19, result);
    }

    @Test
    public void testSetScore2() {
        Match match = new Match(1, 2, 3, 5, 8, 4);
        match.setScore2(18);
        int result = match.getScore2();
        assertEquals(18, result);
    }
}
