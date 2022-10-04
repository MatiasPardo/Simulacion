
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class TP6 {

    private static final int HV = 999999999; //694k dias - 23k meses
    private static final int TF = 99999;
    private static final int N = 1;
    private static final int M = 1;

    int t, tpll, ia, ta, nsp, nsr, nsrt, nspt, rechazo;

    List<Impresora> impresorasM = new LinkedList<>();
    List<Impresora> impresorasN = new LinkedList<>();

    private void condicionesIniciales() {
        MR m1 = new MR();
        MR m2 = new MR();
        MR m3 = new MR();
        MR m4 = new MR();
        MR m5 = new MR();
        NP n1 = new NP();
        NP n2 = new NP();
        NP n3 = new NP();
        NP n4 = new NP();
        NP n5 = new NP();
        m1.tps=HV;
        m1.pto=0;
        m1.ito=0;
        m1.numeroImpresora = 1;

        m2.tps=HV;
        m2.pto=0;
        m2.ito=0;
        m2.numeroImpresora = 2;
        
        m3.tps=HV;
        m3.pto=0;
        m3.ito=0;
        m3.numeroImpresora = 3;
        
        m4.tps=HV;
        m4.pto=0;
        m4.ito=0;
        m4.numeroImpresora = 4;

        m5.tps=HV;
        m5.pto=0;
        m5.ito=0;
        m5.numeroImpresora = 5;

        n1.tps=HV;
        n1.pto=0;
        n1.ito=0;
        n1.numeroImpresora = 1;

        n2.tps=HV;
        n2.pto=0;
        n2.ito=0;
        n2.numeroImpresora = 2;

        n3.tps=HV;
        n3.pto=0;
        n3.ito=0;
        n3.numeroImpresora = 3;

        n4.tps=HV;
        n4.pto=0;
        n4.ito=0;
        n4.numeroImpresora = 4;

        n5.tps=HV;
        n5.pto=0;
        n5.ito=0;
        n5.numeroImpresora = 5;

        t = 0;
        tpll = 1;
        ia = 0;
        ta = 0;
        nsp = 0;
        nsr = 0;
        nsrt = 0;
        nspt = 0;
        rechazo = 0;
        impresorasM.add(m1);
        impresorasM.add(m2);
        impresorasM.add(m3);
        impresorasM.add(m4);
        impresorasM.add(m5);
        impresorasN.add(n1);
        impresorasN.add(n2);
        impresorasN.add(n3);
        impresorasN.add(n4);
        impresorasN.add(n5);
        
    }


    public class Impresora{
        int numeroImpresora;
        int tps;
        int pto;
        int ito;
    }

    public class MR extends Impresora{
    }

    public class NP extends Impresora{
    }


    public static void main(String[] args) throws Exception {
    	TP6 tp6 = new TP6();
        tp6.simulacion();
    }

    public void simulacion(){
        boolean fin = false;
        condicionesIniciales();
        do{
            int tps = menorPuesto();
            if(tpll <= tps){
                procesarLlegada();
            }else{
                procesarSalida();
            }
            if(!(t<TF)){
                fin = true;
            }
        }while(!fin);
        imprimirResultados();
    }

    private void imprimirResultados() {
        System.out.println("=============TP6 - IMPRESORAS 3D================");
        System.out.println("=============================");
        System.out.println("Tiempo simulacion: "+ t + " minutos o " + t/60 + " horas. o " + t/60/24 + " dias" );
        System.out.println("Impresoras de PLA: "+N);
        System.out.println("Impresoras de resina: "+M);
        System.out.println("******Tiempos osiosos de cada impresora*******");
        for(Impresora im: impresorasN){
            System.out.println("Impresora: "+im.numeroImpresora);
            System.out.println("TPS: " + im.tps);
            System.out.println("PTO: " + im.pto);
            System.out.println("ITO: " + im.ito);
        }
        for(Impresora im: impresorasM){
            System.out.println("Impresora: "+im.numeroImpresora);
            System.out.println("TPS: " + im.tps);
            System.out.println("PPTOP: " + im.pto/t);
            //System.out.println("ITO: " + im.ito);
        }

        System.out.println("Rechazos: " + rechazo);
        System.out.println("NSPT: " + nspt);
        System.out.println("NSRT: " + nsrt);


    }


    private void procesarSalida() {
        Impresora impresoraM= menorPuestoM();
        Impresora impresoraN= menorPuestoN();
        if(impresoraM.tps<impresoraN.tps){
            t=impresoraM.tps;
            nsr --;
            if(nsr>=M){
                ta = tiempoAtencionImpresora(impresoraM);
                impresoraM.tps += ta;
            }else{
                impresoraM.ito = t;
                impresoraM.tps = HV;
            }
        }else{
            t=impresoraN.tps;
            nsp --;
            if(nsp>=N){
                ta = tiempoAtencionImpresora(impresoraN);
                impresoraN.tps += ta;
            }else{
                impresoraN.ito = t;
                impresoraN.tps = HV;
            }
        }

    }

    private void procesarLlegada() {
        t = tpll;
        ia = intervaloPedido(true);
        tpll = t + ia;
        double r = Math.random();
        if(r<=new Double(0.7)){
            if(nsp>5){
                rechazo++;
            }else{
                nsp++;
                nspt++;
                if(nsp<=N){
                    Impresora impresoraLibreN = buscoPuestoLibreN();
                    if(impresoraLibreN == null){
                        throw new RuntimeException("Hubo un problema al buscar una impresora N con HV");
                    }
                    ta = tiempoAtencionImpresora(impresoraLibreN);
                    impresoraLibreN.tps =t + ta;
                    impresoraLibreN.pto += (t-impresoraLibreN.ito);
                }
            }
        }else{
            if(nsr>5){
                rechazo++;
            }else{
                nsr++;
                nsrt++;
                if(nsr<=M){
                    Impresora impresoraLibreM = buscoPuestoLibreM();
                    if(impresoraLibreM == null){
                        throw new RuntimeException("Hubo un problema al buscar una impresora M con HV");
                    }
                    ta = tiempoAtencionImpresora(impresoraLibreM);
                    impresoraLibreM.tps =t + ta;
                    impresoraLibreM.pto += (t-impresoraLibreM.ito);
                }
            }
        }


    }

    private int tiempoAtencionImpresora(Impresora impresora) {
        int ta;
        // TODO  M 120 y 240 minutos y N 360 y 1020 minutos
        /*int ta = random();

        boolean seguirBuscando = true;
        do{
            if(impresora instanceof NP){
                if(ta>120 && ta<240){
                    seguirBuscando = false;
                }else{
                    ta = random();
                }
            }
            if(impresora instanceof MR){
                if(ta>360 && ta<1020){
                    seguirBuscando = false;
                }else{
                    ta = random();
                }
            }

            System.out.println(ta);
        }while(seguirBuscando);
        return ta;*/
        if(impresora instanceof NP){
            ta = (int) (Math.random()*(1020-360+1)+360);
        }
        else{
            ta = (int) (Math.random()*(240-120+1)+120);
        }
        return ta;
    }
    /*public int random(){
        double taDouble = new Double(String.valueOf(Math.random()).substring(2,5));
        return new BigDecimal(taDouble).intValue();
    }*/

    private Impresora buscoPuestoLibreN() {
        for(Impresora impresora: impresorasN){
            if(impresora.tps == HV){
                return impresora;
            }
        }
        return null;
    }

    private Impresora buscoPuestoLibreM() {
        for(Impresora impresora: impresorasM){
            if(impresora.tps == HV){
                return impresora;
            }
        }
        return null;
    }

    private int intervaloPedido(boolean diasEspeciales) {

        // TODO calcular con la fdp -> f(R) = ln(-R+1)/(-0,0002)
        //return (int) (Math.random()*(480-120+1)+120);

        double ia = 0;

//		for(int i =0;i<500;i++){
        if(diasEspeciales){
            ia = Math.log10(-Math.random()+1)/(-0.02);
        }else{
            //f(R) = 1*(-ln(-R+1))^(1/3)
            ia = Math.pow(-Math.log10(-Math.random()+1),0.333);
        }
//		System.out.println(ia/2);
//		}



        return new BigDecimal(ia/2).intValue();
   }

    private int menorPuesto() {
        Impresora tpsMinM = menorPuestoM();
        Impresora tpsMinN = menorPuestoN();
        if(tpsMinM.tps > tpsMinN.tps){
            return tpsMinN.tps;
        }else{
            return tpsMinM.tps;
        }

    }

    private Impresora menorPuestoM() {
        int tpsMin = HV;
        Impresora im = null;
        for(Impresora impresora: impresorasM){
            if(tpsMin >= impresora.tps){
                tpsMin = impresora.tps;
                im = impresora;
            }
        }
        return im;
    }

    private Impresora menorPuestoN() {
        int tpsMin = HV;
        Impresora im = null;
        for(Impresora impresora: impresorasN){
            if(tpsMin >= impresora.tps){
                tpsMin = impresora.tps;
                im = impresora;
            }
        }
        return im;
    }


}
