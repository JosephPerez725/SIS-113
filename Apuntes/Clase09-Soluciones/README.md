# Clase 9 — Soluciones

## Taller de Cine: pedido y pago ([cine/](cine/))

Las cinco clases de entradas son las de la Clase 8, sin cambios. Se agregan `Order`, `PaymentMethod`, `CashPayment`, `CardPayment` y `QrPayment`.

```bash
cd cine && javac -d out *.java && java -cp out Main
```

```
== Receipt
Regular | Dune: Part Three | seat F7 | Bs 50.0
Student | Dune: Part Three | seat F8 | Bs 35.0
    student id: UCB-2026-114
Senior | Dune: Part Three | seat F9 | Bs 30.0
Premium | Dune: Part Three | seat A1 | Bs 90.0 (with snack)
Subtotal: Bs 205.0

== Payment options
Cash: Bs 205.0
Card: Bs 209.1
QR (BNB): Bs 205.0
```

## Puente a Sentinel ([sentinel/](sentinel/))

Sobre la copia de `oop/` con las reglas de la Clase 8, reemplazar `Main.java` y `Console.java`.

```bash
java -cp out Main 80 60 < ../../phase1-bash/samples/muestra-2026-09-17.txt
```

Cambios esperados respecto a la versión de una sola regla:

- El encabezado lista las reglas: `rules: memory >= 80% cpu >= 60%`.
- La línea de las `11:00:06` pasa a `[ALERT ]`: la memoria está en 75 %, pero la CPU en 61.9 %.
- El resumen cierra con `alerts memory: 3` y `alerts cpu: 4`.

La trampa para discutir: si el bucle se escribe `alert = alert || rule.evaluate(reading)`, Java **no evalúa** la segunda regla cuando la primera ya dio `true`. Ninguna alerta se pierde en pantalla, pero el contador de la CPU se queda corto. Con la muestra, `alerts cpu` baja de 4 a 1.

## Ejercicios ([../Clase09-Ejercicios/](../Clase09-Ejercicios/))

Las versiones corregidas están en [ejercicios/](ejercicios/), con los mismos nombres de archivo. Cada una cambia solo lo necesario, marcado con un comentario `FIX`, y imprime la salida esperada del drill:

```bash
cd ejercicios && java Drill1InstanceofChain.java
```

Para el Drill 1 hay además [Drill1Interface.java](ejercicios/Drill1Interface.java): `Entry` como interfaz. Con el cajero ya corregido, agrega una garantía: un tipo nuevo que se olvida del precio no compila. El costo es que la entrada de adulto necesita su propia clase. La interfaz sola no arregla el bug: si el cajero sigue con la cadena de `instanceof`, cobra 155 igual.

| Drill | Qué pasa | Causa | Corrección |
|---|---|---|---|
| 1 | `total: Bs 155.0` en lugar de 120 | La cadena de `instanceof` decide precios que **cada clase ya sabe**. `KidEntry` llegó después y cae en el `else`. | Borrar la cadena entera: `total += entry.getPrice();`. Una clase nueva funciona sin tocar al cajero. |
| 2 | `ClassCastException: class GuestBadge cannot be cast to class StaffBadge` | El cast le promete al compilador que **todos** son `StaffBadge`. En ejecución, el segundo no lo es. | `if (badge instanceof StaffBadge staff)` antes de usar `getStaffId()`, y en el `else` imprimir solo el nombre. |
| 3 | No compila: *cannot find symbol: method getMessage(), location: variable voucher of type Voucher* | El compilador mira el **tipo declarado**, `Voucher`, que no tiene ese método. No importa que el objeto real sí lo tenga. | Declarar `GiftVoucher voucher = ...`, porque aquí se necesita lo específico. |
| 4 | No compila: *CryptoOption is not abstract and does not override abstract method fee(double)* | `implements` es una promesa: todos los métodos de la interfaz. | `@Override public double fee(double amount) { return amount * 2.5 / 100; }` |
| 5 | `night: Bs 50.0` | `getPrice(int)` tiene otros parámetros: es una **sobrecarga**, no una sobrescritura. `night.getPrice()` llama al del padre. | `@Override double getPrice() { return 35; }`. Poner `@Override` sobre `getPrice(int)` habría dado error de compilación. |

## Tarea: Control de Estacionamiento

Criterio clave al corregir: **ningún `instanceof` en `ParkingLot`**. Si aparece uno para calcular la tarifa, la jerarquía no se está usando.
