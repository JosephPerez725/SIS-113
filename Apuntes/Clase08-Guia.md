# Clase 8

**SIS-113 Programación II**

> Unidad 3, Herencia (semanas 9 y 10 del plan). Se trabaja primero con un ejemplo simple, entradas de cine, y al final se lleva lo aprendido a Sentinel tocando **solo las reglas**: ni el JSON ni `/proc` entran hoy.

---

## Objetivo de la sesión

Al terminar la clase, cada estudiante:

1. Explica qué problema resuelve la herencia: el código **duplicado** entre clases parecidas
2. Escribe una jerarquía con `extends`, llama al constructor del padre con `super(...)` y sobrescribe métodos con `@Override`
3. Distingue `private` de `protected`: qué ve un hijo y qué no
4. Usa una clase **abstracta** y un método **abstracto** cuando el padre no debe existir solo
5. Aplica la prueba del **"es un"** y reconoce cuándo heredar es un error
6. Reemplaza las reglas copiadas de Sentinel por una jerarquía, sin cambiar la salida del programa

---

## Reglas de la sesión

- **Sin IA** para escribir las clases. Verde solo para entender un mensaje del compilador.
- Todo se compila desde la terminal: `javac -d out *.java && java -cp out Main`.
- Antes de escribir `extends`, se dice en voz alta la frase "un ___ **es un** ___". Si suena mal, no se hereda.

---

## Desarrollo

### Parte 1 — El problema: tres clases casi iguales (10 min)

En la pizarra, tres entradas escritas por separado, como las escribiría alguien que todavía no conoce la herencia:

```java
public class RegularTicket {
    private final String movie;
    private final String seat;
    private final double basePrice;
    // constructor con las 3 validaciones, getMovie(), getSeat(), getPrice()...
}

public class StudentTicket {
    private final String movie;          // otra vez
    private final String seat;           // otra vez
    private final double basePrice;      // otra vez
    private final String studentId;
    // constructor con las MISMAS 3 validaciones + 1, los mismos getters...
}

public class SeniorTicket { /* ...y otra vez */ }
```

Preguntar: si mañana el asiento tiene que validar el formato `F7`, ¿en cuántos lugares hay que cambiarlo? ¿Y si alguien se olvida de uno? Es el mismo dolor del jueves de Clase 5, ahora entre clases en lugar de dentro de un `main`.

### Parte 2 — `extends`, `super` y `@Override` (25 min)

Se construye en vivo, primero solo con dos clases: `Ticket` y `StudentTicket`.

```java
public class Ticket {
    private final String movie;
    private final String seat;
    private final double basePrice;

    public Ticket(String movie, String seat, double basePrice) {
        // las validaciones, UNA vez
        this.movie = movie;
        this.seat = seat;
        this.basePrice = basePrice;
    }

    protected double getBasePrice() { return basePrice; }
    public double getPrice() { return basePrice; }
    public String getType() { return "Regular"; }
}

public class StudentTicket extends Ticket {
    private final String studentId;

    public StudentTicket(String movie, String seat, double basePrice, String studentId) {
        super(movie, seat, basePrice);       // primero, el padre se construye
        this.studentId = studentId;          // después, lo propio del hijo
    }

    @Override
    public double getPrice() { return getBasePrice() * 0.7; }

    @Override
    public String getType() { return "Student"; }
}
```

Lo que hay que decir explícitamente, uno por uno:

| Idea | En el código |
|---|---|
| **`extends`**: el hijo recibe todo lo del padre | `StudentTicket` tiene `getMovie()` sin escribirlo |
| **`super(...)`** construye la parte del padre, y va en la **primera línea** | `super(movie, seat, basePrice);` |
| **`private` es privado incluso para los hijos** | `StudentTicket` no puede leer `basePrice` directamente |
| **`protected`**: visible para los hijos, no para el resto | `getBasePrice()` |
| **`@Override`**: "reemplazo un método que ya existe en el padre" | `getPrice()`, `getType()` |
| **`super.metodo()`**: usar la versión del padre y agregar algo | `super.toString() + " (with snack)"` |

Demostración: quitar `super(...)` y compilar. El error dice que `Ticket` no tiene un constructor vacío. Es el Drill 1 de la tarea.

### Parte 3 — Taller: el cine, en dos niveles (40 min)

La jerarquía completa, que se termina en clase:

```
              Ticket                      precio base: 50 Bs
             /      \
  DiscountTicket    PremiumTicket         premium: +50 %, snack opcional de 15 Bs
     /        \
StudentTicket  SeniorTicket               estudiante: -30 % · adulto mayor: -40 %
```

