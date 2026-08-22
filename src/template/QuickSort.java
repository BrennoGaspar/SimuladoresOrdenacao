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
public class QuickSort extends EngineFrame {
    
    // Classe Interna
    private record EstadoOrdenacao( int[] a, int inicio, int meio, int fim ){};
    
    // Atributos 
    private int[] a;
    private List<EstadoOrdenacao> copias;
    private int copiaAtual;
    private double tempoParaMudar;
    private double contadorTempo;
    
    // Construtor
    public QuickSort() {
        
        super( 800, 450, "Quick Sort", 60, true );
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
        
        quickSort(a, 0, a.length - 1, copias );
        
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
        
        desenharBolinhas( a, tamanho, 5, espacamento, iniX, iniY, e.inicio, Color.RED );
        desenharBolinhas( a, tamanho, 15, espacamento, iniX, iniY, e.fim, Color.GREEN );
        desenharBolinhas( a, tamanho, 25, espacamento, iniX, iniY, e.meio, Color.ORANGE );
        
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
    
    private void trocar ( int[]a, int i, int j ) {
        
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        
    }
    
    private void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int inicio, int meio, int fim ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, inicio, meio, fim );
        copias.add( estadoCopia );
        
    }
    
    public void resetar (){
        
        a = new int[] {9, 5, 4, 1, 2, 7, 6, 8, 3, 10};
        copias = new ArrayList<>();
        copiaAtual = 0;
        tempoParaMudar = 0.2;
        contadorTempo  = 0;
        quickSort( a, 0, a.length - 1, copias );
        
    }
    
}
