# Clase 9

**SIS-113 Programación II**

> Unidad 3, Interfaces y Polimorfismo (semanas 11 y 13 del plan). Continúa el cine de la [Clase 8](Clase08-Guia.md): el mismo código, ahora con pedidos de muchas entradas y varias formas de pago. Al final, Sentinel aprende a usar varias reglas a la vez.

---

## Objetivo de la sesión

Al terminar la clase, cada estudiante:

1. Recorre una `List<Ticket>` con entradas de **distintos tipos** y explica por qué cada una calcula su propio precio
2. Distingue el **tipo declarado** del **tipo real**: qué decide el compilador y qué decide la ejecución
3. Escribe una **interfaz** propia y tres clases que la implementan
4. Decide entre **interfaz** y **clase abstracta** con un criterio claro
5. Reconoce los síntomas de polimorfismo mal usado: cadenas de `instanceof`, casts que fallan y jerarquías que explotan
6. Hace que Sentinel evalúe varias reglas con un solo bucle

---

## Reglas de la sesión

- **Sin IA** para escribir las clases. Verde solo para entender un mensaje del compilador.
- Todo se compila desde la terminal: `javac -d out *.java && java -cp out Main`.
- **Prohibido el `instanceof` para decidir un comportamiento.** Si aparece, se pregunta qué método falta en la clase.

---

## Desarrollo

### Parte 1 — Repaso y el problema (15 min)

Dos estudiantes al azar muestran su jerarquía de salas de la tarea y aplican la prueba del "es un" en voz alta.

Después, el problema que quedó planteado: el `Main` de la Clase 8 sumó el total con cuatro variables. ¿Y con siete entradas? La respuesta típica, y muy común en código generado por IA, es esta:

```bash
cd Apuntes/Clase09-Ejercicios
java Drill1InstanceofChain.java
```

Un cajero que pregunta "¿eres niño? ¿eres adulto mayor?" con `instanceof` y pone el precio él mismo. Imprime `total: Bs 155.0`, cuando debería ser 120. Alguien agregó `KidEntry` la semana pasada, y el cajero no se enteró: le cobra el precio completo.

Pregunta: ¿quién sabe el precio de una entrada? **La entrada misma.** Entonces, ¿por qué el cajero lo está decidiendo?

### Parte 2 — Polimorfismo: una lista, muchas formas (35 min)

Con las clases de la Clase 8, sin cambiarlas:

```java
List<Ticket> tickets = new ArrayList<>();
tickets.add(new Ticket("Dune: Part Three", "F7", 50));
tickets.add(new StudentTicket("Dune: Part Three", "F8", 50, "UCB-2026-114"));
tickets.add(new SeniorTicket("Dune: Part Three", "F9", 50, 67));
tickets.add(new PremiumTicket("Dune: Part Three", "A1", 50, true));

double total = 0;
for (Ticket ticket : tickets) {
    total += ticket.getPrice();       // la misma línea, cuatro comportamientos
}
```

La lista dice `Ticket`, pero cada objeto **sigue siendo lo que es**. `ticket.getPrice()` ejecuta la versión del objeto real: 50, 35, 30 o 90. Eso es **polimorfismo**: una misma llamada, muchas formas.

La frase que hay que poder repetir:

> **El compilador mira el tipo declarado** para decidir qué métodos se pueden llamar.
> **La ejecución mira el objeto real** para decidir qué versión corre.

| | Tipo declarado | Tipo real |
|---|---|---|
| `Ticket t = new StudentTicket(...)` | `Ticket` | `StudentTicket` |
| ¿Quién lo decide? | quien escribe el código | quien crea el objeto con `new` |
| ¿Para qué sirve? | decide qué se puede **llamar** | decide qué versión se **ejecuta** |

Dos demostraciones, con los drills:

- **Drill 3**, `java Drill3DeclaredType.java`: no compila. El objeto es un `GiftVoucher` y tiene `getMessage()`, pero la variable dice `Voucher`, y `Voucher` no tiene ese método. Manda el tipo declarado.
- **Drill 5**, `java Drill5OverloadNotOverride.java`: la tarifa nocturna nunca se aplica. `getPrice(int)` tiene otros parámetros, así que es una **sobrecarga** y no una sobrescritura. Volver a la diferencia de la Clase 7, ahora con la consecuencia a la vista.

Detalle que vale la pena señalar: el `toString()` de la Clase 8 **ya era polimórfico**. `Ticket.toString()` llama a `getType()` y `getPrice()`, y cada hijo responde con lo suyo.

### Parte 3 — Interfaces: un contrato sin código (35 min)

