
import com.github.itellijlover.model.Equipe;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EquipeTest {
    @Test
    public void testGetId() {
        Equipe equipe = new Equipe(1, 1, 1, "nom_j1", "nom_j2");
        int result = equipe.getId();
        assertEquals(1, result);
    }

    @Test
    public void testGetNum() {
        Equipe equipe = new Equipe(1, 1, 1, "nom_j1", "nom_j2");
        int result = equipe.getNum();
        assertEquals(1, result);
    }

    @Test
    public void testGetIdTournoi() {
        Equipe equipe = new Equipe(1, 1, 1, "nom_j1", "nom_j2");
        int result = equipe.getIdTournoi();
        assertEquals(1, result);
    }

    @Test
    public void testGetNomJ1() {
        Equipe equipe = new Equipe(1, 1, 1, "nom_j1", "nom_j2");
        String result = equipe.getNomJ1();
        assertEquals("nom_j1", result);
    }

    @Test
    public void testGetNomJ2() {
        Equipe equipe = new Equipe(1, 1, 1, "nom_j1", "nom_j2");
        String result = equipe.getNomJ2();
        assertEquals("nom_j2", result);
    }

    @Test
    public void testSetNomJ1() {
        Equipe equipe = new Equipe(1, 1, 1, "nom_j1", "nom_j2");
        equipe.setNomJ1("new_nom_j1");
        String result = equipe.getNomJ1();
        assertEquals("new_nom_j1", result);
    }

    @Test
    public void testSetNomJ2() {
        Equipe equipe = new Equipe(1, 1, 1, "nom_j1", "nom_j2");
        equipe.setNomJ2("new_nom_j2");
        String result = equipe.getNomJ2();
        assertEquals("new_nom_j2", result);
    }

}
