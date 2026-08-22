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
public class ShellSort extends EngineFrame {
    
    // Classe Interna
    private record EstadoOrdenacao( int[] a, int i, int j, int h ){};
    
    // Atributos 
    private int[] a;
    private List<EstadoOrdenacao> copias;
    private int copiaAtual;
    private double tempoParaMudar;
    private double contadorTempo;
    
    // Construtor
    public ShellSort() {
        
        super( 800, 450, "Shell Sort", 60, true );
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
        
        ShellSort(a, copias );
        
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
        desenharBolinhas( a, tamanho, 25, espacamento, iniX, iniY, e.h, Color.ORANGE );
        
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
    
    private void ShellSort ( int[] a, List<EstadoOrdenacao> copias ) {
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
        int h = 1;
        while( h < a.length / 3 ) {
            h = 3 * h + 1;
        }
        while( h >= 1 ){
            for( int i = h; i < a.length; i++ ) {
                int j = i;
                while( j >= h && a[j-h] > a[j] ) {
                    trocar( a, j-h, j );
                    salvarEstadoOrdenacao( a, copias, i, j, h );
                    j = j - h;
                }
            }
            h = h / 3;
        }
        
        salvarEstadoOrdenacao( a, copias, -1, -1, -1 );
        
    }
    
    private void trocar ( int[]a, int i, int j ) {
        
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        
    }
    
    private void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posH ) {
        
        int[] copia = new int[ origem.length ];
        System.arraycopy( origem, 0, copia, 0, origem.length );
        EstadoOrdenacao estadoCopia = new EstadoOrdenacao( copia, posI, posJ, posH );
        copias.add( estadoCopia );
        
    }
    
    public void resetar (){
        
        a = new int[] {9, 5, 4, 1, 2, 7, 6, 8, 3, 10};
        copias = new ArrayList<>();
        copiaAtual = 0;
        tempoParaMudar = 0.2;
        contadorTempo  = 0;
        ShellSort( a, copias );
        
    }
    
}
