package template.algoritmos;

import java.util.List;
import template.ui.EstadoOrdenacao;
import template.ui.SimuladorOrdenacaoBase;

/**
 * Simuladores de algoritmos de ordenação
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class SelectionSort extends SimuladorOrdenacaoBase {

    // Construtor
    public SelectionSort( int numeroElementos ) {
        super( numeroElementos, "Selection Sort" );
    }

    // Implementação dos métodos abstratos
    @Override
    protected void executarAlgoritmo(int[] a, List<EstadoOrdenacao> copias) {
        selectionSort( a, copias );
    }    
    
    @Override
    protected void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, posI, posJ, posMenor );
        copias.add( estadoCopia );
        
    }
    
    // Funções necessárias
    private void selectionSort ( int[] a, List<EstadoOrdenacao> copias ) {
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        for( int i = 0; i < a.length; i++ ) {
            int menor = i;
            for( int j = i + 1; j < a.length; j++ ) {
                if( a[menor] > a[j] ){
                    menor = j;
                }
                salvarEstadoOrdenacao( a, copias, i, j, menor ); 
            }
            trocar( a, i, menor );
            salvarEstadoOrdenacao( a, copias, i, -1, menor );
        }
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
    }
    
    private void trocar ( int[]a, int i, int j ) {
        
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        
    }
    
}