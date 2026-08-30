package algoritmos;

import java.util.List;
import UI.EstadoOrdenacao;
import UI.SimuladorOrdenacaoBase;

/**
 * Simuladores do algoritmo Shell Sort
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class ShellSort extends SimuladorOrdenacaoBase {
    
    // Construtor
    public ShellSort( int numeroElementos ) {
        super( numeroElementos, "Shell Sort" );
    }

    // Implementação dos métodos abstratos
    @Override
    protected void executarAlgoritmo(int[] a, List<EstadoOrdenacao> copias) {
        shellSort( a, copias );
    }    
    
    @Override
    protected void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, posI, posJ, posMenor );
        copias.add( estadoCopia );
        
    }
    
    // Algoritmo de Ordenação Shell Sort
    private void shellSort ( int[] a, List<EstadoOrdenacao> copias ) {
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
        int h = 1;
        while( h < a.length / 3 ) {
            h = 3 * h + 1;
        }
        while( h >= 1 ){
            for( int i = h; i < a.length; i++ ) {
                int j = i;
                
                if (j >= h) {
                    salvarEstadoOrdenacao( a, copias, i, j - h, j );
                }
                
                while( j >= h && a[j-h] > a[j] ) {
                    trocar( a, j-h, j );
                    salvarEstadoOrdenacao( a, copias, i, j, j-h );
                    j = j - h;
                }
            }
            h = h / 3;
        }
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
    }
    
}
