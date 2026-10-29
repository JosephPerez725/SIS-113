# Clase 8 — Soluciones

## Taller de Cine ([cine/](cine/))

```bash
cd cine && javac -d out *.java && java -cp out Main
```

```
Regular | Dune: Part Three | seat F7 | Bs 50.0
Student | Dune: Part Three | seat F8 | Bs 35.0
Senior | Dune: Part Three | seat F9 | Bs 30.0
Premium | Dune: Part Three | seat A1 | Bs 90.0 (with snack)
Total: Bs 205.0
```

Verificado también: `new SeniorTicket(..., 40)` rechaza la edad, `new StudentTicket(..., " ")` rechaza el carnet vacío, y `new DiscountTicket(...)` no compila: *DiscountTicket is abstract; cannot be instantiated*.

## Puente a Sentinel ([sentinel/](sentinel/))

Copiar `Rule.java`, `MemoryRule.java` y `CpuRule.java` sobre una copia de `sentinel/phase2-java/oop/`. `Main` y `Console` no se tocan. La salida con la muestra limpia es **idéntica** a la de antes del cambio, verificado con `diff`.

`CpuRule` con la muestra limpia: 4 alertas con umbral 60, 1 alerta con umbral 80.

## Ejercicios ([../Clase08-Ejercicios/](../Clase08-Ejercicios/))

| Drill | Qué pasa | Causa | Corrección |
|---|---|---|---|
| 1 | No compila: *constructor Movie in class Movie cannot be applied to given types; required: String, found: no arguments* | Si el constructor del hijo no llama a `super(...)`, Java intenta llamar al constructor **vacío** del padre. `Movie` no tiene uno. | Primera línea del constructor: `super(title);` |
| 2 | No compila: *price has private access in Seat* | El hijo **tiene** el atributo, pero `private` significa privado incluso para los hijos. | Un `protected double getPrice()` en `Seat`, y `return getPrice() * 1.5;`. Hacer el atributo `protected` también compila, pero abre el dato a cualquier subclase. |
| 3 | `combo: Bs 10.0` | `getprice` con minúscula es un **método nuevo**, no una sobrescritura. `combo.getPrice()` usa el del padre. | Renombrar a `getPrice` y poner `@Override`. Con `@Override` sobre el nombre mal escrito, el compilador avisa: *method does not override or implement a method from a supertype*. |
| 4 | No compila: *MorningScreening is not abstract and does not override abstract method discountPercent()* | Un método `abstract` es una obligación para todo hijo concreto. | `@Override int discountPercent() { return 20; }` |
| 5 | Corre: `Large popcorn \| seat none \| Bs 20.0` | Pregunta de diseño: las pipocas **no son** una entrada. Heredar solo para reusar un atributo obliga a inventar un asiento `"none"`. | No heredar. `Popcorn` es su propia clase. Si una entrada incluye pipocas, la entrada **tiene** un `Popcorn`: composición. |

## Pregunta de la tarea: `LoadRule`

Al intentar `LoadRule` (alertar si `load1 >= 2.0`), el estudiante choca con dos cosas que impone `Rule`:

1. El umbral es un `int` entre 1 y 100. La carga es un `double` sin tope: 2.0 no se puede expresar.
2. `getThreshold()` devuelve `int`.

Respuestas válidas: no heredar de `Rule` y esperar a las interfaces de la Clase 9, o mover la validación del rango a `MemoryRule` y `CpuRule`. Lo que se busca es que vean el costo: **la clase base decide por todos sus hijos**, y un hijo que no encaja obliga a cambiar al padre y, con él, a todos los hermanos.
