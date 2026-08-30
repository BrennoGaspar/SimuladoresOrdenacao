package desenhos;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.util.ArrayList;
import java.util.List;
import UI.EstadoOrdenacao;
import UI.SimuladorOrdenacaoBase;

/**
 * Desenho da árvore binária do Heap Sort
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class HeapSortDesenho extends EngineFrame {
    
    // Variáveis
    private SimuladorOrdenacaoBase simulador;
    ArrayList<Posicao> grafos = new ArrayList<>();
    
    // Record
    private record Posicao( int x, int y ){};
    
    // Construtor
    public HeapSortDesenho ( SimuladorOrdenacaoBase simulador ) {
        super( 900, 450, "Heap Sort Desenho", 60, true );
        this.simulador = simulador;
    }
    
    @Override
    public void draw() {
        
        clearBackground( WHITE );
        
        EstadoOrdenacao e = simulador.getEstadoAtual();

        int[] vetorAtual = e.getA();
        int n = vetorAtual.length;
        
        grafos.clear();

        int iniX = getScreenWidth() / 2;
        int iniY = 30;
        int raio = 14;

        Posicao noRaiz = new Posicao( iniX, iniY );
        grafos.add( noRaiz );
        
        // Desenha as arestas
        for ( int i = 0; i < n; i++ ) {

            int esq = (2 * i) + 1;
            if ( esq < n ) {
                int nivelPai = (int) ( Math.log(i + 1) / Math.log(2) );
                int deslocamentoXEsq = (getScreenWidth() / 4) / ((int) Math.pow(2, nivelPai));

                Posicao pai = grafos.get( i );
                Posicao filhoEsq = new Posicao( pai.x - deslocamentoXEsq, pai.y + 50 );

                drawLine( pai.x, pai.y, filhoEsq.x, filhoEsq.y, BLACK );
                grafos.add( filhoEsq );
            }

            int dir = (2 * i) + 2;
            if ( dir < n ) {
                int nivelPai = (int) ( Math.log(i + 1) / Math.log(2) );
                int deslocamentoXDir = (getScreenWidth() / 4) / ((int) Math.pow(2, nivelPai));

                Posicao pai = grafos.get( i );
                Posicao filhoDir = new Posicao( pai.x + deslocamentoXDir, pai.y + 50 );

                drawLine( pai.x, pai.y, filhoDir.x, filhoDir.y, BLACK );
                grafos.add( filhoDir );
            }
        }
        
        // Desenha os nós (cículos) e pinta os que serão trocados
        for ( int i = 0; i < n; i++ ) {
            Posicao p = grafos.get( i );

            if ( i == e.getI() ) {
                fillCircle( p.x, p.y, raio, RED );
            } else if ( i == e.getJ() ) {
                fillCircle( p.x, p.y, raio, GREEN );
            } else {
                fillCircle( p.x, p.y, raio, WHITE );
            }

            drawCircle( p.x, p.y, raio, BLACK );
            drawText( String.format("%d", vetorAtual[i]), p.x - 5, p.y - 4, BLACK );
        }
    }
    
    // Métodos necessários por causa do extends
    @Override
    public void create() {}

    @Override
    public void update(double delta) {}
    
}