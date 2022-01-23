
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software - ARSW
## Ejercicio Introducción al paralelismo - Hilos - Caso BlackListSearch

## Desarrollado por :
   Camilo Andrés Pichimata Cárdenas

   Zuly Valentina Vargas Ramírez 	

### Descripción
  Este ejercicio contiene una introducción a la programación con hilos en Java, además de la aplicación a un caso concreto.
  

**Parte I - Introducción a Hilos en Java**

1. De acuerdo con lo revisado en las lecturas, complete las clases CountThread, para que las mismas definan el ciclo de vida de un hilo que imprima por pantalla los números entre A y B.

	Para este punto se completo la clase CountThread agregando dos atributos : numero1 y numero2, los cuales permiten definir los límites para la impresión por pantalla de los números. Se completó el constructor con los parametros enteros A y B los cuales son asignados a los atributos definidos.
	Para imprimir los números en pantalla se construyó el método run() en el cual mediante un iterador y los atributos definidos muestra los números en el rango correspondiente.

	![](img/count_thread.png)

	

	
	

2. Complete el método __main__ de la clase CountMainThreads para que:
	1. Cree 3 hilos de tipo CountThread, asignándole al primero el intervalo [0..99], al segundo [99..199], y al tercero [200..299].
	2. Inicie los tres hilos con 'start()'.
	3. Ejecute y revise la salida por pantalla. 
	4. Cambie el incio con 'start()' por 'run()'. Cómo cambia la salida?, por qué?.

Para compilar y ejecutar se emplea :
	
	maven package

	mvn exec:java -Dexec.mainClass="edu.eci.arsw.threads.CountThreadsMain"


**Método main():**

	2. start():


![](img/start.png)
	

	4. run():

![](img/run.png)


Al ejecutar el método run() en lugar de start() se ejecutan las iteraciones en orden. Como se puede observar se muestran primero los números del rango para el primer hilo, luego los del segundo y por último los números en el rango del tercer hilo. Esto sucede ya que al ejecutar run() para cada hilo, estos se ejecutan uno tras o otro secuencialmente, mientras que al usar a start()  este llama al método run() el cual no se ejecuta en un orden en especifico sino que se ejecuta en hilos separados. 


**Parte II - Ejercicio Black List Search**


Para un software de vigilancia automática de seguridad informática se está desarrollando un componente encargado de validar las direcciones IP en varios miles de listas negras (de host maliciosos) conocidas, y reportar aquellas que existan en al menos cinco de dichas listas. 

Dicho componente está diseñado de acuerdo con el siguiente diagrama, donde:

- HostBlackListsDataSourceFacade es una clase que ofrece una 'fachada' para realizar consultas en cualquiera de las N listas negras registradas (método 'isInBlacklistServer'), y que permite también hacer un reporte a una base de datos local de cuando una dirección IP se considera peligrosa. Esta clase NO ES MODIFICABLE, pero se sabe que es 'Thread-Safe'.

- HostBlackListsValidator es una clase que ofrece el método 'checkHost', el cual, a través de la clase 'HostBlackListDataSourceFacade', valida en cada una de las listas negras un host determinado. En dicho método está considerada la política de que al encontrarse un HOST en al menos cinco listas negras, el mismo será registrado como 'no confiable', o como 'confiable' en caso contrario. Adicionalmente, retornará la lista de los números de las 'listas negras' en donde se encontró registrado el HOST.

![](img/Model.png)

Al usarse el módulo, la evidencia de que se hizo el registro como 'confiable' o 'no confiable' se dá por lo mensajes de LOGs:

INFO: HOST 205.24.34.55 Reported as trustworthy

INFO: HOST 205.24.34.55 Reported as NOT trustworthy


Al programa de prueba provisto (Main), le toma sólo algunos segundos análizar y reportar la dirección provista (200.24.34.55), ya que la misma está registrada más de cinco veces en los primeros servidores, por lo que no requiere recorrerlos todos. Sin embargo, hacer la búsqueda en casos donde NO hay reportes, o donde los mismos están dispersos en las miles de listas negras, toma bastante tiempo.

