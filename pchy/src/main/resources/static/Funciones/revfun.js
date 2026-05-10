/* ================================================
   FUNCIONES – Revisión de Competencia
   Archivo: Funciones/revisionfun.js
================================================ */

// ══════════════════════════════════════════════════
// DATOS DE EJEMPLO
// ══════════════════════════════════════════════════
const alumnosRev = [
  {
    id: 1, nombre: "Juan Pérez",     inicial: "JP", estado: "entregado", niveles: "3/3",
    problemas: [
      { titulo: "Problema 1: Sumas",    sintaxis: "aceptada",  pts: 85, archivo: "solucion_juan_perez.txt",  completo: false },
      { titulo: "Problema 2: Arreglos", sintaxis: "rechazada", pts: 60, archivo: "solucion_juan_perez.txt",  completo: false },
      { titulo: "Problema 3: Arreglos", sintaxis: "aceptada",  pts: 90, archivo: "solucion_juan_perez.txt",  completo: true  }
    ]
  },
  {
    id: 2, nombre: "María López",    inicial: "ML", estado: "entregado", niveles: "3/3",
    problemas: [
      { titulo: "Problema 1: Sumas",    sintaxis: "aceptada",  pts: 95, archivo: "solucion_maria_lopez.txt", completo: true  },
      { titulo: "Problema 2: Arreglos", sintaxis: "aceptada",  pts: 90, archivo: "solucion_maria_lopez.txt", completo: false },
      { titulo: "Problema 3: Arreglos", sintaxis: "rechazada", pts: 70, archivo: "solucion_maria_lopez.txt", completo: false }
    ]
  },
  {
    id: 3, nombre: "Ana García",     inicial: "AG", estado: "entregado", niveles: "3/3",
    problemas: [
      { titulo: "Problema 1: Sumas",    sintaxis: "aceptada",  pts: 100, archivo: "solucion_ana_garcia.txt", completo: true  },
      { titulo: "Problema 2: Arreglos", sintaxis: "aceptada",  pts: 95,  archivo: "solucion_ana_garcia.txt", completo: true  },
      { titulo: "Problema 3: Arreglos", sintaxis: "aceptada",  pts: 90,  archivo: "solucion_ana_garcia.txt", completo: true  }
    ]
  },
  {
    id: 4, nombre: "Luis Hernández", inicial: "LH", estado: "entregado", niveles: "3/3",
    problemas: [
      { titulo: "Problema 1: Sumas",    sintaxis: "rechazada", pts: 50, archivo: "solucion_luis_hdz.txt",    completo: false },
      { titulo: "Problema 2: Arreglos", sintaxis: "rechazada", pts: 40, archivo: "solucion_luis_hdz.txt",    completo: false },
      { titulo: "Problema 3: Arreglos", sintaxis: "pendiente", pts: 0,  archivo: "solucion_luis_hdz.txt",    completo: false }
    ]
  }
];

const rankingData = [
  { nombre: "Ana López",      pts: 95 },
  { nombre: "Juan Pérez",     pts: 88 },
  { nombre: "María García",   pts: 80 },
  { nombre: "Luis Hernández", pts: 60 }
];

let alumnoActivo = null;
const tabsActivos = {};

// ══════════════════════════════════════════════════
// HELPERS
// ══════════════════════════════════════════════════
function avatarColorRev(inicial) {
  const colores = ["#dbeafe","#fce7f3","#dcfce7","#fef9c3","#ede9fe","#fee2e2","#e0f2fe","#f3e8ff"];
  const textos  = ["#1d4ed8","#be185d","#15803d","#a16207","#6d28d9","#b91c1c","#0369a1","#7e22ce"];
  const i = inicial.charCodeAt(0) % colores.length;
  return `background:${colores[i]};color:${textos[i]}`;
}

function capitalize(str) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

// ══════════════════════════════════════════════════
// RENDER LISTA ALUMNOS
// ══════════════════════════════════════════════════
function renderAlumnosRev(filtro = "") {
  const lista = document.getElementById("alumnosRevLista");
  if (!lista) return;
  const filtrados = alumnosRev.filter(a =>
    a.nombre.toLowerCase().includes(filtro.toLowerCase())
  );
  lista.innerHTML = filtrados.map(a => `
    <div class="alumno-rev-item ${alumnoActivo === a.id ? 'activo' : ''}"
         onclick="seleccionarAlumno(${a.id})">
      <div class="avatar-letra sm" style="${avatarColorRev(a.inicial)}">${a.inicial}</div>
      <span class="alumno-rev-nombre">${a.nombre}</span>
      <span class="alumno-rev-estado ${a.estado}">${capitalize(a.estado)}</span>
      <span class="alumno-rev-niveles">Niveles: ${a.niveles}</span>
    </div>`).join("");
}

