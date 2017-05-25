package triangulo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Pedro Lanzagorta
 */
public class triangulo {
    
    int[][] matrix;
    int[] maxIndices;
    final int NUM_RENGLONES=5;
    final int NUM_COLUMNAS=5;
    
    public triangulo(){
        matrix = new int[NUM_RENGLONES][NUM_COLUMNAS];
        for(int i = 0; i<NUM_RENGLONES;i++){
            for(int j=0;j<NUM_COLUMNAS;j++){
                if(i==NUM_RENGLONES-1 && j==NUM_COLUMNAS-1)
                    matrix[i][j]=1;
                else 
                if(j>=i+1){
                    matrix[i][j]=-1;
                }
                else
                    matrix[i][j]=0;
            }
        }
    }
    
    public void resuelveJuego(){
        Scanner reader = new Scanner(System.in);  
        Stack<String> pila = new Stack();
        
        pila.add("¡Felicidades! Has ganado el juego.");
        pesquisa(matrix,pila);
        pila.add("Acomoda asi el tablero para comenzar."+"\n\n"+formateaMatriz(matrix));        
        boolean b =false;
        System.out.println(pila.pop());
        while(!pila.isEmpty()){
            System.out.println(pila.pop());
            if(b)
            System.out.println("Pulsa cualquier tecla para continuar.");
            b=!b;
            reader.nextLine();
        }
        
    }
    public boolean[] revisaPosibilidades(int i, int j,int[][] m){
        boolean[] posibilidades = new boolean[8];
        boolean alguno=false;
        //Revisamos arriba
        if( i-1>=0 && m[i-1][j]==0 && i-2>=0 && m[i-2][j]>0){
            //brinca
            posibilidades[0]=true;
            alguno=true;
        }
        //Revisamos abajo
        if( i+1<NUM_RENGLONES && m[i+1][j]==0 && i+2<NUM_RENGLONES && m[i+2][j]>0){
            //brinca 
            posibilidades[1]=true;
            alguno=true;
        }
        //Revisamos izquiera
        if( j-1>=0 && m[i][j-1]==0 && j-2>=0 && m[i][j-2]>0){
            //brinca 
            posibilidades[2]=true;
            alguno=true;

        }
        //Revisamos derecha
        if( j+1<NUM_COLUMNAS && m[i][j+1]==0 && j+2<NUM_COLUMNAS && m[i][j+2]>0){
            //brinca 
            posibilidades[3]=true;
            alguno=true;
        }
        
        // Revisamos arriba-izquierda
        if( j-1>=0 && i-1>=0 && m[i-1][j-1]==0 
                && j-2>=0 && i-2>=0 && m[i-2][j-2]>0){
            //brinca 
            posibilidades[4]=true;
            alguno=true;

        }
        // Revisamos arriba-derecha
        if( i-1>=0 && j+1<NUM_COLUMNAS && m[i-1][j+1]==0 
                && i-2>=0 && j+2<NUM_COLUMNAS && m[i-2][j+2]>0){
            //brinca 
            posibilidades[5]=true;
            alguno=true;

        }
        // Revisamos abajo-izquierda
        if( i+1<NUM_RENGLONES && j-1>=0 && m[i+1][j-1]==0 
                && i+2<NUM_RENGLONES && j-2>=0 && m[i+1][j-1]>0){
            //brinca 
            posibilidades[6]=true;
            alguno=true;

        }
        // Revisamos abajo-derecha
        if( j+1<NUM_COLUMNAS && i+1<NUM_RENGLONES && m[i+1][j+1]==0 
                && j+2<NUM_COLUMNAS && i+2<NUM_RENGLONES && m[i+2][j+2]>0){
            //brinca 
            posibilidades[7]=true;
            alguno=true;

        }
        
        return alguno?posibilidades:null;
    }
    
