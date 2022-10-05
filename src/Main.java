import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class Main {

	private static final int CANTIDAD_DECIMALES = 3;

	public static void main(String[] args) throws Exception {	
//		BigDecimal random = new BigDecimal(50); 
		
//		List<BigDecimal> numAleatorios = generarNumerosAleatorios(random,50);
//		Map<BigDecimal, Integer> misNumeros = cargarRepeticiones(numAleatorios,50);
//		System.out.print("50 numeros aleatorios: " + numAleatorios + "\n");
//		misNumeros.forEach((numero, repeticiones)-> System.out.print("El numero: " + numero + " fue repetido: " + repeticiones + " veces\n") );   
    
        System.out.println("impresoras NP");
    	for(int i = 0; i< 100;i++){   
//    		//1/f(x)=-97.165ln(-(1-x)/x)+702.87
//    		double r = Math.random();
//			int ta = (int) (-97.165*Math.log((1-r)/r)+702.87);
    		
            System.out.println(-Math.log(1-Math.random())/(0.00949));
    	}
    	
    	
        
		BigDecimal random = new BigDecimal(50);
		List<BigDecimal> numAleatorios2 = generarNumerosAleatorios(10000);
		Map<BigDecimal, Integer> misNumeros2 = cargarRepeticiones(numAleatorios2,1000);
		System.out.print("10000 numeros aleatorios: " + numAleatorios2 + "\n");
		
		misNumeros2.forEach((numero, repeticiones)-> {
		String num = String.valueOf(numero.doubleValue());
		Integer min = Integer.valueOf(num.substring(2));
		StringBuilder ia = new StringBuilder();
		
		if(min<60){
			ia.append((repeticiones*2)+"/09/2022");
			Integer hora = Integer.divideUnsigned(min, 2);
			if(hora >= 8 && hora < 22){
				ia.append(" "+hora);
			}
			else if(hora < 8){
				if((hora+12)< 8){
					ia.append(" "+(hora+8));
				}else{
					ia.append(" "+(hora+9));
				}
			}
			else if(hora >= 18){
				ia.append(" "+(hora-12));
			}
			ia.append(":"+min);
			System.out.print(ia.toString()+"\n");

		}
		});   
		
		
		
		BigDecimal masRepetido = BigDecimal.ZERO;
		int repeticionesMaximas = 0;
		List<String> numerosDentroDelRango = new LinkedList<String>();
	
		for (Map.Entry<BigDecimal, Integer> entry : misNumeros2.entrySet()) {
			if(repeticionesMaximas < entry.getValue()){
				masRepetido = entry.getKey();
				repeticionesMaximas = entry.getValue();
			}		
			if(entry.getKey().compareTo(new BigDecimal(0)) >= 0){
				if(entry.getKey().compareTo(new BigDecimal(1)) <= 0){
					
					double num = entry.getKey().doubleValue();
					int p_ent = (int) entry.getKey().intValue();
					numerosDentroDelRango.add(String.valueOf(num - p_ent).substring(2));
				}
			}
		}
//		numerosDentroDelRango.stream().forEach(System.out::println);  
//		System.out.print("Cantidad dentro del rango: " + numerosDentroDelRango.size() +"\n");

	}	
	
	public static List<BigDecimal> generarNumerosAleatorios( int cantidadDeRepeticiones){
		List<BigDecimal> numeros = new LinkedList<BigDecimal>();
		for(int i=0; i<cantidadDeRepeticiones;i++){
			double random = Math.random();
			BigDecimal aleatorio = new BigDecimal(Math.acos(1-random));
//			random = random.multiply(densidad);
			numeros.add(aleatorio.setScale(CANTIDAD_DECIMALES,RoundingMode.HALF_EVEN));
		}
		return numeros;
	}
	
	public static Map<BigDecimal, Integer> cargarRepeticiones(List<BigDecimal> numerosAleatorios, int cantidadDeRepeticiones){
		
		numerosAleatorios = numerosAleatorios.stream().sorted().collect(Collectors.toList());
		Map<BigDecimal, Integer> numerosRepetidos = new HashMap<BigDecimal, Integer>();
		for(int i=0; i<cantidadDeRepeticiones;i++){
			if(numerosRepetidos.containsKey(numerosAleatorios.get(i))){
				numerosRepetidos.put(numerosAleatorios.get(i),numerosRepetidos.get(numerosAleatorios.get(i)) + 1);
			}else{
				numerosRepetidos.put(numerosAleatorios.get(i), 1);
			}
		}
		return numerosRepetidos;

	}
	
	private static int intervaloPedido(boolean diasEspeciales) {

        // TODO calcular con la fdp -> f(R) = ln(-R+1)/(-0,0002)
        //return (int) (Math.random()*(480-120+1)+120);

        double ia = 0;

//		for(int i =0;i<500;i++){
//        if(diasEspeciales){
//            ia = Math.log(-Math.random()+1)/(-0.002);
//        }else{
//            //f(R) = 1*(-ln(-R+1))^(1/3)
//            ia = Math.pow(-Math.log(-Math.random()+1),0.333);
//       }
        
       return new BigDecimal(ia).intValue();

	}
}
