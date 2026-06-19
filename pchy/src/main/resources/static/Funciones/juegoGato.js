/*
=========================================
ESTADO GLOBAL
=========================================
*/

let modoJuego = null;

let jugador1Nombre = "";
let jugador2Nombre = "";

let personajeJugador1 = null;
let personajeJugador2 = null;

let victoriasJugador1 = 0;
let victoriasJugador2 = 0;

let turnoActual = "O";

let tablero = [
    ["", "", ""],
    ["", "", ""],
    ["", "", ""]
];


/*
=========================================
ELEMENTOS
=========================================
*/

const pantallaModo =
    document.getElementById("jg-pantalla-modo");

const pantallaPersonajes =
    document.getElementById("jg-pantalla-personajes");

const pantallaJuego =
    document.getElementById("jg-pantalla-juego");

const modal =
    document.getElementById("jg-modal");

const modalTitulo =
    document.getElementById("jg-modal-titulo");

const modalMensaje =
    document.getElementById("jg-modal-mensaje");

const turnoTexto =
    document.getElementById("jg-turno-actual");


/*
=========================================
INICIO
=========================================
*/

document.addEventListener(
    "DOMContentLoaded",
    iniciarJuego
);

function iniciarJuego() {

    jugador1Nombre =
        document.getElementById(
            "jg-nombre-jugador"
        ).value || "Jugador";

    crearTarjetasPersonajes();

    configurarEventos();

}


/*
=========================================
EVENTOS
=========================================
*/

function configurarEventos() {

    document
        .getElementById("jg-btn-vs-ia")
        .addEventListener(
            "click",
            () => seleccionarModo("IA")
        );

    document
        .getElementById("jg-btn-vs-jugador")
        .addEventListener(
            "click",
            () => seleccionarModo("PVP")
        );

    document
        .getElementById("jg-btn-volver")
        .addEventListener(
            "click",
            volverMenu
        );

    document
        .getElementById("jg-btn-menu")
        .addEventListener(
            "click",
            volverMenu
        );

    document
        .getElementById("jg-btn-reiniciar")
        .addEventListener(
            "click",
            reiniciarPartida
        );

    document
        .getElementById("jg-modal-continuar")
        .addEventListener(
            "click",
            cerrarModal
        );

    document
        .querySelectorAll(".jg-celda")
        .forEach(celda => {

            celda.addEventListener(
                "click",
                manejarClickCelda
            );

        });

}


/*
=========================================
SELECCIONAR MODO
=========================================
*/

function seleccionarModo(modo) {

    modoJuego = modo;

    pantallaModo.classList.add(
        "jg-hidden"
    );

    pantallaPersonajes.classList.remove(
        "jg-hidden"
    );

    document.getElementById(
        "jg-titulo-personajes"
    ).textContent =
        "Selecciona tu personaje";

    document.getElementById(
        "jg-subtitulo-personajes"
    ).textContent =
        jugador1Nombre;

    activarSeleccionPersonajes();

}


/*
=========================================
PERSONAJES
=========================================
*/

function activarSeleccionPersonajes() {

    document
        .querySelectorAll(".jg-personaje")
        .forEach(card => {

            card.onclick = () => {

                const personaje =
                    card.dataset.personaje;

                seleccionarPersonaje(
                    personaje
                );

            };

        });

}


function seleccionarPersonaje(personaje) {

    if (!personajeJugador1) {

        personajeJugador1 =
            personaje;

        if (modoJuego === "IA") {

            personajeJugador2 =
                "IA";

            iniciarPartida();

            return;
        }

        document.getElementById(
            "jg-titulo-personajes"
        ).textContent =
            "Jugador 2";

        document.getElementById(
            "jg-subtitulo-personajes"
        ).textContent =
            "Selecciona tu personaje";

        return;
    }

    if (
        personaje ===
        personajeJugador1
    ) {

        alert(
            "Selecciona otro personaje"
        );

        return;
    }

    personajeJugador2 =
        personaje;

    iniciarPartida();

}


/*
=========================================
INICIAR PARTIDA
=========================================
*/

function iniciarPartida() {

    pantallaPersonajes.classList.add(
        "jg-hidden"
    );

    pantallaJuego.classList.remove(
        "jg-hidden"
    );

    jugador2Nombre =
        modoJuego === "IA"
            ? "IA"
            : "Jugador 2";

    document.getElementById(
        "jg-nombre-panel-j1"
    ).textContent =
        jugador1Nombre;

    document.getElementById(
        "jg-nombre-panel-j2"
    ).textContent =
        jugador2Nombre;

    actualizarAvatares();

    actualizarMarcador();

    reiniciarPartida();

}


