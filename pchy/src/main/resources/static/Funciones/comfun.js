/* ================================================
   FUNCIONES – Enclase + Editar Competencia
   Archivo: Funciones/comfun.js
================================================ */

// ══════════════════════════════════════════════════
// DATOS DE EJEMPLO — Competencias
// ══════════════════════════════════════════════════
const competenciasData = [
  {
    id: 1, nombre: "Bucles en Python", lenguaje: "Python",
    fecha: "Oct 26, 2026, 10:00 AM", estado: "Activa",
    niveles: [{ nombre: "Nivel 1", min: 10 }, { nombre: "Nivel 2", min: 15 }, { nombre: "Nivel 3", min: 20 }]
  },
  {
    id: 2, nombre: "Arreglos en C++", lenguaje: "C++",
    fecha: "Nov 1, 2026, 2:00 PM", estado: "Programada",
    niveles: []
  },
  {
    id: 3, nombre: "Funciones", lenguaje: "Python",
    fecha: "Oct 20, 2026, 9:00 AM", estado: "Cerrada",
    niveles: [{ nombre: "Nivel 1", min: 15 }, { nombre: "Nivel 2", min: 20 }]
  },
  {
    id: 4, nombre: "Recursividad", lenguaje: "C++",
    fecha: "Nov 5, 2026, 11:30 AM", estado: "Activa",
    niveles: [{ nombre: "Nivel 1", min: 20 }]
  },
  {
    id: 5, nombre: "POO básica", lenguaje: "Python",
    fecha: "Nov 10, 2026, 3:00 PM", estado: "Programada",
    niveles: [{ nombre: "Nivel 1", min: 15 }, { nombre: "Nivel 2", min: 25 }]
  },
  {
    id: 6, nombre: "Estructuras: Pilas y Colas", lenguaje: "C++",
    fecha: "Nov 15, 2026, 1:00 PM", estado: "Activa",
    niveles: [{ nombre: "Nivel 1", min: 10 }, { nombre: "Nivel 2", min: 15 }, { nombre: "Nivel 3", min: 20 }]
  }
];

// ══════════════════════════════════════════════════
// DATOS DE EJEMPLO — Alumnos
// ══════════════════════════════════════════════════
const alumnosData = [
  { nombre: "Juan Pérez",     usuario: "jperez",    inicial: "JP" },
  { nombre: "María López",    usuario: "mlopez",    inicial: "ML" },
  { nombre: "Carlos Ruiz",    usuario: "cruiz",     inicial: "CR" },
  { nombre: "Ana González",   usuario: "agonzalez", inicial: "AG" },
  { nombre: "Luis Martínez",  usuario: "lmartinez", inicial: "LM" },
  { nombre: "Sofía Torres",   usuario: "storres",   inicial: "ST" },
  { nombre: "Diego Ramírez",  usuario: "dramirez",  inicial: "DR" },
  { nombre: "Valeria Flores", usuario: "vflores",   inicial: "VF" },
  { nombre: "Andrés Morales", usuario: "amorales",  inicial: "AM" },
];

// ══════════════════════════════════════════════════
// DATOS DE EJEMPLO — Solicitudes
// ══════════════════════════════════════════════════
const solicitudesData = [
  { nombre: "Roberto Silva",   usuario: "rsilva",   inicial: "RS" },
  { nombre: "Paola Herrera",   usuario: "pherrera", inicial: "PH" },
  { nombre: "Miguel Castillo", usuario: "mcastillo",inicial: "MC" },
  { nombre: "Laura Mendoza",   usuario: "lmendoza", inicial: "LM" },
  { nombre: "Fernando Reyes",  usuario: "freyes",   inicial: "FR" },
  { nombre: "Isabella Cruz",   usuario: "icruz",    inicial: "IC" },
  { nombre: "Sebastián Vega",  usuario: "svega",    inicial: "SV" },
  { nombre: "Camila Ortega",   usuario: "cortega",  inicial: "CO" },
  { nombre: "Tomás Jiménez",   usuario: "tjimenez", inicial: "TJ" },
  { nombre: "Natalia Ramos",   usuario: "nramos",   inicial: "NR" },
  { nombre: "Emilio Vargas",   usuario: "evargas",  inicial: "EV" },
  { nombre: "Daniela Soto",    usuario: "dsoto",    inicial: "DS" },
];

