/*
=========================================
CONFIGURACIÓN DE PERSONAJES
=========================================
*/

const GATOS = {

    Gato1: {

        nombre: "Gato 1",

        normal: "/Imagenes/img/Gato1.png",

        ganando: "/Imagenes/img/Gato1.png",

        empate: "/Imagenes/img/Gato1.3.png",

        perdiendo: "/Imagenes/img/Gato1.2.png"

    },

    Gato2: {

        nombre: "Gato 2",

        normal: "/Imagenes/img/Gato2.png",

        ganando: "/Imagenes/img/Gato2.png",

        empate: "/Imagenes/img/Gato2.png",

        perdiendo: "/Imagenes/img/Gato2.1.png"

    },

    Gato3: {

        nombre: "Gato 3",

        normal: "/Imagenes/img/Gato3.png",

        ganando: "/Imagenes/img/Gato3.png",

        empate: "/Imagenes/img/Gato3.png",

        perdiendo: "/Imagenes/img/Gato3.1.png"

    },

    Gato4: {

        nombre: "Gato 4",

        normal: "/Imagenes/img/Gato4.png",

        ganando: "/Imagenes/img/Gato4.png",

        empate: "/Imagenes/img/Gato4.png",

        perdiendo: "/Imagenes/img/Gato4.1.png"

    },

    Pichy: {

        nombre: "Pichy",

        normal: "/Imagenes/img/Pichy.png",

        ganando: "/Imagenes/img/Pichy.png",

        empate: "/Imagenes/img/Pichy.png",

        perdiendo: "/Imagenes/img/Pichy.png"

    }

};

/*
=========================================
PERSONAJE DE LA IA
=========================================
*/

const IA_PERSONAJE = {

    nombre: "IA",

    normal: "/Imagenes/img/AIien.png",

    ganando: "/Imagenes/img/AIien.png",

    empate: "/Imagenes/img/AIien.png",

    perdiendo: "/Imagenes/img/AIien.png"

};

/*
=========================================
CREA TARJETAS DE SELECCIÓN
=========================================
*/

function crearTarjetasPersonajes() {

    const contenedor =
        document.getElementById("jg-grid-personajes");

    contenedor.innerHTML = "";

    Object.keys(GATOS).forEach(clave => {

        const personaje = GATOS[clave];

        const tarjeta =
            document.createElement("div");

        tarjeta.className = "jg-personaje";

        tarjeta.dataset.personaje = clave;

        tarjeta.innerHTML = `
            <img src="${personaje.normal}"
                 alt="${personaje.nombre}">
            <h3>${personaje.nombre}</h3>
        `;

        contenedor.appendChild(tarjeta);

    });

}

/*
=========================================
OBTIENE IMAGEN SEGÚN MARCADOR
=========================================
*/

function obtenerImagenEstado(
    personajeKey,
    victoriasPropias,
    victoriasRival
) {

    const personaje =
        GATOS[personajeKey];

    if (!personaje) {
        return "";
    }

    if (victoriasPropias > victoriasRival) {
        return personaje.ganando;
    }

    if (victoriasPropias < victoriasRival) {
        return personaje.perdiendo;
    }

    return personaje.empate;
}

/*
=========================================
ACTUALIZA AVATAR VISUAL
=========================================
*/

function actualizarAvatarJugador(
    imgElement,
    personajeKey,
    victoriasPropias,
    victoriasRival
) {

    const ruta =
        obtenerImagenEstado(
            personajeKey,
            victoriasPropias,
            victoriasRival
        );

    imgElement.src = ruta;

}

/*
=========================================
ACTUALIZA IA
=========================================
*/

function actualizarAvatarIA(
    imgElement,
    victoriasIA,
    victoriasJugador
) {

    if (victoriasIA > victoriasJugador) {

        imgElement.src =
            IA_PERSONAJE.ganando;

        return;
    }

    if (victoriasIA < victoriasJugador) {

        imgElement.src =
            IA_PERSONAJE.perdiendo;

        return;
    }

    imgElement.src =
        IA_PERSONAJE.empate;

}

const gato =
    document.getElementById(
        "abrirJuegoGato"
    );

if(gato){

    gato.addEventListener(
        "click",
        () => {

            document
                .getElementById(
                    "contenedorJuegoGato"
                )
                .style.display =
                "block";

            gato.scrollIntoView({
                behavior:"smooth"
            });

        }
    );

}