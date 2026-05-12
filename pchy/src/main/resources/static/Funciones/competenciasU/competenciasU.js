/* ================================================
   verCompetencia.js
================================================ */

const niveles = [
  {
    id: 1,
    nombre: "Nivel 1: Fundamentos",
    tag: "En curso",
    tagClass: "tag-active",
    tiempo: "20 min",
    timerActivo: true,
    timer: "08:12",
    bloqueado: false,
    problemas: [
      { nombre: "Problema 1: Insertar al inicio", tipo: "Obligatorio ★", estado: "Enviado", estadoClass: "enviado" },
      { nombre: "Problema 2: Eliminar nodo",      tipo: "Obligatorio ★", estado: "Pendiente", estadoClass: "pendiente" },
      { nombre: "Problema 3: Buscar elemento",    tipo: "Obligatorio ★", estado: "Pendiente", estadoClass: "pendiente" },
      { nombre: "Problema 4: Invertir lista",     tipo: "Opcional",      estado: "Opcional",  estadoClass: "" },
      { nombre: "Problema 5: Detectar ciclo",     tipo: "Opcional",      estado: "Opcional",  estadoClass: "" },
    ],
    desbloqueoMsg: "Completa los 3 obligatorios para desbloquear el siguiente nivel.",
    btnLabel: "Entrar al nivel",
    link: "/verNivel",
  },
  {
    id: 2,
    nombre: "Nivel 2: Intermedio",
    tag: "Bloqueado",
    tagClass: "tag-closed",
    tiempo: "25 min",
    timerActivo: false,
    timer: null,
    bloqueado: true,
    problemas: [],
    desbloqueoMsg: null,
    btnLabel: "Bloqueado",
    link: null,
  },
  {
    id: 3,
    nombre: "Nivel 3: Avanzado",
    tag: "Bloqueado",
    tagClass: "tag-closed",
    tiempo: "30 min",
    timerActivo: false,
    timer: null,
    bloqueado: true,
    problemas: [],
    desbloqueoMsg: null,
    btnLabel: "Bloqueado",
    link: null,
  },
];

const top5 = [
  { pos:1, nombre:"Ana",   pts:260 },
  { pos:2, nombre:"Luis",  pts:240 },
  { pos:3, nombre:"Elaine",pts:180 },
  { pos:4, nombre:"Sofía", pts:160 },
  { pos:5, nombre:"Diego", pts:150 },
];

function renderNiveles() {
  document.getElementById("nivelesList").innerHTML = niveles.map(n => `
    <div class="nivel-row ${n.bloqueado ? 'bloqueado' : ''}">
      <div class="nivel-row-header">
        <div class="nivel-row-left">
          <span class="nivel-row-nombre">${n.nombre}</span>
          <span class="tag ${n.tagClass}">${n.tag}</span>
          <span class="nivel-row-meta">
            <svg viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
            ${n.tiempo}
          </span>
          ${n.timerActivo ? `<span class="nivel-timer">⏱ Tiempo restante: ${n.timer}</span>` : ""}
        </div>
        ${n.bloqueado
          ? `<button class="btn-bloqueado" disabled>🔒 Bloqueado</button>`
          : `<button class="btn-entrar-nivel" onclick="window.location.href='/Alumno/Competencias/Niveles'">Entrar al nivel</button>`
        }
      </div>
      ${n.problemas.length ? `
        <div class="nivel-problemas-list">
          ${n.problemas.map(p => `
            <div class="prob-row">
              <span class="tag ${p.tipo.includes('Obligatorio') ? 'tag-oblig' : 'tag-opc'}" style="font-size:.7rem">${p.tipo}</span>
              <span class="prob-row-nombre">${p.nombre}</span>
              <span class="prob-row-estado ${p.estadoClass}">${p.estado === 'Enviado' ? '✓ Enviado' : p.estado}</span>
            </div>
          `).join("")}
          ${n.desbloqueoMsg ? `<div class="nivel-desbloqueo-msg">📌 ${n.desbloqueoMsg}</div>` : ""}
        </div>
      ` : ""}
    </div>
  `).join("");
}

function renderTop5() {
  document.getElementById("top5").innerHTML = top5.map(r => `
    <div class="top5-row">
      <span class="top5-pos">#${r.pos}</span>
      <span class="top5-nombre">${r.nombre}</span>
      <span class="top5-pts">${r.pts} pts</span>
    </div>
  `).join("");
}

renderNiveles();
renderTop5();