// ══════════════════════════════════════════════════
// HELPERS DE COLOR
// ══════════════════════════════════════════════════
function langStyle(lang) {
  if (lang === "Python") return "background:#e0f2fe;color:#0369a1";
  if (lang === "C++")    return "background:#ede9fe;color:#6d28d9";
  return "background:#f1f5f9;color:#374151";
}

function estadoConfig(estado) {
  const map = {
    "Activa":     { bg: "#dcfce7", color: "#15803d", dot: "#16a34a" },
    "Programada": { bg: "#fef9c3", color: "#a16207", dot: "#ca8a04" },
    "Cerrada":    { bg: "#f1f5f9", color: "#475569", dot: "#94a3b8" }
  };
  return map[estado] || map["Cerrada"];
}

function avatarColor(inicial) {
  const colores = [
    "#dbeafe","#fce7f3","#dcfce7","#fef9c3",
    "#ede9fe","#fee2e2","#e0f2fe","#f3e8ff"
  ];
  const textos = [
    "#1d4ed8","#be185d","#15803d","#a16207",
    "#6d28d9","#b91c1c","#0369a1","#7e22ce"
  ];
  const i = inicial.charCodeAt(0) % colores.length;
  return `background:${colores[i]};color:${textos[i]}`;
}

// ══════════════════════════════════════════════════
// RENDER GRID — Competencias
// ══════════════════════════════════════════════════
function renderCompGrid() {
  const grid = document.getElementById("compGrid");
  if (!grid) return;

  grid.innerHTML = competenciasData.map(c => {
    const est = estadoConfig(c.estado);
    const nivelesHTML = c.niveles.length > 0
      ? c.niveles.map(n => `<div class="nivel">${n.nombre}: ${n.min} min</div>`).join("")
      : `<div class="nivel muted">Sin niveles</div>`;

    return `
      <div class="card comp-card" data-comp-id="${c.id}" data-comp-nombre="${c.nombre}">
        <div class="comp-top">
          <span class="comp-title">${c.nombre}</span>
          <span class="lang-tag" style="${langStyle(c.lenguaje)}">${c.lenguaje}</span>
        </div>
        <div class="comp-date">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="4" width="18" height="18" rx="2"/>
            <line x1="16" y1="2" x2="16" y2="6"/>
            <line x1="8" y1="2" x2="8" y2="6"/>
            <line x1="3" y1="10" x2="21" y2="10"/>
          </svg>
          ${c.fecha}
        </div>
        <span class="status-pill" style="background:${est.bg};color:${est.color}">
          <span style="display:inline-block;width:7px;height:7px;border-radius:50%;background:${est.dot};margin-right:5px;vertical-align:middle"></span>
          ${c.estado}
        </span>
        <div class="niveles">${nivelesHTML}</div>
        <div class="comp-actions">
          <button class="btn-enter"   onclick="abrirEditar(${c.id},'${c.nombre}')">Entrar</button>
          <button class="btn-outline" onclick="abrirEditar(${c.id},'${c.nombre}')">Editar</button>
          <button class="btn-outline btn-revision" onclick="abrirRevision(${c.id},'${c.nombre}')">Revisión</button>
          <button class="menu-btn">⋯</button>
        </div>
      </div>`;
  }).join("");
}

// ══════════════════════════════════════════════════
// RENDER ALUMNOS
// ══════════════════════════════════════════════════
function renderAlumnos(filtro = "") {
  const grid = document.getElementById("alumnosGrid");
  if (!grid) return;
  const filtrados = alumnosData.filter(a =>
    a.nombre.toLowerCase().includes(filtro.toLowerCase()) ||
    a.usuario.toLowerCase().includes(filtro.toLowerCase())
  );
  grid.innerHTML = filtrados.map(a => `
    <div class="alumno-card">
      <div class="avatar-letra" style="${avatarColor(a.inicial)}">${a.inicial}</div>
      <div>
        <div class="alumno-name">${a.nombre}</div>
        <div class="alumno-user">@${a.usuario}</div>
        <span class="status-badge activo">Activo</span>
      </div>
    </div>`).join("");
}

