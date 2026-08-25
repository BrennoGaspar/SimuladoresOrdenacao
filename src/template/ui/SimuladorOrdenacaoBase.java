package template.ui;

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
 *
 * @author driog
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
    private JLabel lblStatus;
    private final Object pauseLock = new Object();
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
    
    
    // Lógica do pause
    public void ativarPause(){
            estaPausado = !estaPausado;
            
            if(estaPausado){
                lblStatus.setText("| Pausado (Aperte ESPACO para Continuar)");
            }else{
                lblStatus.setText("| Rodando (Aperte ESPACO para Pausar)");
            }
    }
    
    
    // Slider de Controle de Velocidade 
    private void sliderVelocidade() {
        
        JPanel painelControles = new JPanel();
        
        slide = new JSlider( 5, 500, 50 );
        // fixing reset not working after changing the speed.
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
        
        // Lógica do Botão
        
        lblStatus = new JLabel(" | [ESPACO]: Rodando (Aperte para Pausar)");
        painelControles.add(lblStatus);
          
        getContentPane().add( painelControles, BorderLayout.SOUTH );
        
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
        if(isKeyPressed(KEY_SPACE)){
            if( !espacoPressionado){
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
        drawText( String.format( "Tempo rodando: %.2f", tempoRodando ), 10, 10, Color.BLACK );
        
    }
    
    // Funções secundárias
    protected abstract void executarAlgoritmo( int[] a, List<EstadoOrdenacao> copias );
    
    protected abstract void salvarEstadoOrdenacao ( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor );
    
    public void resetar (){
        
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
        
        int larguraDisponivel = getScreenWidth() - (iniX * 2);
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
                iniY - alturaBarra - espaco,
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
    
}
