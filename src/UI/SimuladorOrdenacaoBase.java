package UI;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.KEY_R;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.WHITE;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.KEY_SPACE;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Menu base do sistema, usado por todas as outras classes.
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public abstract class SimuladorOrdenacaoBase extends EngineFrame {
    
    // Atributos
    private int[] a;
    private int numeroElementos;
    private List<EstadoOrdenacao> copias;
    private int copiaAtual;
    private double tempoParaMudar;
    private double contadorTempo;
    private JSlider slide;
    private JLabel label;
    private String textoStatus = "[ESPACO] | Pausar";
    private boolean estaPausado = false;
    private boolean espacoPressionado = false;
    private double tempoRodando;
    
    // Construtor
    public SimuladorOrdenacaoBase ( int numeroElementos, String nome ) {
        
        super( 800, 450, nome, 60, true );
        this.numeroElementos = numeroElementos;
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        sliderVelocidade();
        
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
        
        executarAlgoritmo( a, copias );
        
    }
    
    @Override
    public void update( double delta ) {        
        
        // Lógica Play/Pause 
        if( isKeyPressed(KEY_SPACE) ){
            if( !espacoPressionado ){
                ativarPause();
                espacoPressionado = true;
            }
        }else{
            espacoPressionado = false;
        }
        
        // Lógica de Animação
        
        if(!estaPausado){
        contadorTempo += delta;
        
        if( contadorTempo >= tempoParaMudar ) {
            if( copiaAtual < copias.size()-1 ) {
                copiaAtual++;
                tempoRodando += contadorTempo;
            }
            contadorTempo = 0;
        }
        }
        
        if( isKeyPressed( KEY_R ) ) {
            resetar();
        }
        
    }
    
    @Override
    public void draw() {
        
        clearBackground( WHITE );
        desenharEstadoOrdenacao( copias.get(copiaAtual) );
        drawText( String.format( "Tempo rodando: %.2f", tempoRodando ), 10, 10, 20, Color.BLACK );
        drawText( String.format( "%s", textoStatus ), getScreenWidth() - 142, getScreenHeight() / 2, 12, Color.BLACK );
        drawText( "[R] - Reiniciar", getScreenWidth() - 142, getScreenHeight() / 2+10, 12, Color.BLACK );
        
    }
    
    // Lógica do pause
    private void ativarPause(){
            estaPausado = !estaPausado;
            
            if( estaPausado ){
                textoStatus = "[ESPACO] | Continuar";
            }else{
                textoStatus = "[ESPACO] | Pausar";
            }
    }
    
    // Slider de Controle de Velocidade 
    private void sliderVelocidade() {
        
        JPanel painelControles = new JPanel();
        
        slide = new JSlider( 5, 500, 50 );
        // Arrumado erro de não funcionar o resetar() após mexer na velocidade
        slide.setFocusable(false);
        slide.setPaintLabels( false );
        slide.setPaintTicks( true );
        
        label = new JLabel( "Velocidade: 1.0x" );
        
        slide.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged( ChangeEvent e ) {
                int val = slide.getValue();
                
                tempoParaMudar = 20.0 / val;
                
                double fatorVelocidade = val / 50.0;
                label.setText( String.format( "Velocidade: %.1fx", fatorVelocidade ) );
            } 
        });
        
        painelControles.add( slide );
        painelControles.add( label );
          
        getContentPane().add( painelControles, BorderLayout.SOUTH );
        
    }
    
    // Funções abstratas para implementar de forma individual em cada algoritmo
    protected abstract void executarAlgoritmo( int[] a, List<EstadoOrdenacao> copias );
    
    protected abstract void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor );
    
    // Funções genéricas para todos algoritmos
    private void resetar (){
        
        gerarArray( a, numeroElementos );
        copias = new ArrayList<>();
        copiaAtual = 0;
        contadorTempo  = 0;
        tempoRodando = 0;
        executarAlgoritmo( a, copias );
        
    }
    
    private void gerarArray( int[] a, int n ) {
        Random random = new Random();
        for( int i = 0; i < n; i++ ) {
            a[i] = random.nextInt(99);
        }
    }
    
    private void desenharEstadoOrdenacao( EstadoOrdenacao e ){
        
        int[] a = e.a;
        int espacamento = 4;
        int iniX = 10;
        int iniY = getScreenHeight() - 10;
        
        int larguraDisponivel = getScreenWidth() - (iniX * 15);
        int tamanho = (larguraDisponivel - (espacamento * (numeroElementos - 1))) / numeroElementos;
        double escalaAltura = 3.5;
        
        for( int i = 0; i < a.length; i++ ) {
            int v = a[i];
            int altura = (int) (v * escalaAltura);
            fillRectangle( 
                iniX + i * (tamanho + espacamento), // x
                iniY - altura, // y
                tamanho, // largura
                altura,  // altura
                Color.BLUE
            );
            // Texto adicionado mostrando os valores de cada elemento do array (cada barra)
            drawText( String.format("%d", v), iniX + i * (tamanho + espacamento) + tamanho / 2 - 5, iniY - altura - 10, BLACK );
        }
        
        desenharBolinhas( a, tamanho, escalaAltura, 5,  espacamento, iniX, iniY, e.getI(), Color.RED );
        desenharBolinhas( a, tamanho, escalaAltura, 15, espacamento, iniX, iniY, e.getJ(), Color.GREEN );
        desenharBolinhas( a, tamanho, escalaAltura, 25, espacamento, iniX, iniY, e.getEspecial(), Color.ORANGE );
        
    }
    
    private void desenharBolinhas( int[] a, int tamanho, double escalaAltura, int espaco, int espacamento, int iniX, int iniY, int variavel, Color cor ){
        
        if( variavel >= 0 && variavel < a.length ) {
            int alturaBarra = (int) (a[variavel] * escalaAltura);
            fillCircle( 
                iniX + variavel * (tamanho + espacamento) + tamanho / 2,
                iniY - alturaBarra - espaco - 15,
                5,
                cor
            );
        }
        
    }
    
    protected void trocar ( int[]a, int i, int j ) {
        
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        
    }
    
    // Getters
    public EstadoOrdenacao getEstadoAtual() {
        if (copias != null && !copias.isEmpty() && copiaAtual < copias.size()) {
            return copias.get(copiaAtual);
        }
        return null;
    }
    
    
}
