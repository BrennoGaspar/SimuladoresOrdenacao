package template.algoritmos;

import java.util.List;
import template.ui.EstadoOrdenacao;
import template.ui.SimuladorOrdenacaoBase;

/**
 * Simuladores do algoritmo Insertion Sort
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class InsertionSort extends SimuladorOrdenacaoBase {
    
    // Construtor
    public InsertionSort( int numeroElementos ) {
        super( numeroElementos, "Insertion Sort" );
    }

    // Implementação dos métodos abstratos
    @Override
    protected void executarAlgoritmo(int[] a, List<EstadoOrdenacao> copias) {
        insertionSort( a, copias );
    }    
    
    @Override
    protected void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, posI, posJ, posMenor );
        copias.add( estadoCopia );
        
    }
    
    // Algoritmo de Ordenação Insertion Sort
    private void insertionSort ( int[] a, List<EstadoOrdenacao> copias ) {
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
        for( int i = 0; i < a.length; i++ ) {
            int j = i;
            while( j > 0 && a[j-1] > a[j] ) {
                trocar( a, j-1, j );
                salvarEstadoOrdenacao( a, copias, i, j, j-1 );
                j--;
            }
        }
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
    }
    
}