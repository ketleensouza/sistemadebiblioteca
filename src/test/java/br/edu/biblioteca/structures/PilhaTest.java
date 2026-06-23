package br.edu.biblioteca.structures;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Testes unitários para a classe Pilha<T>
 */
public class PilhaTest {
    private Pilha<Integer> pilha;

    @Before
    public void setUp() {
        pilha = new Pilha<>();
    }

    @Test
    public void testPush() {
        pilha.push(1);
        pilha.push(2);
        
        assertEquals(2, pilha.size());
    }

    @Test
    public void testPop() {
        pilha.push(1);
        pilha.push(2);
        pilha.push(3);
        
        assertEquals(3, (int) pilha.pop());
        assertEquals(2, (int) pilha.pop());
        assertEquals(1, pilha.size());
    }

    @Test
    public void testPeek() {
        pilha.push(1);
        pilha.push(2);
        
        assertEquals(2, (int) pilha.peek());
        assertEquals(2, pilha.size()); // Não remove
    }

    @Test
    public void testIsEmpty() {
        assertTrue(pilha.isEmpty());
        pilha.push(1);
        assertFalse(pilha.isEmpty());
    }

    @Test
    public void testLIFO() {
        pilha.push(1);
        pilha.push(2);
        pilha.push(3);
        
        assertEquals(3, (int) pilha.pop());
        assertEquals(2, (int) pilha.pop());
        assertEquals(1, (int) pilha.pop());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmVazio() {
        pilha.pop();
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmVazio() {
        pilha.peek();
    }

    @Test
    public void testClear() {
        pilha.push(1);
        pilha.push(2);
        pilha.clear();
        
        assertTrue(pilha.isEmpty());
    }
}
