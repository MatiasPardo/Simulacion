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
		BigDecimal random = new BigDecimal(50); 
		
		List<BigDecimal> numAleatorios = generarNumerosAleatorios(random,50);
		Map<BigDecimal, Integer> misNumeros = cargarRepeticiones(numAleatorios,50);
		System.out.print("50 numeros aleatorios: " + numAleatorios + "\n");
		misNumeros.forEach((numero, repeticiones)-> System.out.print("El numero: " + numero + " fue repetido: " + repeticiones + " veces\n") );   
		
		List<BigDecimal> numAleatorios2 = generarNumerosAleatorios(random,10000);
		Map<BigDecimal, Integer> misNumeros2 = cargarRepeticiones(numAleatorios2,10000);
		System.out.print("10000 numeros aleatorios: " + numAleatorios2 + "\n");
		
		misNumeros2.forEach((numero, repeticiones)-> {	 
		String num = String.valueOf(numero.doubleValue());
		Integer min = Integer.valueOf(num.substring(2));
		StringBuilder ia = new StringBuilder();
		
		if(min<60){
			ia.append( (repeticiones * 2)+"/08/2022");
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
		List<BigDecimal> numerosDentroDelRango = new LinkedList<BigDecimal>();
	
		for (Map.Entry<BigDecimal, Integer> entry : misNumeros2.entrySet()) {
			if(repeticionesMaximas < entry.getValue()){
				masRepetido = entry.getKey();
				repeticionesMaximas = entry.getValue();
			}		
			if(entry.getKey().compareTo(new BigDecimal(0)) >= 0){
				if(entry.getKey().compareTo(new BigDecimal(1)) <= 0){
					numerosDentroDelRango.add(entry.getKey());
				}
			}
		}
		
		System.out.print("Numero dentro del rango: " + numerosDentroDelRango +"\n");
		System.out.print("Cantidad dentro del rango: " + numerosDentroDelRango.size() +"\n");

	}	
	
	public static List<BigDecimal> generarNumerosAleatorios(BigDecimal densidad, int cantidadDeRepeticiones){
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
}
