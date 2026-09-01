package UI;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.KEY_R;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.KEY_SPACE;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Menu base do sistema com interface visual aprimorada.
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public abstract class SimuladorOrdenacaoBase extends EngineFrame {
    
    private int[] a;
    private int numeroElementos;
    private List<EstadoOrdenacao> copias;
    private int copiaAtual;
    private double tempoParaMudar;
    private double contadorTempo;
    private JSlider slide;
    private JLabel label;
    private String textoStatus = "EM EXECUÇÃO";
    private boolean estaPausado = false;
    private boolean espacoPressionado = false;
    private double tempoRodando;
    
    // Paleta de Cores
    private static final Color COR_FUNDO = new Color( 24, 28, 36 );
    private static final Color COR_PAINEL = new Color( 33, 38, 49 );
    private static final Color COR_BORDA = new Color( 211, 211, 211 );
    private static final Color COR_BARRA_PADRAO = new Color( 74, 144, 226 );
    private static final Color COR_TEXTO_BARRA = new Color( 220, 225, 235 );
    private static final Color COR_TEXTO = new Color( 140, 150, 165 );
    private static final Color COR_PONTEIRO_I = new Color( 255, 99, 132 );
    private static final Color COR_PONTEIRO_J = new Color( 46, 204, 113 );
    private static final Color COR_PONTEIRO_ESP = new Color( 241, 196, 15 );

    public SimuladorOrdenacaoBase( int numeroElementos, String nome ) {
        
        super( 800, 480, nome, 60, true );
        this.numeroElementos = numeroElementos;
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        sliderVelocidade();
        
    }
    
    @Override
    public void create() {
        
        a = new int[numeroElementos];
        gerarArray( a, numeroElementos );
        copias = new ArrayList<>();
        copiaAtual = 0;
        
        tempoParaMudar = 0.4;
        contadorTempo = 0;
        
        executarAlgoritmo( a, copias );
        
    }
    
    @Override
    public void update( double delta ) {   
        
        if ( isKeyPressed( KEY_SPACE ) ) {
            if ( !espacoPressionado ) {
                ativarPause();
                espacoPressionado = true;
            }
        } else {
            espacoPressionado = false;
        }
        
        if ( !estaPausado ) {
            contadorTempo += delta;
            
            if ( contadorTempo >= tempoParaMudar ) {
                if ( copiaAtual < copias.size() - 1 ) {
                    copiaAtual++;
                    tempoRodando += contadorTempo;
                } else {
                    textoStatus = "CONCLUÍDO";
                }
                contadorTempo = 0;
            }
        }
        
        if ( isKeyPressed( KEY_R ) ) {
            resetar();
        }
        
    }
    
    @Override
    public void draw() {
        
        clearBackground( COR_FUNDO );
        
        if ( !copias.isEmpty() ) {
            desenharEstadoOrdenacao( copias.get( copiaAtual ) );
        }
        
        desenharInterfaceHUD();
        
    }
    
    private void desenharEstadoOrdenacao( EstadoOrdenacao e ) {
        
        int[] array = e.a;
        int margemEsquerda = 20;
        int larguraDisponivel = getScreenWidth() - 200;
        int iniY = getScreenHeight() - 40;
        
        int espacamento = numeroElementos > 30 ? 2 : 4;
        int larguraBarra = Math.max( 2, ( larguraDisponivel - ( espacamento * ( numeroElementos - 1 ) ) ) / numeroElementos );
        double escalaAltura = 3.2;

        for ( int i = 0; i < array.length; i++ ) {
            int val = array[i];
            int altura = (int) ( val * escalaAltura );
            int x = margemEsquerda + i * ( larguraBarra + espacamento );
            int y = iniY - altura;

            Color corBarra = COR_BARRA_PADRAO;
            
            if ( i == e.getI() ) {
                corBarra = COR_PONTEIRO_I;
            }
            else if ( i == e.getJ() ) {
                corBarra = COR_PONTEIRO_J;
            }
            else if ( i == e.getEspecial() ) {
                corBarra = COR_PONTEIRO_ESP;
            }

            fillRectangle( x, y, larguraBarra, altura, corBarra );
        }

        desenharPonteiro( array, e.getI(), larguraBarra, escalaAltura, espacamento, margemEsquerda, iniY, COR_PONTEIRO_I );
        desenharPonteiro( array, e.getJ(), larguraBarra, escalaAltura, espacamento, margemEsquerda, iniY, COR_PONTEIRO_J );
        desenharPonteiro( array, e.getEspecial(), larguraBarra, escalaAltura, espacamento, margemEsquerda, iniY, COR_PONTEIRO_ESP );
        
    }

    private void desenharPonteiro( int[] array, int posicao, int larguraBarra, double escalaAltura, int espacamento, int iniX, int iniY, Color cor ) {
        
        if ( posicao >= 0 && posicao < array.length ) {
            int alturaBarra = (int) ( array[posicao] * escalaAltura );
            int posX = iniX + posicao * ( larguraBarra + espacamento ) + larguraBarra / 2;
            int posY = iniY - alturaBarra - 10;

            fillCircle( posX, posY, 6, cor );
            drawCircle( posX, posY, 6, Color.WHITE );
        }
        
    }

    private void desenharInterfaceHUD() {
        
        int xHUD = getScreenWidth() - 170;
        int yHUD = 20;
        int larguraHUD = 150;
        int alturaHUD = 380;

        fillRectangle( xHUD, yHUD, larguraHUD, alturaHUD, COR_PAINEL );
        drawRectangle( xHUD, yHUD, larguraHUD, alturaHUD, COR_BORDA );

        drawText( "STATUS", xHUD + 15, yHUD + 25, 12, COR_TEXTO );
        drawText( textoStatus, xHUD + 15, yHUD + 45, 13, estaPausado ? COR_PONTEIRO_I : COR_PONTEIRO_J );

        drawText( "TEMPO DE EXECUÇÃO", xHUD + 15, yHUD + 85, 12, COR_TEXTO );
        drawText( String.format( "%.2f s", tempoRodando ), xHUD + 15, yHUD + 105, 16, COR_TEXTO_BARRA );

        drawText( "LEGENDA", xHUD + 15, yHUD + 155, 12, COR_TEXTO );
        
        fillCircle( xHUD + 20, yHUD + 180, 5, COR_PONTEIRO_I );
        drawText( "Ponteiro I", xHUD + 35, yHUD + 184, 11, COR_TEXTO_BARRA );

        fillCircle( xHUD + 20, yHUD + 205, 5, COR_PONTEIRO_J );
        drawText( "Ponteiro J", xHUD + 35, yHUD + 209, 11, COR_TEXTO_BARRA );

        fillCircle( xHUD + 20, yHUD + 230, 5, COR_PONTEIRO_ESP );
        drawText( "Pino/Especial", xHUD + 35, yHUD + 234, 11, COR_TEXTO_BARRA );

        drawText( "CONTROLES", xHUD + 15, yHUD + 285, 12, COR_TEXTO );
        drawText( "[ESPAÇO] Pause", xHUD + 15, yHUD + 310, 11, COR_TEXTO_BARRA );
        drawText( "[R] Reiniciar", xHUD + 15, yHUD + 330, 11, COR_TEXTO_BARRA );
        
    }
    
    private void ativarPause() {
        estaPausado = !estaPausado;
        if ( estaPausado ) {
            textoStatus = "PAUSADO";
        } else {
            textoStatus = "EM EXECUÇÃO";
        }
    }
    
    private void sliderVelocidade() {
        
        JPanel painelControles = new JPanel();
        painelControles.setBackground( COR_PAINEL );
        
        slide = new JSlider( 5, 500, 50 );
        slide.setFocusable( false );
        slide.setPaintLabels( false );
        slide.setPaintTicks( false );
        slide.setBackground( COR_PAINEL );
        
        label = new JLabel( "Velocidade: 1.0x" );
        label.setForeground( COR_TEXTO_BARRA );
        label.setFont( new Font( "SansSerif", Font.BOLD, 12 ) );
        
        slide.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged( ChangeEvent e ) {
                int val = slide.getValue();
                tempoParaMudar = 20.0 / val;
                
                double fatorVelocidade = val / 50.0;
                label.setText( String.format( "Velocidade: %.1fx", fatorVelocidade ) );
            } 
        } );
        
        painelControles.add( label );
        painelControles.add( slide );
          
        getContentPane().add( painelControles, BorderLayout.SOUTH );
        
    }
    
    protected abstract void executarAlgoritmo( int[] a, List<EstadoOrdenacao> copias );
    protected abstract void salvarEstadoOrdenacao( int[] origem, List<EstadoOrdenacao> copias, int posI, int posJ, int posMenor );
    
    private void resetar() {
        gerarArray( a, numeroElementos );
        copias = new ArrayList<>();
        copiaAtual = 0;
        contadorTempo = 0;
        tempoRodando = 0;
        estaPausado = false;
        textoStatus = "EM EXECUÇÃO";
        executarAlgoritmo( a, copias );
    }
    
    private void gerarArray( int[] a, int n ) {
        Random random = new Random();
        for ( int i = 0; i < n; i++ ) {
            a[i] = random.nextInt( 90 ) + 10;
        }
    }
    
    protected void trocar( int[] a, int i, int j ) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    public EstadoOrdenacao getEstadoAtual() {
        if ( copias != null && !copias.isEmpty() && copiaAtual < copias.size() ) {
            return copias.get( copiaAtual );
        }
        return null;
    }
}