// ══════════════════════════════════════════════════
// SELECCIONAR ALUMNO
// ══════════════════════════════════════════════════
function seleccionarAlumno(id) {
  alumnoActivo = id;
  const alumno = alumnosRev.find(a => a.id === id);
  renderAlumnosRev(document.getElementById("searchRevAlumno").value);
  renderResumen(alumno);
  renderProblemasRev(alumno);
}

// ══════════════════════════════════════════════════
// RENDER RESUMEN
// ══════════════════════════════════════════════════
function renderResumen(alumno) {
  document.getElementById("resumenTitulo").textContent = `Resumen del alumno (${alumno.nombre})`;
  const cal = Math.round(alumno.problemas.reduce((s, p) => s + p.pts, 0) / alumno.problemas.length);
  document.getElementById("calFinal").value = cal;

  document.getElementById("resumenTabla").innerHTML = alumno.problemas.map((p, i) => `
    <tr>
      <td>Problema ${i + 1}</td>
      <td>
        | API: <span class="api-tag ${p.sintaxis}">${capitalize(p.sintaxis)}</span>
        | <strong>${p.pts} pts</strong>
      </td>
    </tr>`).join("");
}

// ══════════════════════════════════════════════════
// RENDER PROBLEMAS (columna derecha)
// ══════════════════════════════════════════════════
function renderProblemasRev(alumno) {
  const col = document.getElementById("colDerecha");
  if (!col) return;

  col.innerHTML = `
    <div class="nivel-header">
      <div>
        <div class="nivel-titulo">Nivel 1</div>
        <div class="nivel-meta">Tiempo: 10 min</div>
      </div>
      <span class="nivel-total">Total problemas: ${alumno.problemas.length}</span>
    </div>
    ${alumno.problemas.map((p, i) => problemaRevHTML(p, i, alumno.id)).join("")}`;
}

function problemaRevHTML(p, i, alumnoId) {
  const cardId   = `pcard-${alumnoId}-${i}`;
  const tabKey   = `${alumnoId}-${i}`;
  const tabActivo = tabsActivos[tabKey] || "archivo";

  return `
    <div class="prob-rev-card" id="${cardId}">
      <div class="prob-rev-header">
        <span class="prob-rev-titulo">${p.titulo}</span>
        <span class="sintaxis-badge ${p.sintaxis}">Sintaxis: ${capitalize(p.sintaxis)}</span>
      </div>

      <div class="prob-rev-tabs">
        <button class="prob-rev-tab ${tabActivo==='enunciado'?'active':''}"
                onclick="switchRevTab('${cardId}','enunciado','${tabKey}')">Enunciado</button>
        <button class="prob-rev-tab ${tabActivo==='archivo'?'active':''}"
                onclick="switchRevTab('${cardId}','archivo','${tabKey}')">Archivo .txt</button>
        <button class="prob-rev-tab ${tabActivo==='api'?'active':''}"
                onclick="switchRevTab('${cardId}','api','${tabKey}')">Revisión API</button>
        <button class="prob-rev-tab ${tabActivo==='cal'?'active':''}"
                onclick="switchRevTab('${cardId}','cal','${tabKey}')">Calificación</button>
      </div>

      <!-- Enunciado -->
      <div class="prob-rev-panel ${tabActivo==='enunciado'?'visible':''}" id="${cardId}-enunciado">
        <p style="font-size:13px;color:#374151;line-height:1.6">
          Descripción del problema ${i+1}. Aquí aparecerá el enunciado completo del problema asignado.
        </p>
      </div>

      <!-- Archivo -->
      <div class="prob-rev-panel ${tabActivo==='archivo'?'visible':''}" id="${cardId}-archivo">
        <div class="archivo-top">
          <span>Archivo recibido: ${p.archivo}</span>
          <div class="archivo-btns">
            <button class="btn-archivo">Descargar</button>
            <button class="btn-archivo ${p.completo?'ver-completo':''}">${p.completo?'Ver completo':'Ver detalles'}</button>
          </div>
        </div>
        <div class="archivo-recibido">
          <div class="archivo-icono">📄</div>
          <div class="archivo-info">
            <span class="archivo-nombre-txt">Archivo recibido: ${p.archivo}</span>
            <span class="archivo-meta-txt">Formato: .txt · Tamaño: 12 KB</span>
          </div>
        </div>
      </div>

      <!-- Revisión API -->
      <div class="prob-rev-panel ${tabActivo==='api'?'visible':''}" id="${cardId}-api">
        <div class="rev-seccion-titulo">Revisión automática (API)</div>
        <input class="rev-input-pts" type="number" min="0" max="100" value="${p.pts}" placeholder="0"/>
        <div class="rev-pts-hint">(0–100)</div>
        <select class="rev-select">
          <option>Comentarios rápidos</option>
          <option>Lógica correcta</option>
          <option>Mejora estructura</option>
          <option>Revisar casos borde</option>
        </select>
        <textarea class="rev-textarea" placeholder="Comentario de la API..."></textarea>
      </div>

      <!-- Calificación -->
      <div class="prob-rev-panel ${tabActivo==='cal'?'visible':''}" id="${cardId}-cal">
        <div class="rev-seccion-titulo">Calificación del problema</div>
        <input class="rev-input-pts" type="number" min="0" max="100" value="${p.pts}" placeholder="0"/>
        <div class="rev-pts-hint">(0–100)</div>
        <select class="rev-select">
          <option>Comentarios rápidos</option>
          <option>Excelente solución</option>
          <option>Buena lógica, mejorar nombres</option>
          <option>Falta manejo de errores</option>
        </select>
        <div class="rev-seccion-titulo" style="margin-top:8px">Comentario del maestro</div>
        <textarea class="rev-textarea" placeholder="Escribe tu comentario..."></textarea>
      </div>
    </div>`;
}

