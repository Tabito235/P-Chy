class PichyTips {

    constructor() {

        this.imagenes = [];

        // Cargar las 60 imágenes
        for(let i = 1; i <= 60; i++) {
            this.imagenes.push(
                `/Imagenes/Pichy_Consejos/Pichy_consejo${i}.png`
            );
        }

        this.ultimaImagen = -1;
        this.intervalo = null;

        this.crearContenedor();
    }

    crearContenedor() {

        this.contenedor = document.createElement("div");
        this.contenedor.id = "pichy-container";

        this.imagen = document.createElement("img");

        this.contenedor.appendChild(this.imagen);

        document.body.appendChild(this.contenedor);
    }

    mostrar() {

        let indice;

        do {
            indice = Math.floor(
                Math.random() * this.imagenes.length
            );
        }
        while(indice === this.ultimaImagen);

        this.ultimaImagen = indice;

        this.imagen.src = this.imagenes[indice];

        const lado = Math.random() < 0.5 ? "izquierda" : "derecha";

        this.contenedor.style.top = "";
        this.contenedor.style.left = "";
        this.contenedor.style.right = "";

        this.contenedor.style.bottom = "20px";

        if(lado === "izquierda"){
            this.contenedor.style.left = "20px";
        }
        else{
            this.contenedor.style.right = "20px";
        }

        this.contenedor.style.display = "block";

        setTimeout(() => {
            this.contenedor.style.display = "none";
        }, 10000);
    }

    iniciar() {

        // Primera aparición a los 10 segundos
        setTimeout(() => {
            this.mostrar();
        }, 10000);

        // Luego cada 2 minutos
        this.intervalo = setInterval(() => {
            this.mostrar();
        }, 120000);
    }

    detener() {

        clearInterval(this.intervalo);

        this.contenedor.style.display = "none";
    }
}