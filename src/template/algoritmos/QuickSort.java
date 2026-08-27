package template.algoritmos;

import java.util.List;
import template.ui.EstadoOrdenacao;
import template.ui.SimuladorOrdenacaoBase;

/**
 * Simuladores de algoritmos de ordenação
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class QuickSort extends SimuladorOrdenacaoBase {
    
    // Construtor
    public QuickSort( int numeroElementos ) {
        super( numeroElementos, "Quick Sort" );
    }

    // Implementação dos métodos abstratos
    @Override
    protected void executarAlgoritmo(int[] a, List<EstadoOrdenacao> copias) {
        quickSort( a, 0, a.length-1, copias );
    }    
    
    @Override
    protected void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, posI, posJ, posMenor );
        copias.add( estadoCopia );
        
    }
    
    private void quickSort ( int[] a, int inicio, int fim, List<EstadoOrdenacao> copias ) {
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
        if( inicio < fim ) {
            int meio = particao( a, inicio, fim, copias );
            salvarEstadoOrdenacao( a, copias, inicio, meio, fim );
            quickSort( a, inicio, meio - 1, copias );
            quickSort( a, meio + 1, fim, copias );
        }
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
    }
    
    private int particao( int[] a, int inicio, int fim, List<EstadoOrdenacao> copias ) {
        
        int pivo = a[inicio];
        int i = inicio;
        int j = fim + 1;
        
        while (true) {
             
            while ( a[++i] < pivo) {
                if( i == fim ) {
                    break;
                }
                salvarEstadoOrdenacao(a, copias, inicio, i, j <= fim ? j : -1);
            }
            while (a[--j] > pivo) {
                if( j == inicio ) {
                    break;
                }
                salvarEstadoOrdenacao(a, copias, inicio, i <= fim ? i : -1, j);
            }

            if (i >= j) {
                break;
            }

            trocar(a, i, j);
            salvarEstadoOrdenacao(a, copias, inicio, i, j);
            
        }
        
        trocar( a, inicio, j );
        salvarEstadoOrdenacao(a, copias, j, -1, -1);
        return j;
        
    }
    
}