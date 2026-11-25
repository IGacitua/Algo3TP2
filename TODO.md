Correciones (Eliminar al completar):

- Modelo y Codigo:
    - [Medio] Implementa al menos una clase anemica
        - Mech: solo `isMech()`, `createMoveCommand` duplicado y canAttack sin uso; no existen los tipos de Mech
        - *Ver Sugerencias*

    - [Medio] If en lugar de polimorfismo:
        - [X] `PlayerBehavior.executeAction`: `instanceof MoveAction / AttackAction`

    - [Medio] Desnormalizacion del estado PlayerBehavior:
        - [X] hasMoved y previousMovement != null dicen lo mismo;
        - [X] flags hasAttacked/hasUndone (candidato a State) `Unit.isDamaged()` derivable de Health;

    - [Leve] Un nuevo requerimiento implicara alterar varias clases
        - Nuevo tipo de casilla: subclase + CellFactory + `isX()` en Cell + `hasXAt()` en GameMap

- Bugs funcionales:
    - El movimiento atraviesa montanas (solo compara distancia Manhattan)
    - Un Mech puede moverse sobre un Edificio: lo destruye y resta Red
    - Mover/empujar a casilla ocupada: la unidad desaparece del tablero
    - getReachablePositions borra la unidad del mapa y usa distancia euclidea
    - Canon: con un Vek detras del Mech, el disparo sale del Vek y dania al Mech
    - Un Vek terrestre puede moverse voluntariamente al Agua y muere
    - [X] PlayerBehavior no esta asociado a un Mech (mover 2 Mechs falla / undo por Mech y no por turno)

- Pruebas:

  -[Grave] Tests incorrectos / vacios:
    - [X] mechCanUndoMovementBeforeAttacking compara la misma instancia de posicion: pasa sin deshacer nada
    - MovementIntegrationTest usa Grounded (2)/vida 10 y un mapa sin obstaculos en el camino

Sugerencias (Ezequiel):

- Separación Mek/Vek puede ser refactorizada:
    - isMech () -> Boolean en el constructor. O, mas adecuadamente, un 'Equipo' enum, Jugador/Enemigo.
        - Con eso podemos hacer que la pregunta no sea 'hay una unidad aca', si no 'hay una unidad que NO es del mi
          equipo aca'
    - Comportamiento Vek -> UnitBehavior, recibido en el constructor.
    - Comportamiento Mek -> Idem.

Entrega 2:

- [X] Verificar que al empujar una unidad contra otra unidad u obstáculo, la unidad no se mueva y se aplique el daño de
  colisión.
- [ ] Verificar que el daño a un edificio reduzca la Red Eléctrica en 1 punto.
- [ ] Verificar que un Vek terrestre empujado al Agua muera, que el Avispón no, y que un Mech en el Agua no pueda
  atacar.
- [X] Verificar que una unidad terrestre que termina sobre Ácido quede Contaminada y reciba el doble de daño, y que el
  Avispón no se contamine.
- [ ] Verificar que la Luciérnaga contamine a la unidad que impacta y que la contaminación no se acumule.
- [X] Verificar que al deshacer el movimiento de un Mech que terminó sobre Ácido, el Mech deje de estar Contaminado.
- [ ] Verificar que cada Vek anuncie su ataque en la Fase Vek y que se ejecute en la Fase de Resolución, en el orden
  anunciado.
- [ ] Verificar que al empujar a un Vek su ataque anunciado se desplace con él, y que si queda apuntando a otro Vek, lo
  dañe.
- [ ] Verificar que la IA de los Vek aplique las reglas de prioridad en orden.
- [ ] Verificar que una unidad sobre la casilla de emergencia bloquee la aparición del nuevo Vek y reciba 1 de daño.
- [ ] Planteo inicial de interfaz gráfica (mockups/dibujos) mostrando: el tablero con los ataques anunciados, la
  información de cada unidad, la Red Eléctrica y el turno actual.
