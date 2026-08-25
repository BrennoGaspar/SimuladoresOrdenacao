package template.algoritmos;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.util.ArrayList;
import java.util.List;
import template.ui.EstadoOrdenacao;
import template.ui.SimuladorOrdenacaoBase;

/**
 * Simuladores de algoritmos de ordenação
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class BubbleSort extends SimuladorOrdenacaoBase {
    
    // Construtor
    public BubbleSort( int numeroElementos ) {
        super( numeroElementos, "Bubble Sort" );
    }

    // Implementação dos métodos abstratos
    @Override
    protected void executarAlgoritmo(int[] a, List<EstadoOrdenacao> copias) {
        BubbleSort( a, copias );
    }    
    
    @Override
    protected void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, posI, posJ, posMenor );
        copias.add( estadoCopia );
        
    }
    
    private void BubbleSort ( int[] a, List<EstadoOrdenacao> copias ) {
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
       
        int i = 0;
        boolean trocou;
        
        do {
            trocou = false;
            for( int j = 0; j < a.length - 1; j++ ) {
                salvarEstadoOrdenacao( a, copias, j, j+1, -1 );
                if( a[j] > a[j+1] ) {
                    trocar( a, j, j+1 );
                    salvarEstadoOrdenacao( a, copias, j, j+1, -1 );
                    trocou = true;
                }
            }
            i++;
        } while ( trocou && i < a.length );
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
    }
    
    private void trocar ( int[]a, int i, int j ) {
        
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        
    }
    
}