// ══════════════════════════════════════════════════
// RENDER SOLICITUDES (panel lateral + pestaña full)
// ══════════════════════════════════════════════════
function solicitudHTML(s, modo = "panel") {
  if (modo === "panel") {
    return `
      <div class="solicitud-item">
        <div class="avatar-letra sm" style="${avatarColor(s.inicial)}">${s.inicial}</div>
        <div class="sol-info">
          <div class="alumno-name">${s.nombre}</div>
          <div class="alumno-user">@${s.usuario}</div>
        </div>
        <div class="sol-actions">
          <button class="btn-aceptar">✓</button>
          <button class="btn-rechazar">✕</button>
        </div>
      </div>`;
  }
  return `
    <div class="solicitud-item full">
      <div class="avatar-letra" style="${avatarColor(s.inicial)}">${s.inicial}</div>
      <div class="sol-info">
        <div class="alumno-name">${s.nombre}</div>
        <div class="alumno-user">@${s.usuario}</div>
      </div>
      <div class="sol-actions">
        <button class="btn-aceptar">Aceptar</button>
        <button class="btn-rechazar">Rechazar</button>
      </div>
    </div>`;
}

function renderSolicitudes() {
  const lista = document.getElementById("solicitudesList");
  const full  = document.getElementById("solicitudesFull");
  // Panel lateral: primeras 4
  if (lista) lista.innerHTML = solicitudesData.slice(0, 4).map(s => solicitudHTML(s, "panel")).join("");
  // Pestaña completa: todas
  if (full)  full.innerHTML  = solicitudesData.map(s => solicitudHTML(s, "full")).join("");
}

// ══════════════════════════════════════════════════
// MODAL — Nueva competencia
// ══════════════════════════════════════════════════
let nivelesModal = [];

function abrirModal() {
  nivelesModal = [];
  renderNivelesModal();
  document.getElementById("mNombre").value   = "";
  document.getElementById("mLenguaje").value = "";
  document.getElementById("mFecha").value    = "";
  document.getElementById("mHora").value     = "";
  ["mCampoNombre","mCampoLenguaje","mCampoFecha"].forEach(id =>
    document.getElementById(id).classList.remove("tiene-error"));
  document.getElementById("modalNuevaComp").classList.add("visible");
}

function cerrarModal() {
  document.getElementById("modalNuevaComp").classList.remove("visible");
}

function agregarNivelModal() {
  nivelesModal.push({ nombre: `Nivel ${nivelesModal.length + 1}`, min: 10 });
  renderNivelesModal();
}

function eliminarNivelModal(i) {
  nivelesModal.splice(i, 1);
  renderNivelesModal();
}

function renderNivelesModal() {
  const lista = document.getElementById("mNivelesLista");
  if (!lista) return;
  lista.innerHTML = nivelesModal.map((n, i) => `
    <div class="m-nivel-fila">
      <input class="m-input" value="${n.nombre}" oninput="nivelesModal[${i}].nombre=this.value" placeholder="Nombre del nivel">
      <input class="m-input" type="number" value="${n.min}" oninput="nivelesModal[${i}].min=+this.value" min="1" placeholder="Min">
      <button class="btn-del-nivel" onclick="eliminarNivelModal(${i})">✕</button>
    </div>`).join("");
}

function crearCompetencia() {
  let ok = true;
  ["mCampoNombre","mCampoLenguaje","mCampoFecha"].forEach(id =>
    document.getElementById(id).classList.remove("tiene-error"));

  const nombre   = document.getElementById("mNombre").value.trim();
  const lenguaje = document.getElementById("mLenguaje").value;
  const fecha    = document.getElementById("mFecha").value;
  const hora     = document.getElementById("mHora").value;

  if (!nombre)         { document.getElementById("mCampoNombre").classList.add("tiene-error");   ok = false; }
  if (!lenguaje)       { document.getElementById("mCampoLenguaje").classList.add("tiene-error"); ok = false; }
  if (!fecha || !hora) { document.getElementById("mCampoFecha").classList.add("tiene-error");    ok = false; }
  if (!ok) return;

  competenciasData.push({
    id: competenciasData.length + 1,
    nombre, lenguaje,
    fecha: `${fecha}, ${hora}`,
    estado: "Programada",
    niveles: [...nivelesModal]
  });
  renderCompGrid();
  cerrarModal();
}