    public int[][] salta(int i,int j,int decision, int[][] matriz){
        matriz[i][j]=1;
        switch(decision){
            case 0:
                matriz[i-1][j]=1;
                matriz[i-2][j]=0;
                break;
            case 1:
                matriz[i+1][j]=1;
                matriz[i+2][j]=0;
                break;
            case 2:
                matriz[i][j-1]=1;
                matriz[i][j-2]=0;
                break;
            case 3:
                matriz[i][j+1]=1;
                matriz[i][j+2]=0;
                break;
            case 4:
                matriz[i-1][j-1]=1;
                matriz[i-2][j-2]=0;
                break;
            case 5:
                matriz[i-1][j+1]=1;
                matriz[i-2][j+2]=0;
                break;
            case 6:
                matriz[i+1][j-1]=1;
                matriz[i+2][j-2]=0;
                break;
            case 7:
                matriz[i+1][j+1]=1;
                matriz[i+2][j+2]=0;
                break; 
        }
        return matriz;
        
    }
    
    
    public int pesquisa(int[][] m,Stack respuesta){
        int r = 0;
        ArrayList<int[]> coordenadas = new ArrayList();
        ArrayList<boolean[]> posibilidades = new ArrayList();
        for(int i =0;i<NUM_RENGLONES;i++){
            for(int j=0;j<=i;j++){
                if(m[i][j]==0){
                    r++;
                    boolean[] pos=revisaPosibilidades(i,j,m);
                    if(pos!=null){
                        int[] cor = {i,j};
                        coordenadas.add(cor);
                        posibilidades.add(pos);
                    }
                }
            }
        }
        
        if(r==1){
            return 1;
        }
        for(int k=0;k<posibilidades.size();k++){
            for(int l=0;l<8;l++){
                if(posibilidades.get(k)[l]==true){
                    int[][] copia=  copiaMatriz(m);
                    int[][] temp = salta(coordenadas.get(k)[0],coordenadas.get(k)[1],l,copia);
                    int x = pesquisa(temp,respuesta);
                    if(x ==1){
                        String ins =formateaInstruccion(coordenadas.get(k)[0],coordenadas.get(k)[1],l);
                        String fig= "\n\n"+formateaMatriz(copia);
                        respuesta.push(fig);
                        respuesta.push(ins);
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    
    public String formateaInstruccion(int i, int j, int decision){
        String dec="";
        switch(decision){
            case 0:
                dec="hacia arriba.";
                break;
            case 1:
                dec="hacia abajo.";
                break;
            case 2:
                dec="hacia la izquierda.";
                break;
            case 3:
                dec="hacia la derecha.";
                break;
            case 4:
                dec="hacia arriba y a la izquierda.";
                break;
            case 5:
                dec="hacia arriba y a la derecha.";
                break;
            case 6:
                dec="hacia abajo y a la izquierda.";
            case 7:
                dec="hacia abajo y a la derecha.";
        }
        int[] a={1,0,-1,-2,-3};
        
        String ins="Mueve la pieza en la posicion "+((i+a[j]))+" de la columna "+(j+1)+" "+dec;
        return ins;
        
    }
    public static void test(){
        StringBuilder sb = new StringBuilder();
        int[] b={1,0,-1,-2,-3};
        int[][] a = new int[5][5];
        for(int i =0;i<5;i++){
            for(int j =0;j<i;j++)
                sb.append("("+(i+b[j]-1)+","+(j+1)+")  ");
        sb.append("\n");
        }
        System.out.println(sb.toString());
    }
    public static int[][] copiaMatriz(int[][] src) {
    int[][] dst = new int[src.length][];
    for (int i = 0; i < src.length; i++) {
        dst[i] = Arrays.copyOf(src[i], src[i].length);
    }
    return dst;
}
    public static void imprimeMatriz(int[][] m){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++)
                sb.append(m[i][j]+"   ");
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
    public static String formateaMatriz(int[][] temp){
        StringBuilder sb = new StringBuilder();
        int[][] m = new int[5][5];
        for(int i=0;i<5;i++)
            for(int j=0;j<=i;j++)
                m[i][j]=1-temp[i][j];
        sb.append(m[0][0]+"\n");
        sb.append("    "+m[1][1]+"\n");
        sb.append(m[1][0]+"       "+m[2][2]+"\n");
        sb.append("    "+m[2][1]+"       "+m[3][3]+"\n");
        sb.append(m[2][0]+"       "+m[3][2]+"       "+m[4][4]+"\n");
        sb.append("    "+m[3][1]+"       "+m[4][3]+"\n");
        sb.append(m[3][0]+"       "+m[4][2]+"\n");
        sb.append("    "+m[4][1]+"\n");
        sb.append(m[4][0]+"\n");

        return sb.toString();
    }
    
    public static void main(String[] args) {
        triangulo t = new triangulo();
        t.resuelveJuego();
        

        
    }
    
}
