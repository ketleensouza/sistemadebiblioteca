package br.edu.biblioteca.structures;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Testes unitários para a classe Fila<T>
 */
public class FilaTest {
    private Fila<String> fila;

    @Before
    public void setUp() {
        fila = new Fila<>();
    }

    @Test
    public void testEnqueue() {
        fila.enqueue("a");
        fila.enqueue("b");
        
        assertEquals(2, fila.size());
    }

    @Test
    public void testDequeue() {
        fila.enqueue("a");
        fila.enqueue("b");
        fila.enqueue("c");
        
        assertEquals("a", fila.dequeue());
        assertEquals("b", fila.dequeue());
        assertEquals(1, fila.size());
    }

    @Test
    public void testPeek() {
        fila.enqueue("a");
        fila.enqueue("b");
        
        assertEquals("a", fila.peek());
        assertEquals(2, fila.size()); // Não remove
    }

    @Test
    public void testIsEmpty() {
        assertTrue(fila.isEmpty());
        fila.enqueue("a");
        assertFalse(fila.isEmpty());
    }

    @Test
    public void testFIFO() {
        fila.enqueue(1);
        fila.enqueue(2);
        fila.enqueue(3);
        
        assertEquals("1", fila.dequeue().toString());
        assertEquals("2", fila.dequeue().toString());
        assertEquals("3", fila.dequeue().toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEmVazio() {
        fila.dequeue();
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmVazio() {
        fila.peek();
    }

    @Test
    public void testClear() {
        fila.enqueue("a");
        fila.enqueue("b");
        fila.clear();
        
        assertTrue(fila.isEmpty());
    }

    @Test
    public void testRedimensionamento() {
        for (int i = 0; i < 20; i++) {
            fila.enqueue("item" + i);
        }
        
        assertEquals(20, fila.size());
        assertEquals("item0", fila.peek());
    }
}
