/* ================================================
   verNivel.js
================================================ */

const problemas = [
  { nombre:"Problema 1: Insertar al inicio", tipo:"Obligatorio", estado:"Enviado",  estadoClass:"enviado",  pts:40, link:"/verProblema" },
  { nombre:"Problema 2: Eliminar nodo",      tipo:"Obligatorio", estado:"Pendiente",estadoClass:"pendiente", pts:40, link:"/verProblema" },
  { nombre:"Problema 3: Buscar elemento",    tipo:"Obligatorio", estado:"Pendiente",estadoClass:"pendiente", pts:40, link:"/verProblema" },
  { nombre:"Problema 4: Invertir lista",     tipo:"Opcional",    estado:"Pendiente",estadoClass:"pendiente", pts:40, link:"/verProblema" },
  { nombre:"Problema 5: Detectar ciclo",     tipo:"Opcional",    estado:"Calificado: Correcto",estadoClass:"correcto",  pts:40, link:"/verProblema" },
];

function renderProblemas() {
  document.getElementById("probList").innerHTML = problemas.map(p => `
    <div class="prob-item">
      <div class="prob-icon">${p.tipo === 'Obligatorio' ? '★' : '○'}</div>
      <div class="prob-item-left">
        <div>
          <div class="prob-info-nombre">${p.nombre}</div>
          <div class="prob-info-pts">
            <span class="tag ${p.tipo === 'Obligatorio' ? 'tag-oblig' : 'tag-opc'}" style="font-size:.7rem">${p.tipo}</span>
            ⏱ ${p.pts} pts
          </div>
        </div>
      </div>
      <span class="prob-estado ${p.estadoClass}">${p.estado === 'Enviado' ? '✓ Enviado' : p.estado}</span>
      ${p.estado === 'Enviado'
        ? `<button class="btn-ver-envio">Ver envío</button>`
        : `<button class="btn-resolver" onclick="window.location.href='/Alumno/Competencias/Problema'">Resolver</button>`
      }
    </div>
  `).join("");
}

/* ── TIMER ── */
let timerSecs = 8 * 60 + 12;

function tickTimer() {
  if (timerSecs <= 0) return;
  timerSecs--;
  const m = Math.floor(timerSecs / 60).toString().padStart(2,"0");
  const s = (timerSecs % 60).toString().padStart(2,"0");
  const el = document.getElementById("timer");
  if (el) {
    el.textContent = `${m}:${s}`;
    el.style.color = timerSecs < 120 ? "#ef4444" : "";
  }
}

renderProblemas();
setInterval(tickTimer, 1000);