// ══════════════════════════════════════════════════
// TABS
// ══════════════════════════════════════════════════
function initTabs() {
  document.querySelectorAll(".tab[data-tab]").forEach(btn => {
    btn.addEventListener("click", () => {
      document.querySelectorAll(".tab").forEach(t => t.classList.remove("active"));
      document.querySelectorAll(".tab-panel").forEach(p => p.classList.add("hidden"));
      btn.classList.add("active");
      const panel = document.getElementById("tab-" + btn.dataset.tab);
      if (panel) panel.classList.remove("hidden");
    });
  });
}

// ══════════════════════════════════════════════════
// NAVEGACIÓN
// ══════════════════════════════════════════════════
function abrirEditar(compId, compNombre) {
  const params = new URLSearchParams({ id: compId, nombre: compNombre });
  window.location.href = `/Administrador/Competencias/competencia?${params.toString()}`;
}


function abrirRevision(compId, compNombre) {
  const params = new URLSearchParams({ id: compId, nombre: compNombre });
  window.location.href = `/Administrador/Competencias/Revision?${params.toString()}`;
}

// ══════════════════════════════════════════════════
// DATOS — Editar competencia (competencia.html)
// ══════════════════════════════════════════════════
const competencia = {
  title: "Bucles en Python",
  levels: [
    { name: "Nivel 1", min: 10 },
    { name: "Nivel 2", min: 15 },
    { name: "Nivel 3", min: 20 }
  ]
};

const problemasPorNivel = {
  0: [
    {
      titulo: "Problema 1: Sumas", guardado: true,
      enunciado: "Desarrolla un programa de sumas que reciba dos números enteros y retorne su suma.",
      codigo: "def suma(a, b):\n    return a + b",
      casos: "Entrada: 2, 3 → Salida: 5\nEntrada: 0, 0 → Salida: 0",
      capturas: [
        { archivo: "captura_alumno_01.png", alumno: "Juan Pérez" },
        { archivo: "captura_alumno_02.png", alumno: "Maria López" }
      ]
    },
    { titulo: "Problema 2: Arreglos", guardado: false, enunciado: "", codigo: "", casos: "", capturas: [] }
  ],
  1: [
    {
      titulo: "Problema 1: Ciclos", guardado: true,
      enunciado: "Escribe un FizzBuzz que imprima números del 1 al 100.",
      codigo: "for i in range(1, 101):\n    print(i)",
      casos: "",
      capturas: [{ archivo: "captura_alumno_03.png", alumno: "Carlos Ruiz" }]
    }
  ],
  2: []
};

let nivelActivo = 0;

function renderNiveles() {
  const cont = document.getElementById("nivelesContainer");
  if (!cont) return;
  cont.innerHTML = competencia.levels.map((l, i) => {
    const probs = (problemasPorNivel[i] || []).length;
    return `
      <div class="nivel-item ${i === nivelActivo ? 'activo' : ''}" onclick="seleccionarNivel(${i})">
        <div class="nivel-info">
          <span class="nivel-nombre">${l.name}</span>
          <span class="nivel-probs">Problemas: ${probs}</span>
        </div>
        <span class="nivel-tiempo-tag">${l.min} min</span>
      </div>`;
  }).join("");
}

function seleccionarNivel(i) {
  nivelActivo = i;
  renderNiveles();
  renderProblemas();
}

function renderProblemas() {
  const nivel  = competencia.levels[nivelActivo];
  document.getElementById("probTitulo").textContent = nivel.name;
  document.getElementById("probTiempo").textContent = nivel.min + " min";
  const probs  = problemasPorNivel[nivelActivo] || [];
  const footer = document.getElementById("problemasFooter");
  if (probs.length === 0) {
    document.getElementById("problemasContainer").innerHTML =
      `<p style="text-align:center;color:#94a3b8;padding:30px 0;font-size:13px;">No hay problemas en este nivel.</p>`;
    footer.style.display = "none";
  } else {
    document.getElementById("problemasContainer").innerHTML =
      probs.map((p, i) => problemaCardHTML(p, i)).join("");
    footer.style.display = "block";
  }
}