/*
=========================================
AVATARES
=========================================
*/

function actualizarAvatares() {

    actualizarAvatarJugador(
        document.getElementById(
            "jg-img-jugador1"
        ),
        personajeJugador1,
        victoriasJugador1,
        victoriasJugador2
    );

    if (modoJuego === "IA") {

        actualizarAvatarIA(
            document.getElementById(
                "jg-img-jugador2"
            ),
            victoriasJugador2,
            victoriasJugador1
        );

        return;
    }

    actualizarAvatarJugador(
        document.getElementById(
            "jg-img-jugador2"
        ),
        personajeJugador2,
        victoriasJugador2,
        victoriasJugador1
    );

}


/*
=========================================
TABLERO
=========================================
*/

function reiniciarPartida() {

    tablero = [
        ["", "", ""],
        ["", "", ""],
        ["", "", ""]
    ];

    turnoActual = "O";

    dibujarTablero();

    actualizarTurno();

}


function dibujarTablero() {

    const celdas =
        document.querySelectorAll(
            ".jg-celda"
        );

    celdas.forEach(celda => {

        const fila =
            Number(celda.dataset.row);

        const col =
            Number(celda.dataset.col);

        celda.textContent =
            tablero[fila][col];

        celda.classList.remove(
            "jg-x",
            "jg-o"
        );

        if (
            tablero[fila][col] === "X"
        ) {
            celda.classList.add(
                "jg-x"
            );
        }

        if (
            tablero[fila][col] === "O"
        ) {
            celda.classList.add(
                "jg-o"
            );
        }

    });

}


/*
=========================================
CLICK CELDA
=========================================
*/

function manejarClickCelda(e) {

    const fila =
        Number(
            e.target.dataset.row
        );

    const col =
        Number(
            e.target.dataset.col
        );

    if (
        tablero[fila][col] !== ""
    ) {
        return;
    }

    tablero[fila][col] =
        turnoActual;

    dibujarTablero();

    if (verificarFinJuego()) {
        return;
    }

    if (
        modoJuego === "IA"
    ) {

        turnoIA();

        return;
    }

    turnoActual =
        turnoActual === "O"
            ? "X"
            : "O";

    actualizarTurno();

}


/*
=========================================
IA
=========================================
*/

function turnoIA() {

    const movimiento =
        mejorMovimiento(tablero);

    if (!movimiento) {
        return;
    }

    const [fila, col] =
        movimiento;

    tablero[fila][col] =
        "X";

    dibujarTablero();

    verificarFinJuego();

}


/*
=========================================
FIN DEL JUEGO
=========================================
*/

function verificarFinJuego() {

    const resultado =
        ganador(tablero);

    if (resultado === "O") {

        victoriasJugador1++;

        finalizarPartida(
            `${jugador1Nombre} gana`
        );

        return true;
    }

    if (resultado === "X") {

        victoriasJugador2++;

        finalizarPartida(
            `${jugador2Nombre} gana`
        );

        return true;
    }

    if (empate(tablero)) {

        finalizarPartida(
            "Empate"
        );

        return true;
    }

    return false;

}


function finalizarPartida(
    mensaje
) {

    actualizarMarcador();

    actualizarAvatares();

    modalTitulo.textContent =
        "Fin de partida";

    modalMensaje.textContent =
        mensaje;

    modal.classList.remove(
        "jg-hidden"
    );

}


function cerrarModal() {

    modal.classList.add(
        "jg-hidden"
    );

    reiniciarPartida();

}


/*
=========================================
UI
=========================================
*/

function actualizarMarcador() {

    document.getElementById(
        "jg-score-j1"
    ).textContent =
        victoriasJugador1;

    document.getElementById(
        "jg-score-j2"
    ).textContent =
        victoriasJugador2;

}


function actualizarTurno() {

    let nombre;

    if (turnoActual === "O") {

        nombre =
            jugador1Nombre;

    } else {

        nombre =
            jugador2Nombre;

    }

    turnoTexto.textContent =
        "Turno de " + nombre;

}


/*
=========================================
VOLVER AL MENÚ
=========================================
*/

function volverMenu() {

    modoJuego = null;

    personajeJugador1 = null;
    personajeJugador2 = null;

    victoriasJugador1 = 0;
    victoriasJugador2 = 0;

    pantallaJuego.classList.add(
        "jg-hidden"
    );

    pantallaPersonajes.classList.add(
        "jg-hidden"
    );

    pantallaModo.classList.remove(
        "jg-hidden"
    );

}