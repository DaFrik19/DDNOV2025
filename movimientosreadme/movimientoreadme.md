# MÓDULO DE MOVIMIENTOS BANCARIOS

## DESCRIPCIÓN DEL DESARROLLO
Se realizó la implementación del módulo completo de movimientos bancarios para AmerikeBank, llevando así
a cabo un sistema capaz de gestionar las operaciones bancarias esenciales de depósitos, retiros y
transferencias. El desarrollo se realizó siguiendo una buena arquitectura en tres capas bien definidas
que segregaba las responsabilidades del sistema.

En la capa de modelo se implementó la clase Movimiento. Dicha clase sirve para representar cada
operación bancaria, utilizando tipos de datos adecuados para asegurar la precisión de los cálculos
monetarios con BigDecimal y el correcto manejo de fechas a través de LocalDateTime. Se definió un
enumerador TipoMovimiento, con los tres tipos de operaciones que puede llegar a manejar el sistema.

En el acceso a datos se implementó el patrón Repository. Para ello se definieron una interfaz que
define todas las operaciones necesarias contra la base de datos, así como una implementación concreta
que utiliza JDBC para conectarse con MySQL. Esta capa se encarga de mapear los resultados de las
consultas SQL a objetos Java y ejecutar las inserciones y actualizaciones que se tienen que hacer.
La capa de servicio contiene toda la lógica del negocio del módulo. Se encarga de realizar todas las
validaciones que tiene que hacer cada operación y orquesta el flujo completo de cada transacción, además
implementa las validaciones relativas a los montos, la verificación de saldos suficientes así como la
actualización automática de saldos tras cada movimiento.

## FUNCIONALIDADES IMPLEMENTADAS
El módulo permite la realización de depósitos que acrediten fondos entre cuentas, comprobando que los
montos sean válidos; utilizando una expresión generativa que los limiten en el sentido que los fondos
sean positivos y no excedan los límites superiores. En los retiros, el sistema comprueba que el saldo sea
suficiente para llevar a cabo la operación, manteniendo así la corrección del saldo; es decir, evitando
así la "soberanía" en las operaciones, que puede llevar a debilitar el equilibrio financiero. Las
transferencias entre cuentas realizan la verificación del saldo en la cuenta origen y el detalle del
movimiento en ambas cuentas como consecuencia de llevar a cabo la transferencia.

Pero, además de las operaciones puramente relacionadas con el manejo de cuentas, se habilitaron
capacidades de consulta que permiten acceder a la información del historial de movimientos filtrando cada
cuenta, por cliente con todos sus saldos y en rangos de fechas. Le sumamos el poder consultar los saldos
actuales, permite incluso la generación de los estados de cuenta que muestran ordenadamente la información
financiera al usuario.

La primera es la posibilidad de manejar los datos cuidando la precisión de poder llevar a cabo operaciones
financieras, mediante el uso de BigDecimal. Por lo que se emplean prácticas de trabajo que evitan los
problemas de redondeo de los tipos de punto flotante. En lo que respecta las fechas se ha implementado
utilizando LocalDateTime los que nos permiten un tratamiento más seguro y moderno que las hídricamente
clases Date.
