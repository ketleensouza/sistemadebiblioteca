package br.edu.biblioteca.structures;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Testes unitários para a classe Vetor<T>
 */
public class VetorTest {
    private Vetor<String> vetor;

    @Before
    public void setUp() {
        vetor = new Vetor<>();
    }

    @Test
    public void testAdicionar() {
        vetor.add("elemento1");
        assertEquals(1, vetor.size());
        assertEquals("elemento1", vetor.get(0));
    }

    @Test
    public void testAdicionarMultiplos() {
        vetor.add("a");
        vetor.add("b");
        vetor.add("c");
        assertEquals(3, vetor.size());
    }

    @Test
    public void testRemover() {
        vetor.add("a");
        vetor.add("b");
        vetor.add("c");
        
        String removido = vetor.remove(1);
        assertEquals("b", removido);
        assertEquals(2, vetor.size());
        assertEquals("c", vetor.get(1));
    }

    @Test
    public void testRemoverPorObjeto() {
        vetor.add("a");
        vetor.add("b");
        vetor.add("c");
        
        boolean removido = vetor.remove("b");
        assertTrue(removido);
        assertEquals(2, vetor.size());
        assertFalse(vetor.contains("b"));
    }

    @Test
    public void testContains() {
        vetor.add("a");
        vetor.add("b");
        
        assertTrue(vetor.contains("a"));
        assertFalse(vetor.contains("c"));
    }

    @Test
    public void testSet() {
        vetor.add("a");
        vetor.add("b");
        
        vetor.set(0, "x");
        assertEquals("x", vetor.get(0));
    }

    @Test
    public void testRedimensionamento() {
        for (int i = 0; i < 15; i++) {
            vetor.add("elemento" + i);
        }
        assertEquals(15, vetor.size());
        assertEquals("elemento14", vetor.get(14));
    }

    @Test
    public void testClear() {
        vetor.add("a");
        vetor.add("b");
        vetor.clear();
        
        assertEquals(0, vetor.size());
        assertTrue(vetor.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalido() {
        vetor.add("a");
        vetor.get(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveInvalido() {
        vetor.add("a");
        vetor.remove(5);
    }
}
