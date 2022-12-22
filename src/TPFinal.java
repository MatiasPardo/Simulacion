
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public class TPFinal {

    private static final int HV = 999999999; //694k dias - 23k meses
    private static final int TF = 730;
    private static final int N = 5; // impresoras
    
    
    public static void main(String[] args) throws Exception {
    	TPFinal tp6 = new TPFinal();
        tp6.simulacion();
    }
    int cantPiezas = 0;
    int t, nsp, nsr, rechazo, vasos, mates, Mst, ip, cpe, cantidadTotalDePiezasImpresas;
    int vs, pedidoAmigo, st, tpe, fll, cmd, pmd, noCumploConTotalidadPedido, vsTotales;

    List<Impresora> impresoras = new LinkedList<>();
    
    private void condicionesIniciales() {
        for(int i = 0 ; i<N ; i++){
        	Impresora m = new Impresora();
        	m.ito=0;
        	m.numeroImpresora = i;
        	impresoras.add(m);
        }
        vs = 0;
        vsTotales = 0;
        pedidoAmigo = 0;
        st = 12000;
        tpe = 8000;
        fll = 0;
        cmd = 0;
        pmd = 0;
        noCumploConTotalidadPedido = 0;
        vasos = 0;
        mates = 0;
        Mst = 5000;
        ip=0;
        cpe=0;
        cantidadTotalDePiezasImpresas=0;
        t = 0;   
    }
    
    public void simulacion(){
        boolean fin = false;
        condicionesIniciales();
        vs = ventasSemanales();
        vsTotales += vs;
        do{
            t = t + 1;
            double ra = random();
        	cmd = 0;
        	pmd = 0;
            
            if(ra > 0.8 && pedidoAmigo == 0) {
            	vs += 20;
            	pedidoAmigo = 1;
            }
            
            if(t % 7 == 0) {
                determinarVasosYMates();
            	if(cantPiezas < vs) {
                	noCumploConTotalidadPedido ++; 
            	}
            	vs = ventasSemanales();
            	vsTotales += vs;
            	pedidoAmigo = 0;
                cantidadTotalDePiezasImpresas += cantPiezas; 
            	cantPiezas = 0;
            }
            
            if(t == fll) {
            	st = st + tpe;
            	ip=0;
            }
            imprimirPiezas(); 
            //System.out.println("Dia numero: " + t + " Cantidad de piezas: " + cantPiezas);
            
            if((pmd + cmd) > st) {
            	noCumploConTotalidadPedido ++;
            }
            
            st = st-(cmd+pmd);
            
            if(st < Mst && ip==0) {
        		ip = 1;
        		fll = t + 1; //al otro dia
        		cpe = cpe + 1;
            }
                    	            
            if(!(t<TF)){
                fin = true;
            }
        }while(!fin);
        
        imprimirResultados();
    }

    private void determinarVasosYMates() {
    	
    	for(int i = 1; i< cantPiezas;i++) {
    		double r = random();
    		if (r<=0.4) 
    			vasos = vasos+1;
    		else 
    			mates = mates +1;
    	}
		
	}

	private void imprimirPiezas() {
    	for(Impresora imp: impresoras) {
    		if(cantPiezas < vs) {
    			int impFdp = cantImpresionesDiarias();
    			cantPiezas += impFdp;
    			cmd += cantMaterialUsado(impFdp);
    		}else {
    			imp.sto += 1;
    		}
    	}
	}

	private int cantMaterialUsado(int cid) {
		// fdp de cantidad de material usado de 200 y 350:          -158.617*(((1-x)^0.99119)-2.23681)
		double fdp = -1;
		for(int i = 1; i<cid; i++) {

			do{
				double valor1 = 1-random();
				double valor2 = 0.99119;
				fdp = -158.617*(Math.pow(valor1,valor2)-2.23681);
			}while((int) fdp < 200 || (int) fdp > 350);
			
			if((int) fdp < 0) {
				throw new RuntimeException("error en calculo de fdp de cantMaterialUsado");
			}
			cmd += (int) fdp;
			cantMaterialPerdido();
		}

		return (int) fdp;
	}

	private void cantMaterialPerdido() {
		// fdp de cantidad material perdido de 10 y 20:     14.738 - 2.091*tan(1.5708-3.14159x)
		double fdp = -1;
		do {
			fdp = 14.738 - 2.091*Math.tan(1.5708-3.14159*random());
		}while((int) fdp < 10 || (int) fdp > 20);
		if((int) fdp < 0) {
			throw new RuntimeException("error en calculo de fdp de cantMaterialUsado");
		}
		pmd += (int) fdp;
		
	
	}

	private int cantImpresionesDiarias() {
		// fdp de cantidad de impresiones diarias 2^(x+1)
		double valor1 = 2;
		double valor2 = random()+1;
		double fdp = Math.pow(valor1, valor2);
		if((int) fdp < 0 ) {
			throw new RuntimeException("error en calculo de fdp de cantImpresionesDiarias");
		}
		//System.out.println("Una impresora imprimio: " + (int) fdp);
		return (int) fdp; 
	}

	private double random() {
		return Math.random();
	}

	private void imprimirResultados() {
	
        BigDecimal tiempo = new BigDecimal(t);
        BigDecimal cien = new BigDecimal(100);
        BigDecimal totalPedidos = new BigDecimal(cantidadTotalDePiezasImpresas);
       	BigDecimal beneficioTotal = new BigDecimal(vasos*800 + mates*1200);
       	BigDecimal meses = tiempo.divide(new BigDecimal(30),2,RoundingMode.HALF_EVEN);
		BigDecimal beneficioMensual = beneficioTotal.divide(meses,2, RoundingMode.HALF_EVEN);
       	BigDecimal PNC = new BigDecimal(noCumploConTotalidadPedido).divide(new BigDecimal(t/7),2, RoundingMode.HALF_EVEN).multiply(cien);
        System.out.println("=============TP6 - IMPRESORAS 3D================");
        System.out.println("=============================");
        System.out.println("Tiempo simulacion: "+ t + " dias.");
        System.out.println("Cantidad de impresoras: "+N);
        for(Impresora imp: impresoras){
//            System.out.println("******Porcentaje de t osiosos *******");
//            System.out.println("Impresora: "+impresorasM.get(i).numeroImpresora);
//            System.out.println("TPS: " + impresorasM.get(i).tps);
			System.out.println("Tiempo Oscioso en dias: " + imp.sto);
			BigDecimal sto = new BigDecimal(imp.sto);
            System.out.println("PTO: " + sto.divide(tiempo,2, RoundingMode.HALF_EVEN).multiply(cien) + "%");
            System.out.println("***************");
        }
        System.out.println("Beneficio total: "+ beneficioTotal);
        System.out.println("Porcentaje de veces que no se pudo completar el pedido semanal: " + PNC);
        System.out.println("Beneficio Mensual: "+ beneficioMensual);
        System.out.println("Veces que no se completo el pedido semanal: " +noCumploConTotalidadPedido);
        System.out.println("Total de impresiones : " +totalPedidos);
        System.out.println("Total de pedidos: " +vsTotales);
//        System.out.println();
        System.out.println("Cantidad de pedidos extras: " + cpe);
        System.out.println("Stock final: " +st);
        System.out.println("Desperdicio total: " +pmd + cmd);
        System.out.println("Vasos: " +vasos);
        System.out.println("Mates: " +mates);
}

    private int ventasSemanales() {
    	// fdp impresoras la fdp de 46.835*(((1/((1-x)^0.442791))-1)^0.04657011130256602)
    	double r = random();
    	double uno = 1;
    	double valor2 = 0.04657011130256602;
    	double valor1 = (((1/(Math.pow(uno-r,0.442791)))-1));
    	double fdp =  46.835*Math.pow(valor1,valor2);
		if((int) fdp == -1) {
			throw new RuntimeException("error en calculo de fdp de ventasSemanaless");
		}
    	return (int) fdp;
    }


    public class Impresora{
        int numeroImpresora;
        int sto;
        int ito;
    }

}
