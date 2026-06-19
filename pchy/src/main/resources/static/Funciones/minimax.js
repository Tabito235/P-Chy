/*
=========================================
MINIMAX + ALPHA BETA PRUNING
=========================================

IA = X
Jugador = O

Estados:

1  = IA gana
0  = Empate
-1 = Jugador gana

=========================================
*/


function obtenerCasillasDisponibles(tablero) {

    const disponibles = [];

    for (let fila = 0; fila < 3; fila++) {

        for (let col = 0; col < 3; col++) {

            if (tablero[fila][col] === "") {

                disponibles.push([fila, col]);

            }

        }

    }

    return disponibles;

}


/*
=========================================
GANADOR
=========================================
*/

function ganador(tablero) {

    const lineas = [

        // Filas
        [
            [0, 0],
            [0, 1],
            [0, 2]
        ],

        [
            [1, 0],
            [1, 1],
            [1, 2]
        ],

        [
            [2, 0],
            [2, 1],
            [2, 2]
        ],

        // Columnas
        [
            [0, 0],
            [1, 0],
            [2, 0]
        ],

        [
            [0, 1],
            [1, 1],
            [2, 1]
        ],

        [
            [0, 2],
            [1, 2],
            [2, 2]
        ],

        // Diagonales
        [
            [0, 0],
            [1, 1],
            [2, 2]
        ],

        [
            [0, 2],
            [1, 1],
            [2, 0]
        ]

    ];

    for (const linea of lineas) {

        const a =
            tablero[linea[0][0]][linea[0][1]];

        const b =
            tablero[linea[1][0]][linea[1][1]];

        const c =
            tablero[linea[2][0]][linea[2][1]];

        if (
            a !== "" &&
            a === b &&
            b === c
        ) {
            return a;
        }

    }

    return null;

}


/*
=========================================
EMPATE
=========================================
*/

function empate(tablero) {

    if (ganador(tablero) !== null) {
        return false;
    }

    return obtenerCasillasDisponibles(
        tablero
    ).length === 0;

}


/*
=========================================
UTILIDAD
=========================================
*/

function utilidad(tablero) {

    const resultado =
        ganador(tablero);

    if (resultado === "X") {
        return 1;
    }

    if (resultado === "O") {
        return -1;
    }

    if (empate(tablero)) {
        return 0;
    }

    return null;

}


/*
=========================================
MINIMAX
=========================================
*/

function minimax(
    tablero,
    profundidad,
    esMax,
    alpha,
    beta
) {

    const valor =
        utilidad(tablero);

    if (valor !== null) {

        return valor;

    }

    /*
    ==========================
    TURNO IA
    ==========================
    */

    if (esMax) {

        let mejor = -Infinity;

        const movimientos =
            obtenerCasillasDisponibles(
                tablero
            );

        for (const [fila, col] of movimientos) {

            tablero[fila][col] = "X";

            const score =
                minimax(
                    tablero,
                    profundidad + 1,
                    false,
                    alpha,
                    beta
                );

            tablero[fila][col] = "";

            mejor =
                Math.max(
                    mejor,
                    score
                );

            alpha =
                Math.max(
                    alpha,
                    mejor
                );

            if (beta <= alpha) {
                break;
            }

        }

        return mejor;

    }

    /*
    ==========================
    TURNO JUGADOR
    ==========================
    */

    let mejor = Infinity;

    const movimientos =
        obtenerCasillasDisponibles(
            tablero
        );

    for (const [fila, col] of movimientos) {

        tablero[fila][col] = "O";

        const score =
            minimax(
                tablero,
                profundidad + 1,
                true,
                alpha,
                beta
            );

        tablero[fila][col] = "";

        mejor =
            Math.min(
                mejor,
                score
            );

        beta =
            Math.min(
                beta,
                mejor
            );

        if (beta <= alpha) {
            break;
        }

    }

    return mejor;

}


/*
=========================================
MEJOR MOVIMIENTO
=========================================

Mejora respecto a Python:

Si existen varias jugadas
óptimas se elige una al azar.

La IA sigue siendo invencible
pero deja de jugar siempre igual.

=========================================
*/

function mejorMovimiento(tablero) {

    let mejorValor =
        -Infinity;

    let mejoresMovimientos =
        [];

    const movimientos =
        obtenerCasillasDisponibles(
            tablero
        );

    for (const [fila, col] of movimientos) {

        tablero[fila][col] = "X";

        const valor =
            minimax(
                tablero,
                0,
                false,
                -Infinity,
                Infinity
            );

        tablero[fila][col] = "";

        if (valor > mejorValor) {

            mejorValor = valor;

            mejoresMovimientos = [
                [fila, col]
            ];

        }

        else if (
            valor === mejorValor
        ) {

            mejoresMovimientos.push(
                [fila, col]
            );

        }

    }

    const indice =
        Math.floor(
            Math.random() *
            mejoresMovimientos.length
        );

    return mejoresMovimientos[
        indice
    ];

}