package br.edu.biblioteca.structures;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Testes unitários para a classe ArvoreBST<K, V>
 */
public class ArvoreBSTTest {
    private ArvoreBST<String, String> arvore;

    @Before
    public void setUp() {
        arvore = new ArvoreBST<>();
    }

    @Test
    public void testPut() {
        arvore.put("ISBN1", "Livro1");
        arvore.put("ISBN2", "Livro2");
        
        assertEquals(2, arvore.size());
    }

    @Test
    public void testGet() {
        arvore.put("ISBN1", "Livro1");
        arvore.put("ISBN2", "Livro2");
        
        assertEquals("Livro1", arvore.get("ISBN1"));
        assertEquals("Livro2", arvore.get("ISBN2"));
    }

    @Test
    public void testGetInexistente() {
        arvore.put("ISBN1", "Livro1");
        
        assertNull(arvore.get("ISBN3"));
    }

    @Test
    public void testRemove() {
        arvore.put("ISBN1", "Livro1");
        arvore.put("ISBN2", "Livro2");
        arvore.put("ISBN3", "Livro3");
        
        String removido = arvore.remove("ISBN2");
        
        assertEquals("Livro2", removido);
        assertEquals(2, arvore.size());
        assertNull(arvore.get("ISBN2"));
    }

    @Test
    public void testContainsKey() {
        arvore.put("ISBN1", "Livro1");
        
        assertTrue(arvore.containsKey("ISBN1"));
        assertFalse(arvore.containsKey("ISBN2"));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(arvore.isEmpty());
        arvore.put("ISBN1", "Livro1");
        assertFalse(arvore.isEmpty());
    }

    @Test
    public void testAtualizarValor() {
        arvore.put("ISBN1", "Livro1");
        arvore.put("ISBN1", "LivroAtualizado");
        
        assertEquals("LivroAtualizado", arvore.get("ISBN1"));
        assertEquals(1, arvore.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutChaveNula() {
        arvore.put(null, "Livro");
    }

    @Test
    public void testRemoverNodo() {
        // Teste complexo: remover nó com dois filhos
        arvore.put("D", "D");
        arvore.put("B", "B");
        arvore.put("F", "F");
        arvore.put("A", "A");
        arvore.put("C", "C");
        arvore.put("E", "E");
        arvore.put("G", "G");
        
        arvore.remove("B"); // Nó com dois filhos
        
        assertEquals(6, arvore.size());
        assertNull(arvore.get("B"));
        assertEquals("A", arvore.get("A"));
        assertEquals("C", arvore.get("C"));
    }
}