El cine necesita aceptar pagos en efectivo, con tarjeta (2 % de recargo) y con QR. Las tres formas de pago **no comparten ningún dato**: solo comparten lo que **saben hacer**. Eso es una interfaz.

```java
public interface PaymentMethod {
    String getName();
    double fee(double amount);
}

public class CardPayment implements PaymentMethod {
    @Override
    public String getName() { return "Card"; }

    @Override
    public double fee(double amount) { return amount * 2 / 100; }
}
```

Lo que hay que decir:

- **`interface`**: solo firmas, sin atributos de instancia ni constructor. No se puede hacer `new PaymentMethod()`.
- **`implements`**: una promesa. La clase debe escribir **todos** los métodos de la interfaz. El Drill 4 muestra qué dice el compilador cuando falta uno.
- **Una clase hereda de una sola clase, pero puede implementar muchas interfaces.**

Cuándo usar cada una:

| | Clase abstracta | Interfaz |
|---|---|---|
| Comparte **datos y código** | sí: `Ticket` guarda película, asiento y precio | no |
| Comparte **qué se puede hacer** | sí | sí: `PaymentMethod` |
| Cuántas por clase | una sola | varias |
| En el cine | `Ticket`, `DiscountTicket` | `PaymentMethod` |
| En Sentinel | `Rule` | el contrato JSON, si fuera código |

Regla práctica: **si las clases comparten estado, clase abstracta. Si solo comparten un comportamiento, interfaz.**

Puente con el curso: el contrato JSON de Sentinel **es** una interfaz, solo que entre programas: `collector.sh` y Java no comparten código, solo el formato de la línea.

### Parte 4 — Taller: el pedido y el pago (40 min)

Sobre la copia del cine de la Clase 8, dos piezas nuevas.

**`Order`**, la composición: un pedido **tiene** entradas.

- `private final List<Ticket> tickets = new ArrayList<>();`
- `add(Ticket ticket)`: acepta **cualquier** tipo de entrada.
- `getSubtotal()`: un solo `for` con `ticket.getPrice()`. Sin `instanceof`.
- `totalWith(PaymentMethod method)`: subtotal más el recargo. Funciona con cualquier forma de pago, incluso una que todavía no existe.
- `getTickets()`: una **copia** de la lista, como en la Clase 7.

**Las formas de pago:** `CashPayment`, `CardPayment` y `QrPayment`. `QrPayment` recibe el banco en el constructor y su nombre es `QR (BNB)`.

**`Main`:** el recibo y las tres opciones de pago.

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

La línea del carnet es el uso **correcto** de `instanceof`: no decide un precio, solo muestra un dato que solo los estudiantes tienen. Con *pattern matching*, el cast va incluido:

```java
if (ticket instanceof StudentTicket student) {
    System.out.println("    student id: " + student.getStudentId());
}
```

**Prueba final del taller:** crear una clase nueva `KidTicket` con 60 % de descuento, agregarla al pedido y correr. `Order` no se toca. Comparar con el cajero del Drill 1.

### Parte 5 — Lo malo: cuándo el polimorfismo se rompe (20 min)

Tres síntomas, cada uno con su ejemplo:

1. **La cadena de `instanceof`** que decide comportamiento. Es el Drill 1: cada clase nueva obliga a buscar y actualizar todas las cadenas, y la que se olvida falla en silencio.
2. **El cast forzado.** `java Drill2BadDowncast.java` se cae con `ClassCastException` en el segundo elemento. El cast le prometió al compilador algo que en ejecución no era cierto.
3. **La explosión de la jerarquía.** ¿Y si un estudiante quiere un asiento premium? ¿`StudentPremiumTicket`? ¿Y `SeniorPremiumTicket`? Con 3 descuentos y 3 tipos de asiento salen 9 clases. La herencia combina mal. La salida es la composición: el descuento es una cosa, el asiento es otra, y la entrada **tiene** uno de cada uno.

Y un exceso del lado contrario: **una interfaz con una sola implementación**, "por si acaso". Si no hay una segunda forma, la interfaz solo agrega un archivo más que leer.

### Parte 6 — Puente a Sentinel: muchas reglas, un bucle (30 min)

En Sentinel **solo se tocan `Main` y `Console`**, sobre la copia que ya tiene `Rule`, `MemoryRule` y `CpuRule` de la Clase 8.

El problema: `Console.summary` pide un `MemoryRule`. No acepta un `CpuRule`, aunque los dos sean reglas.

**Paso 1 — `Main` con una lista de reglas:**

```java
List<Rule> rules = new ArrayList<>();
rules.add(new MemoryRule(memoryThreshold));    // args[0], por defecto 80
rules.add(new CpuRule(cpuThreshold));          // args[1], por defecto 60
```

