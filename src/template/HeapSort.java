package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simuladores de algoritmos de ordenação
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class HeapSort extends EngineFrame {
    
    // Classe Interna
    private record EstadoOrdenacao( int[] a, int i, int j ){};
    
    // Atributos 
    private int[] a;
    private int numeroElementos;
    private List<EstadoOrdenacao> copias;
    private int copiaAtual;
    private double tempoParaMudar;
    private double contadorTempo;
    
    // Construtor
    public HeapSort( int numeroElementos ) {
        
        super( 800, 450, "Heap Sort", 60, true );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        this.numeroElementos = numeroElementos;
        
    }
    
    // Funções do JSGE
    @Override
    public void create() {
        
        a = new int[ numeroElementos ];
        gerarArray( a, numeroElementos );
        copias = new ArrayList<>();
        copiaAtual = 0;
        
        tempoParaMudar = 1;
        contadorTempo  = 0;
        
        salvarEstadoOrdenacao(a, copias, -1, -1);
        heapSort(a, copias);
        salvarEstadoOrdenacao(a, copias, -1, -1);
        
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
    private void gerarArray( int[] a, int n ) {
        Random random = new Random();
        for( int i = 0; i < n; i++ ) {
            a[i] = random.nextInt(99);
        }
    }
    
    private void desenharEstadoOrdenacao( EstadoOrdenacao e  ){
        
        int[] a = e.a;
        int espacamento = 10;
        int iniX = 10;
        int iniY = getScreenHeight() - 10;
        
        int larguraDisponivel = getScreenWidth() - (iniX * 2);
        int tamanho = (larguraDisponivel - (espacamento * (numeroElementos - 1))) / numeroElementos;
        double escalaAltura = 3.5;
        
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
        
        desenharBolinhas( a, tamanho, escalaAltura, 5, espacamento, iniX, iniY, e.i, Color.RED );
        desenharBolinhas( a, tamanho, escalaAltura, 15, espacamento, iniX, iniY, e.j, Color.GREEN );
        
    }
    
    private void desenharBolinhas( int[] a, int tamanho, double escalaAltura, int espaco, int espacamento, int iniX, int iniY, int variavel, Color cor ){
        if( variavel >= 0 && variavel < a.length ) {
            
            int alturaBarra = (int) (a[variavel] * escalaAltura);
            
            fillCircle( 
                iniX + variavel * (tamanho + espacamento) + tamanho / 2,
                iniY - alturaBarra - espaco,
                5,
                cor
            );
        }
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
            salvarEstadoOrdenacao( a, copias, k, j );
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
            salvarEstadoOrdenacao( a, copias, 0, n );            
            afundar( a, 0, n, copias );
        }
        
        salvarEstadoOrdenacao( a, copias, -1, -1 );
        
    }
    
    private void trocar ( int[]a, int i, int j ) {
        
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        
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
        heapSort( a, copias );
        
    }
    
}
