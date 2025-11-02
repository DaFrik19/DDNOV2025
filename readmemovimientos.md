# Un pequeño módulo sobre caracteristicas.

## DESCRIPCIÓN DEL DESARROLLO

Se ha implementado el módulo completo de movimientos (transacciones) bancarios de AmerikeBank y que constituyeron el
desarrollo de un sistema robusto que gestiona esas operaciones financieras básicas de depósitos, retiros y
transferencias. Se basa en una arquitectura en tres capas perfectamente definidas y claramente independientes entre sí.
En la capa de modelo se creó la clase Movimiento que representa cada una de las operaciones bancarias, utilizando tipos
de datos específicos para garantizar la precisión de los cálculos monetarios mediante BigDecimal y para el correcto
tratamiento de fechas con LocalDateTime, etc. También se creó un enumerador TipoMovimiento con los tres tipos de
operación que el sistema debe soportar.

Para el acceso a datos se desarrolló el patrón Repository con una interfaz que define todas las operaciones necesarias
contra la base de datos y una implementación concreta que utiliza JDBC para conectarse con MySQL. Esta capa se encarga
de mapear los resultados de las consultas SQL a objetos Java y ejecutar las inserciones y actualizaciones requeridas.

La capa de servicio contiene toda la lógica de negocio del módulo, coordinando las validaciones necesarias antes de
cada operación y orquestando el flujo completo de cada transacción. Implementa validaciones de montos, verificación de
saldos suficientes y actualización automática de saldos después de cada movimiento.

## FUNCIONALIDADES IMPLEMENTADAS

Se realizaron depósitos que serían los que alimentan las cuentas, los cuales están validados a través de que el monto
sea positivo y no sobrepase los límites fijados en el sistema. Para los retiros, se comprueba prima facie que el saldo
sea suficiente para la operación; por lo cual se garantiza el lado de la integridad financiera y gestión del riesgo en
situaciones de sobre-apalancamiento, en caso de ser necesario.

El módulo también permite la transferencia entre cuentas tal intermedio entre validación de saldo en cuenta origen a
la vez que guardamos un historial de aquellas cuentas que tiene que ver con la transferencia, etcétera, etcétera.
Además de las operaciones en sí, el sistema ofrece funcionalidades de consulta que permiten obtener el histórico de
movimientos de tal forma que pueden ser filtrados por cuenta concreta, por cliente con todas sus cuentas o rangos de
fechas. También se implementó la consulta de saldo actual y preparar estados de cuenta que presenten la información
relacionada con la actividad financiera del cliente, organizada por el mismo.

Con el tipo de fechas se implementó el uso de LocalDateTime que da una forma más moderna y más segura que las clases
que proporcionaba Date.  A la hora de utilizar BigDecimal para toda aquella operación relacionada con la actividad
monetaria y de este modo los errores de redondeo que se producen con los tipos de punto flotante no son aplicables como 
solución rápida.