**Paso 2 — un solo bucle** para todas las reglas:

```java
boolean alert = false;
for (Rule rule : rules) {
    if (rule.evaluate(reading)) {
        alert = true;
    }
}
console.show(reading, alert);
```

**Paso 3 — `Console` recibe `List<Rule>`** en `header` y `summary`, y el resumen imprime una línea por regla con `getName()` y `getHits()`.

```bash
java -cp out Main 80 60 < ../../phase1-bash/samples/muestra-2026-09-17.txt
```

Lo que cambia frente a la versión de una regla:

- La línea de las `11:00:06` pasa a `[ALERT ]`: la memoria está en 75 %, pero la CPU en 61.9 %.
- El resumen cierra con `alerts memory: 3` y `alerts cpu: 4`.

**La trampa, para discutir:** escribir el bucle como `alert = alert || rule.evaluate(reading)` parece equivalente. No lo es: cuando la memoria ya alertó, Java **no evalúa** la regla de CPU, y su contador baja de 4 a 1. Probarlo en vivo.

Mañana, una regla nueva es una clase nueva y una línea en la lista. `Console` y el bucle no se tocan.

---

## Verificación de salida

Antes de irse, cada estudiante:

- [ ] Tiene el pedido con las tres formas de pago, con la salida de la Parte 4
- [ ] Agrega `KidTicket` sin tocar `Order`
- [ ] Explica la diferencia entre tipo declarado y tipo real con un ejemplo propio (pregunta oral al azar)
- [ ] Dice cuándo usaría una interfaz y cuándo una clase abstracta

---

## Tarea para la casa

En `bitacora-terminal/java/clase09/`, con commits por parte.

### Parte 1 — Los cinco drills

Los programas de [Clase09-Ejercicios/](Clase09-Ejercicios/). En `investigacion/drills-clase09.md`, la misma tabla de la Clase 8: qué pasa, por qué y qué cambiaste.

### Parte 2 — Control de Estacionamiento

El ejercicio del plan, con todo lo de estas dos clases:

1. `Vehicle`, abstracta: placa validada y un método abstracto `getHourlyRate()`.
2. `Car` a 5 Bs la hora, `Motorcycle` a 2 Bs y `Truck` a 10 Bs.
3. `ParkingLot` **tiene** una `List<Vehicle>`: `enter(Vehicle)`, `exit(String plate)` y `charge(String plate, int hours, PaymentMethod method)`. Reusar `PaymentMethod` del cine.
4. Regla: **ningún `instanceof` en `ParkingLot`**.
5. Agregar un tipo nuevo, `ElectricCar` a 3 Bs la hora, y verificar que `ParkingLot` no cambia. Escribir en `investigacion/parking.md` cuántos archivos tocaste para agregarlo.

### Parte 3 — Sentinel

1. En la copia propia: `Main` y `Console` con `List<Rule>`, y la salida con `80 60` en `investigacion/rules.txt`.
2. Probar la trampa del `||` y pegar las dos salidas del resumen, con una frase que explique la diferencia.

### Entrega

- Link al repositorio actualizado, antes de la próxima clase.
- Commits por parte; un solo commit final es una tarea incompleta.

> **Regla de IA:** verde para entender un mensaje del compilador. Las clases se escriben a mano. En la próxima clase, cualquier estudiante puede ser invitado a agregar una forma de pago nueva en vivo.

---

## Referencias y práctica interactiva

*(contenido en inglés — leer documentación técnica en inglés es parte del oficio)*

### Documentación y lectura

| Recurso | Descripción |
|---------|-------------|
| [Oracle Tutorial — Polymorphism](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html) | Una página: una variable del tipo padre, distintos comportamientos. |
| [Oracle Tutorial — Interfaces](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html) | Declarar e implementar una interfaz. La referencia de la Parte 3. |
| [Oracle Tutorial — Abstract Methods and Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html) | Incluye la comparación con interfaces de la tabla de la Parte 3. |
| [Oracle — Pattern Matching for instanceof](https://docs.oracle.com/en/java/javase/21/language/pattern-matching-instanceof.html) | El `instanceof` con variable incluida, y cuándo usarlo. |

### Ejercicios interactivos

| Recurso | Descripción |
|---------|-------------|
| [Java Programming MOOC — Part 9](https://java-programming.mooc.fi/part-9) | Helsinki: interfaces y polimorfismo con ejercicios autocorregidos. |
| [Exercism — Java track](https://exercism.org/tracks/java) | Los ejercicios de los conceptos *interfaces* y *inheritance*. |
