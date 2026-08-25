package template.algoritmos;

import java.util.ArrayList;
import java.util.List;
import template.ui.EstadoOrdenacao;
import template.ui.SimuladorOrdenacaoBase;

/**
 * Simuladores de algoritmos de ordenação
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class HeapSort extends SimuladorOrdenacaoBase {
    
    // Construtor
    public HeapSort( int numeroElementos ) {
        super( numeroElementos, "Heap Sort" );
    }

    // Implementação dos métodos abstratos
    @Override
    protected void executarAlgoritmo(int[] a, List<EstadoOrdenacao> copias) {
        heapSort( a, copias );
    }    
    
    @Override
    protected void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, posI, posJ, posMenor );
        copias.add( estadoCopia );
        
    }
    
    private void afundar ( int[] a, int k, int n, List<EstadoOrdenacao> copias ) {
        
        while( 2*k + 1 < n ) {
            int j = 2*k + 1;
            if( j+1 < n && a[j] < a[j+1] ) {
                j++;
            }
            if( a[k] >= a[j] ) {
                break;
            }
            trocar( a, k, j );
            salvarEstadoOrdenacao( a, copias, k, j, -1 );
            k = j;
        }
        
                
    }
    
    private void heapSort ( int[] a, List<EstadoOrdenacao> copias ) {
        
        int n = a.length;
        
        for( int k = n/2 - 1; k >= 0; k-- ) {
            afundar( a, k, n, copias );
        }
        while( n > 1 ) {
            n--;
            trocar( a, 0, n ); 
            salvarEstadoOrdenacao( a, copias, 0, n, -1 );            
            afundar( a, 0, n, copias );
        }
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
    }
    
}