function problemaCardHTML(p, i) {
  const capturasHTML = p.capturas.length > 0
    ? p.capturas.map(c => `
        <div class="captura-item">
          <div class="captura-info">
            <div class="captura-icon">🖼️</div>
            <div>
              <span class="archivo-nombre">${c.archivo}</span>
              <span class="alumno-nombre">Alumno: ${c.alumno}</span>
            </div>
          </div>
          <div class="captura-acciones">
            <button class="btn-captura-ver">Ver</button>
            <button class="btn-captura-habilitar">Habilitar siguiente nivel</button>
          </div>
        </div>`).join("") +
      `<p class="capturas-footer-msg">Revisa la evidencia para habilitar el siguiente nivel</p>`
    : `<p class="capturas-empty">No hay capturas recibidas aún para este problema.</p>`;

  return `
    <div class="problema-card" id="card-${nivelActivo}-${i}">
      <div class="prob-header">
        <span class="prob-titulo">${p.titulo}</span>
        <span class="prob-badge ${p.guardado ? 'guardado' : 'sin-guardar'}">${p.guardado ? 'Guardado' : 'Sin guardar'}</span>
      </div>
      <div class="prob-tabs">
        <button class="prob-tab active" onclick="switchTab(this,'enunciado-${nivelActivo}-${i}','card-${nivelActivo}-${i}')">Enunciado</button>
        <button class="prob-tab"        onclick="switchTab(this,'codigo-${nivelActivo}-${i}','card-${nivelActivo}-${i}')">Código de ejemplo</button>
        <button class="prob-tab"        onclick="switchTab(this,'casos-${nivelActivo}-${i}','card-${nivelActivo}-${i}')">Casos de prueba</button>
        <button class="prob-tab"        onclick="switchTab(this,'captura-${nivelActivo}-${i}','card-${nivelActivo}-${i}')">Captura</button>
      </div>
      <textarea id="enunciado-${nivelActivo}-${i}" class="prob-panel prob-textarea">${p.enunciado}</textarea>
      <textarea id="codigo-${nivelActivo}-${i}"    class="prob-panel prob-textarea" style="display:none">${p.codigo}</textarea>
      <textarea id="casos-${nivelActivo}-${i}"     class="prob-panel prob-textarea" style="display:none">${p.casos}</textarea>
      <div id="captura-${nivelActivo}-${i}" class="prob-panel capturas-seccion" style="display:none">
        <h3 class="capturas-titulo">Capturas recibidas</h3>
        ${capturasHTML}
      </div>
    </div>`;
}

function switchTab(btn, targetId, cardId) {
  const card = document.getElementById(cardId);
  card.querySelectorAll(".prob-tab").forEach(t => t.classList.remove("active"));
  btn.classList.add("active");
  card.querySelectorAll(".prob-panel").forEach(el => el.style.display = "none");
  const target = document.getElementById(targetId);
  if (target) target.style.display = "block";
}

function agregarProblema() {
  if (!problemasPorNivel[nivelActivo]) problemasPorNivel[nivelActivo] = [];
  const n = problemasPorNivel[nivelActivo].length + 1;
  problemasPorNivel[nivelActivo].push({
    titulo: `Problema ${n}: Nuevo`, guardado: false,
    enunciado: "", codigo: "", casos: "", capturas: []
  });
  renderNiveles();
  renderProblemas();
}

// ══════════════════════════════════════════════════
// INIT
// ══════════════════════════════════════════════════
document.addEventListener("DOMContentLoaded", () => {

  // ── Enclase.html ──
  if (document.getElementById("compGrid")) {
    initTabs();
    renderCompGrid();
    renderAlumnos();
    renderSolicitudes();

    // Buscador de alumnos
    const buscador = document.getElementById("searchAlumno");
    if (buscador) buscador.addEventListener("input", e => renderAlumnos(e.target.value));

    // Botón nueva competencia
    document.getElementById("btnNuevaComp").addEventListener("click", abrirModal);

    // Cerrar modal al hacer clic fuera
    document.getElementById("modalNuevaComp").addEventListener("click", e => {
      if (e.target === e.currentTarget) cerrarModal();
    });
  }

  // ── competencia.html ──
  if (document.getElementById("nivelesContainer")) {
    const urlParams  = new URLSearchParams(window.location.search);
    const compNombre = urlParams.get("nombre") || "Competencia";
    const breadcrumb = document.getElementById("breadcrumbNombre");
    if (breadcrumb) breadcrumb.textContent = compNombre;
    renderNiveles();
    renderProblemas();
  }

});