Éste, como cualquier método de búsqueda, puede verse como un problema [vergonzosamente paralelo](https://en.wikipedia.org/wiki/Embarrassingly_parallel), ya que no existen dependencias entre una partición del problema y otra.

Para 'refactorizar' este código, y hacer que explote la capacidad multi-núcleo de la CPU del equipo, realice lo siguiente:

1. Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que haga la búsqueda de un segmento del conjunto de servidores disponibles. Agregue a dicha clase un método que permita 'preguntarle' a las instancias del mismo (los hilos) cuantas ocurrencias de servidores maliciosos ha encontrado o encontró.

**Creación de la clase BlackListValidator :**

![](img/blackList_thread.png)

2. Agregue al método 'checkHost' un parámetro entero N, correspondiente al número de hilos entre los que se va a realizar la búsqueda (recuerde tener en cuenta si N es par o impar!). Modifique el código de este método para que divida el espacio de búsqueda entre las N partes indicadas, y paralelice la búsqueda a través de N hilos. Haga que dicha función espere hasta que los N hilos terminen de resolver su respectivo sub-problema, agregue las ocurrencias encontradas por cada hilo a la lista que retorna el método, y entonces calcule (sumando el total de ocurrencuas encontradas por cada hilo) si el número de ocurrencias es mayor o igual a _BLACK_LIST_ALARM_COUNT_. Si se da este caso, al final se DEBE reportar el host como confiable o no confiable, y mostrar el listado con los números de las listas negras respectivas. Para lograr este comportamiento de 'espera' revise el método [join](https://docs.oracle.com/javase/tutorial/essential/concurrency/join.html) del API de concurrencia de Java. Tenga también en cuenta:

	* Dentro del método checkHost Se debe mantener el LOG que informa, antes de retornar el resultado, el número de listas negras revisadas VS. el número de listas negras total (línea 60). Se debe garantizar que dicha información sea verídica bajo el nuevo esquema de procesamiento en paralelo planteado.

	* Se sabe que el HOST 202.24.34.55 está reportado en listas negras de una forma más dispersa, y que el host 212.24.24.55 NO está en ninguna lista negra.

	
	Después de la implementación de busqueda mediante el uso de hilos y las pruebas correspondientes obtuvimos los siguientes resultados:
	 
	- **Con la IP:  202.24.34.55 y 10 Hilos**

	![](img/prueba_1.png)

	- **Con la IP: 212.24.24.55 y 12 Hilos**

	![](img/prueba_2.png)


**Parte II.I Para discutir la próxima clase (NO para implementar aún)**

La estrategia de paralelismo antes implementada es ineficiente en ciertos casos, pues la búsqueda se sigue realizando aún cuando los N hilos (en su conjunto) ya hayan encontrado el número mínimo de ocurrencias requeridas para reportar al servidor como malicioso. Cómo se podría modificar la implementación para minimizar el número de consultas en estos casos?, qué elemento nuevo traería esto al problema?

**Parte III - Evaluación de Desempeño**

A partir de lo anterior, implemente la siguiente secuencia de experimentos para realizar las validación de direcciones IP dispersas (por ejemplo 202.24.34.55), tomando los tiempos de ejecución de los mismos (asegúrese de hacerlos en la misma máquina):

1. Un solo hilo.
	![](img/1_hilo_jvm.png)
	![](img/1_hilo_tiempo.png)

2. Tantos hilos como núcleos de procesamiento (haga que el programa determine esto haciendo uso del [API Runtime](https://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html)).

	Con 8 Hilos:
	![](img/8_hilo_jvm.png)
	![](img/8_hilo_tiempo.png)

3. Tantos hilos como el doble de núcleos de procesamiento.

	Con 16 Hilos:
	![](img/16_hilo_jvm.png)
	![](img/16_hilo_tiempo.png)


4. 50 hilos.
	![](img/50_hilo_jvm.png)
	![](img/50_hilo_tiempo.png)
5. 100 hilos.
	![](img/100_hilo_jvm.png)
	![](img/100_hilo_tiempo.png)

Al iniciar el programa ejecute el monitor jVisualVM, y a medida que corran las pruebas, revise y anote el consumo de CPU y de memoria en cada caso. 

Con lo anterior, y con los tiempos de ejecución dados, haga una gráfica de tiempo de solución vs. número de hilos. Analice y plantee hipótesis con su compañero para las siguientes preguntas (puede tener en cuenta lo reportado por jVisualVM):
		![](img/grafica.png)




**Parte IV - Ejercicio Black List Search**

1. Según la [ley de Amdahls](https://www.pugetsystems.com/labs/articles/Estimating-CPU-Performance-using-Amdahls-Law-619/#WhatisAmdahlsLaw?):

	![](img/ahmdahls.png), donde _S(n)_ es el mejoramiento teórico del desempeño, _P_ la fracción paralelizable del algoritmo, y _n_ el número de hilos, a mayor _n_, mayor debería ser dicha mejora. Por qué el mejor desempeño no se logra con los 500 hilos?, cómo se compara este desempeño cuando se usan 200?. 

	El mejor desempeño no se logra con 500 hilos debido a que, como se puede ver en la gráfica, no se presentará gran diferencia en el tiempo de ejecución si se usa un número menor de hilos, si se compara con 200 hilos podríamos concluir teniendo en cuenta la tendencia que la diferencia en tiempos no representa una mejora significativa. De igual manera si el número de hilos es muy alto se empleará mucho más tiempo en la creación de cada de uno de estos hilos que el tiempo que se empleará en resolver el problema.

2. Cómo se comporta la solución usando tantos hilos de procesamiento como núcleos comparado con el resultado de usar el doble de éste?.

	Al usar 8 hilos de procesamiento se obtuvo un tiempo de solución de 22597 milisegundos, por otro lado al usar el doble de estos, 16 hilos de procesamiento, se empleó un tiempo de 11055 milisegundos, lo que nos permite observar una reducción en el tiempo de aproximadamente el 50%. Por lo tanto, en este caso es preferible usar 16 hilos en lugar de 8.   


3. De acuerdo con lo anterior, si para este problema en lugar de 100 hilos en una sola CPU se pudiera usar 1 hilo en cada una de 100 máquinas hipotéticas, la ley de Amdahls se aplicaría mejor?. Si en lugar de esto se usaran c hilos en 100/c máquinas distribuidas (siendo c es el número de núcleos de dichas máquinas), se mejoraría?. Explique su respuesta.

	Para el primer caso al emplear 1 hilo por cada una de las 100 máquinas no se presentaría una mejora debido a que la ley de Amdahls aplica sobre la mejora de desempeño de cada una de las máquinas individualmente, además de esto sería un solo hilo por cada máquina y los procesos serían independientes. 
	En el segundo caso al usar c hilos en las 100/c máquinas si existiría una mejora tal como se presento en el ejercicio realizado. 
	




