package template.algoritmos;

import java.util.List;
import template.ui.EstadoOrdenacao;
import template.ui.SimuladorOrdenacaoBase;

/**
 * Simuladores do algoritmo Merge Sort
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class MergeSort extends SimuladorOrdenacaoBase {
    
    // Construtor
    public MergeSort( int numeroElementos ) {
        super( numeroElementos, "Merge Sort" );
    }

    // Implementação dos métodos abstratos
    @Override
    protected void executarAlgoritmo(int[] a, List<EstadoOrdenacao> copias) {
        mergeSort( a, 0, a.length-1, copias );
    }    
    
    @Override
    protected void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, posI, posJ, posMenor );
        copias.add( estadoCopia );
        
    }
    
    // Algoritmo de Ordenação Merge Sort
    private void mergeSort ( int[] a, int inicio, int fim, List<EstadoOrdenacao> copias ) {
        
        if( inicio < fim ) {
            int meio = (inicio + fim) / 2;
            mergeSort( a, inicio, meio, copias );
            mergeSort( a, meio + 1, fim, copias );
            intercala( a, inicio, meio, fim, copias );
        }
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
    }
    
    private void intercala( int[] a, int inicio, int meio, int fim, List<EstadoOrdenacao> copias ) {
        
        int[] b = new int[ a.length ];
        
        for( int k = inicio; k <= fim; k++ ) {
            b[k] = a[k];
        }
        
        int i = inicio;
        int j = meio + 1;
        
        for (int k = inicio; k <= fim; k++) {
            
            int indiceComparado = -1;
            if (i <= meio && j <= fim) {
                indiceComparado = (b[j] < b[i]) ? j : i;
            } else if (i <= meio) {
                indiceComparado = i;
            } else if (j <= fim) {
                indiceComparado = j;
            }

            salvarEstadoOrdenacao( a, copias, k, indiceComparado, -1 );

            if (i > meio) {
                a[k] = b[j++];
            } else if (j > fim) {
                a[k] = b[i++];
            } else if (b[j] < b[i]) {
                a[k] = b[j++];
            } else {
                a[k] = b[i++];
            }

            salvarEstadoOrdenacao( a, copias, k, -1, -1 );
            
        }
        
    }
    
}