**Paso 1 — `Ticket`.** Ya hecho en la Parte 2. Agregar un `toString()` que use `getType()` y `getPrice()`:

```java
@Override
public String toString() {
    return getType() + " | " + movie + " | seat " + seat + " | Bs " + getPrice();
}
```

**Paso 2 — `DiscountTicket`, abstracta.** Estudiantes y adultos mayores comparten **cómo** se aplica un descuento; solo cambia **cuánto**. Nadie compra "una entrada con descuento" a secas: por eso la clase es `abstract`.

```java
public abstract class DiscountTicket extends Ticket {

    protected DiscountTicket(String movie, String seat, double basePrice) {
        super(movie, seat, basePrice);
    }

    protected abstract int getDiscountPercent();      // cada hijo dice cuánto

    @Override
    public double getPrice() {                         // el padre dice cómo
        return getBasePrice() * (100 - getDiscountPercent()) / 100.0;
    }
}
```

Probar `new DiscountTicket(...)`: no compila, *DiscountTicket is abstract; cannot be instantiated*.

**Paso 3 — `StudentTicket` y `SeniorTicket`.** Ahora heredan de `DiscountTicket`. Cada una valida lo suyo: el carnet no vacío, la edad de 60 o más. Cada una devuelve su porcentaje y su tipo.

**Paso 4 — `PremiumTicket`.** Hereda directo de `Ticket`. Constructor con `boolean withSnack`. Su `toString()` usa `super.toString()` y agrega `" (with snack)"`.

**Paso 5 — `Main`.** Cuatro entradas para la misma película, impresas una por una, y el total sumado a mano:

```
Regular | Dune: Part Three | seat F7 | Bs 50.0
Student | Dune: Part Three | seat F8 | Bs 35.0
Senior | Dune: Part Three | seat F9 | Bs 30.0
Premium | Dune: Part Three | seat A1 | Bs 90.0 (with snack)
Total: Bs 205.0
```

Dejar planteada la pregunta para el sábado: el total se sumó con cuatro variables. ¿Y si el cliente compra siete entradas?

### Parte 4 — Cuándo NO heredar (15 min)

La prueba del **"es un"**: "una entrada de estudiante **es una** entrada" suena bien. Ahora:

```bash
cd Apuntes/Clase08-Ejercicios
java Drill5WrongIsA.java
```

Imprime `Large popcorn | seat none | Bs 20.0`. Compila, corre, no falla. Pero las pipocas tuvieron que **inventar un asiento** para poder heredar. "Unas pipocas **son una** entrada" suena mal, y el código lo delata.

| Relación | Frase | En Java | Ejemplo |
|---|---|---|---|
| **Herencia** | "es un" | `extends` | `StudentTicket` es un `Ticket` |
| **Composición** | "tiene un" | un atributo | una entrada **tiene** un snack; `Library` **tiene** libros (Clase 7) |

Regla práctica para llevarse: **si dudas, compón**. La herencia es la relación más fuerte entre dos clases: el hijo depende de cada decisión del padre.

### Parte 5 — Puente a Sentinel: una familia de reglas (25 min)

En Sentinel **solo se tocan las reglas**. El JSON, `Contract`, `History` y `Console` no se abren.

En la Clase 6, quien hizo la Misión A copió `MemoryRule` para crear `CpuRule`: el mismo umbral, la misma validación y el mismo contador, dos veces. Es exactamente el problema de la Parte 1.

La solución tiene la misma forma que `DiscountTicket`: el padre guarda lo común, y cada hijo dice solo **qué** cuenta como alerta.

```java
public abstract class Rule {
    private final String name;
    private final int threshold;
    private int hits = 0;

    protected Rule(String name, int threshold) { /* la validación 1..100, UNA vez */ }

    public final boolean evaluate(Snapshot reading) {    // final: ningún hijo cambia cómo se cuentan las alertas
        boolean alert = matches(reading);
        if (alert) {
            hits++;
        }
        return alert;
    }

    protected abstract boolean matches(Snapshot reading); // lo único que escribe cada hijo

    public String getName() { return name; }
    public int getThreshold() { return threshold; }
    public int getHits() { return hits; }
}

public class MemoryRule extends Rule {
    public MemoryRule(int threshold) {
        super("memory", threshold);
    }

    @Override
    protected boolean matches(Snapshot reading) {
        return reading.getMemoryPercent() >= getThreshold();
    }
}
```

`CpuRule` son seis líneas: cambia `"memory"` por `"cpu"` y `getMemoryPercent()` por `getCpuPct()`.

**La prueba**, en una copia de `sentinel/phase2-java/oop/`:

```bash
javac -d out *.java && java -cp out Main 80 < ../../phase1-bash/samples/muestra-2026-09-17.txt > antes.txt
# reemplazar MemoryRule.java, agregar Rule.java y CpuRule.java
javac -d out *.java && java -cp out Main 80 < ../../phase1-bash/samples/muestra-2026-09-17.txt > despues.txt
diff antes.txt despues.txt     # sin diferencias
```

`Main` no cambió porque `MemoryRule` mantiene el mismo constructor y los mismos métodos públicos.

Pregunta para el sábado: `Main` todavía solo usa la regla de memoria. ¿Cómo hacemos que use las dos sin escribir un `if` por cada tipo de regla?

---

## Verificación de salida

Antes de irse, cada estudiante:

- [ ] Tiene el cine compilando, con la salida de la Parte 3
- [ ] Explica por qué `super(...)` va en la primera línea y qué pasa si falta (pregunta oral al azar)
- [ ] Explica por qué `DiscountTicket` es abstracta
- [ ] Aplica la prueba del "es un" a un par de clases que le dé el docente

---

## Tarea para la casa

En `bitacora-terminal/java/clase08/`, con commits por parte.

### Parte 1 — Los cinco drills

Los programas de [Clase08-Ejercicios/](Clase08-Ejercicios/). Cada uno se ejecuta con `java DrillN....java`. En `investigacion/drills-clase08.md`, una fila por drill:

| Drill | Qué pasa (copia el mensaje o la salida) | Por qué, en una frase | Qué cambiaste |
|---|---|---|---|

El Drill 5 no tiene error de compilación: la respuesta es una decisión de diseño.

### Parte 2 — Reservas de Cine: las salas

El plan pide **al menos dos niveles de herencia**. Agregar al cine una jerarquía de salas:

```
Room                 sala normal: recargo 0 Bs
 └── Room3D          sala 3D: recargo 10 Bs, entrega lentes
      └── ImaxRoom   sala IMAX: recargo 25 Bs
```

1. `Room` con nombre y capacidad, validados. `getSurcharge()` y `getType()` que los hijos sobrescriben.
2. `ImaxRoom` hereda de `Room3D`: una sala IMAX **es una** sala 3D. Su constructor llama a `super(...)`.
3. Una clase `Screening`, la función: **tiene** una película, una sala y una hora. `describe()` imprime `Dune: Part Three · IMAX · 19:30 · recargo Bs 25.0`.
4. `Main` con tres funciones, una por tipo de sala.

En una frase, en `investigacion/cine.md`: ¿por qué `Screening` **tiene** una `Room` en lugar de heredar de ella?

### Parte 3 — Sentinel

1. En la copia propia de `oop/`: `Rule`, `MemoryRule` y `CpuRule`, con el `diff` vacío como evidencia en `investigacion/rule-diff.txt`.
2. Intentar escribir `LoadRule`, que alerte si `load1` es 2.0 o más. En `investigacion/loadrule.md`, explicar qué impide `Rule` y qué cambiarías. No hay una única respuesta correcta; se evalúa el razonamiento.

### Entrega

- Link al repositorio actualizado, antes del sábado.
- Commits por parte; un solo commit final es una tarea incompleta.

> **Regla de IA:** verde para entender un mensaje del compilador. Las clases se escriben a mano. El sábado, cualquier estudiante puede ser invitado a agregar un tipo de sala nueva en vivo.

---

## Referencias y práctica interactiva

*(contenido en inglés — leer documentación técnica en inglés es parte del oficio)*

### Documentación y lectura

| Recurso | Descripción |
|---------|-------------|
| [Oracle Tutorial — Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html) | `extends`, qué se hereda y qué no. La referencia de la Parte 2. |
| [Oracle Tutorial — Using the Keyword super](https://docs.oracle.com/javase/tutorial/java/IandI/super.html) | `super(...)` en constructores y `super.metodo()`. |
| [Oracle Tutorial — Overriding and Hiding Methods](https://docs.oracle.com/javase/tutorial/java/IandI/override.html) | Las reglas exactas de `@Override`. |
| [Oracle Tutorial — Abstract Methods and Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html) | Cuándo una clase debe ser abstracta. |
| [Oracle Tutorial — Controlling Access to Members](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html) | La tabla de `private`, `protected` y `public`. |

### Ejercicios interactivos

| Recurso | Descripción |
|---------|-------------|
| [Java Programming MOOC — Part 9](https://java-programming.mooc.fi/part-9) | Universidad de Helsinki: herencia, clases abstractas e interfaces, con ejercicios autocorregidos. |
| [Exercism — Java track](https://exercism.org/tracks/java) | Los ejercicios del concepto *inheritance*. |
