package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Simuladores de algoritmos de ordenação
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class MergeSort extends EngineFrame {
    
    // Classe Interna
    private record EstadoOrdenacao( int[] a, int i, int j ){};
    
    // Atributos 
    private int[] a;
    private List<EstadoOrdenacao> copias;
    private int copiaAtual;
    private double tempoParaMudar;
    private double contadorTempo;
    
    // Construtor
    public MergeSort() {
        
        super( 800, 450, "Merge Sort", 60, true );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        
    }
    
    // Funções do JSGE
    @Override
    public void create() {
        
        a = new int[] {9, 5, 4, 1, 2, 7, 6, 8, 3, 10};
        copias = new ArrayList<>();
        copiaAtual = 0;
        
        tempoParaMudar = 1;
        contadorTempo  = 0;
        
        mergeSort(a, 0, a.length - 1, copias );
        
    }
    
    @Override
    public void update( double delta ) {        
        
        contadorTempo += delta;
        
        if( contadorTempo >= tempoParaMudar ) {
            if( copiaAtual < copias.size()-1 ) {
                copiaAtual++;
            }
            contadorTempo = 0;
        }
        
        if( isKeyPressed( KEY_R ) ) {
            resetar();
        }
        
        if( isKeyPressed( KEY_E ) ) {
            tempoParaMudar -= 0.1;
        }
        
        if( isKeyPressed( KEY_Q ) ) {
            tempoParaMudar += 0.1;
        }
        
    }
    
    @Override
    public void draw() {
        
        clearBackground( WHITE );
        desenharEstadoOrdenacao( copias.get(copiaAtual) );
        
    }
    
    // Funções Secundárias
    private void desenharEstadoOrdenacao( EstadoOrdenacao e  ){
        
        int[] a = e.a;
        int tamanho = 30;
        int espacamento = 10;
        int iniX = 10;
        int iniY = getScreenHeight() - 10;
        
        for( int i = 0; i < a.length; i++ ) {
            int v = a[i];
            int altura = v * tamanho;
            fillRectangle( 
                iniX + i * (tamanho + espacamento), // x
                iniY - altura, // y
                tamanho, // largura
                altura,  // altura
                Color.BLUE
            );
            
        }
        
        desenharBolinhas( a, tamanho, 5, espacamento, iniX, iniY, e.i, Color.RED );
        desenharBolinhas( a, tamanho, 15, espacamento, iniX, iniY, e.j, Color.GREEN );
        
    }
    
    private void desenharBolinhas( int[] a, int tamanho, int espaco, int espacamento, int iniX, int iniY, int variavel, Color cor ){
        if( variavel >= 0 ) {
            fillCircle( 
                iniX + variavel * (tamanho + espacamento) + tamanho / 2,
                iniY - a[variavel] * tamanho - espaco,
                5,
                cor
            );
        }
    }
    
    private void mergeSort ( int[] a, int inicio, int fim, List<EstadoOrdenacao> copias ) {
        
        if( inicio < fim ) {
            int meio = (inicio + fim) / 2;
            mergeSort( a, inicio, meio, copias );
            mergeSort( a, meio + 1, fim, copias );
            intercala( a, inicio, meio, fim, copias );
        }
        
        salvarEstadoOrdenacao( a, copias, -1, -1 );
        
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

            salvarEstadoOrdenacao(a, copias, k, indiceComparado);

            if (i > meio) {
                a[k] = b[j++];
            } else if (j > fim) {
                a[k] = b[i++];
            } else if (b[j] < b[i]) {
                a[k] = b[j++];
            } else {
                a[k] = b[i++];
            }

            salvarEstadoOrdenacao(a, copias, k, -1);
            
        }
        
    }
    
    private void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, posI, posJ );
        copias.add( estadoCopia );
        
    }
    
    public void resetar (){
        
        a = new int[] {9, 5, 4, 1, 2, 7, 6, 8, 3, 10};
        copias = new ArrayList<>();
        copiaAtual = 0;
        tempoParaMudar = 0.2;
        contadorTempo  = 0;
        mergeSort( a, 0, a.length - 1, copias );
        
    }
    
}
