
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public class TP6 {

    private static final int HV = 999999999; //694k dias - 23k meses
    private static final int TF = 999999;
    private static final int N = 4; //PLA
    private static final int M = 3; //Resina
    private static final boolean DIAS_ESPECIALES = false;
    
    
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
        BigDecimal tiempo = new BigDecimal(t);
        BigDecimal cien = new BigDecimal(100);
        for(int i =0 ; i< (N);i++){
//            System.out.println("******Porcentaje de t osiosos PLA *******");
//            System.out.println("Impresora: "+impresorasN.get(i).numeroImpresora);
//            System.out.println("TPS: " + impresorasN.get(i).tps);
            BigDecimal pto = new BigDecimal(impresorasN.get(i).pto);
			System.out.println("PPTOP: " + pto.divide(tiempo, 2, RoundingMode.HALF_EVEN).multiply(cien) +"%");
            System.out.println("***************");
//          System.out.println("ITO: " + impresorasN.get(i).ito);
        }
        System.out.println("Impresoras de resina: "+M);

        for(int i =0 ; i< (M);i++){
//            System.out.println("******Porcentaje de t osiosos Resina *******");
//            System.out.println("Impresora: "+impresorasM.get(i).numeroImpresora);
//            System.out.println("TPS: " + impresorasM.get(i).tps);
            BigDecimal pto = new BigDecimal(impresorasM.get(i).pto);
			System.out.println("PPTOP: " + pto.divide(tiempo, 2, RoundingMode.HALF_EVEN).multiply(cien) +"%");
            System.out.println("***************");
            //System.out.println("ITO: " + im.ito);
        }

//        double division = rechazoN/(nspt+nsrt+rechazoN+rechazoM);
		BigDecimal totalPedidos = new BigDecimal(nspt+nsrt+rechazoN+rechazoM);
		BigDecimal rechazosPLA = new BigDecimal(rechazoN);
		BigDecimal rechazosResina = new BigDecimal(rechazoM);
		BigDecimal rechazosPorcentajePLA = rechazosPLA.divide(totalPedidos,2,RoundingMode.HALF_EVEN).multiply(cien);
        BigDecimal rechazosPorcentajeResina = rechazosResina.divide(totalPedidos,2,RoundingMode.HALF_EVEN).multiply(cien);
//      BigDecimal totalIngresosPLA = new BigDecimal(nspt*2400); 
//      System.out.println("NSPT: " + nspt + " Ingresos: "+ totalIngresosPLA.divide(tiempo.divide(new BigDecimal(60),2,RoundingMode.HALF_EVEN),2,RoundingMode.HALF_EVEN).divide(new BigDecimal(24),2,RoundingMode.HALF_EVEN) + " Rechazados: " + rechazosPLA);
        System.out.println("NSPT: " + nspt + " Rechazados: " + rechazosPLA + " - % Rechazos PLA: " + rechazosPorcentajePLA);
        System.out.println("NSRT: " + nsrt + " Rechazados: " + rechazosResina +  " - % Rechazos Resina: " + rechazosPorcentajeResina);
        System.out.println("Total : " +totalPedidos);


    }

    int t, tpll, ia, ta, nsp, nsr, nsrt, nspt, rechazoN, rechazoM;

    List<Impresora> impresorasM = new LinkedList<>();
    List<Impresora> impresorasN = new LinkedList<>();
    
    private void condicionesIniciales() {
        for(int i = 0 ; i<M;i++){
        	MR m = new MR();
        	m.tps=HV;
        	m.pto=0;
        	m.ito=0;
        	m.numeroImpresora = i;
        	impresorasM.add(m);
        }        
        for(int i = 0 ; i<N;i++){
        	NP n = new NP();
        	n.tps=HV;
        	n.pto=0;
        	n.ito=0;
        	n.numeroImpresora = i;
        	impresorasN.add(n);      
        }
        t = 0;
        tpll = 1;
        ia = 0;
        ta = 0;
        nsp = 0;
        nsr = 0;
        nsrt = 0;
        nspt = 0;
        rechazoN = 0;
        rechazoM = 0;
        
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
        ia = intervaloPedido(DIAS_ESPECIALES);
        tpll = t + ia;
        double r = Math.random();
        if(r<=new Double(0.7)){
            if(nsp>N+5){
                rechazoN++;
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
            if(nsr>M+5){
                rechazoM++;
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
        //  M 120 y 240 minutos y N 360 y 1020 minutos

        if(impresora instanceof NP){
        	//F(x)'=-97.165ln(-(1-x)/x)+702.87
    		double r = Math.random();
			ta = (int) (-97.165*Math.log((1-r)/r)+702.87);
        }
        else{
        	//F(x)'=-ln(1-r)/0.00533
            ta = (int) (Math.random()*(240-120+1)+120);
        }
        return ta;
    }

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

        double ia = 0;

        if(diasEspeciales){
        	//F(R)' =  -ln(1-x)/0.00949 
            ia = -Math.log(1-Math.random())/(0.00949);
        }else{
            //F(R)' = -48.583ln((1/x)-1)+295.76
            ia = -48.583*Math.log((1/Math.random())-1)+295.76;
        }

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
}