// ══════════════════════════════════════════════════
// SWITCH TAB
// ══════════════════════════════════════════════════
function switchRevTab(cardId, tab, tabKey) {
  tabsActivos[tabKey] = tab;
  const card = document.getElementById(cardId);
  card.querySelectorAll(".prob-rev-tab").forEach(t => t.classList.remove("active"));
  card.querySelectorAll(".prob-rev-panel").forEach(p => p.classList.remove("visible"));
  const tabBtn = [...card.querySelectorAll(".prob-rev-tab")].find(t =>
    t.getAttribute("onclick").includes(`'${tab}'`));
  if (tabBtn) tabBtn.classList.add("active");
  const panel = card.querySelector(`#${cardId}-${tab}`);
  if (panel) panel.classList.add("visible");
}

// ══════════════════════════════════════════════════
// RANKING
// ══════════════════════════════════════════════════
function renderRanking() {
  const posClases = ["oro","plata","bronce",""];
  const lista = document.getElementById("rankingLista");
  if (!lista) return;
  lista.innerHTML = rankingData.map((r, i) => `
    <div class="ranking-item ${i===1?'destacado':''}">
      <span class="ranking-pos ${posClases[i]||''}">${i+1})</span>
      <span class="ranking-nombre">${r.nombre}</span>
      <span class="ranking-pts">${r.pts}</span>
    </div>`).join("");
}

function guardarCalificacion() {
  const cal = document.getElementById("calFinal").value;
  alert(`Calificación de ${cal} pts guardada correctamente.`);
}

// ══════════════════════════════════════════════════
// INIT URL PARAMS
// ══════════════════════════════════════════════════
function initRevision() {
  const urlParams  = new URLSearchParams(window.location.search);
  const compNombre = urlParams.get("nombre") || "Competencia";
  const compId     = urlParams.get("id") || "";

  const bcNombre = document.getElementById("bcNombre");
  if (bcNombre) {
    bcNombre.textContent = compNombre;
    bcNombre.href = `/Administrador/Competencias/competencia?id=${compId}&nombre=${encodeURIComponent(compNombre)}`;
  }
  const revTitulo = document.getElementById("revTitulo");
  if (revTitulo) revTitulo.textContent = `Revisión de competencia (cerrada)`;

  const revMeta = document.getElementById("revMeta");
  if (revMeta) revMeta.textContent = `Competencia: ${compNombre} · Lenguaje: Python · Fecha de cierre: 20 Oct 2026, 9:00 AM`;

  const revEstado = document.getElementById("revEstado");
  if (revEstado) revEstado.textContent = "Cerrada";
}

// ══════════════════════════════════════════════════
// INIT
// ══════════════════════════════════════════════════
document.addEventListener("DOMContentLoaded", () => {
  initRevision();
  renderAlumnosRev();
  renderRanking();

  const buscador = document.getElementById("searchRevAlumno");
  if (buscador) buscador.addEventListener("input", e => renderAlumnosRev(e.target.value));

  // Seleccionar primer alumno por defecto
  seleccionarAlumno(alumnosRev[0].id);
});