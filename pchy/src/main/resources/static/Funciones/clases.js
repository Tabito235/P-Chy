
/* ================================================
   DATOS
================================================ */
const alumnos = [
  { name:"Ana López",     user:"@username" },
  { name:"Juan Pérez",    user:"@username" },
  { name:"María Gómez",   user:"@username" },
  { name:"Pedro Silva",   user:"@username" },
  { name:"Carmen Díaz",   user:"@username" },
  { name:"Miguel Ruiz",   user:"@username" },
  { name:"Luis García",   user:"@username" },
  { name:"Sofía Torres",  user:"@username" },
  { name:"Javier Vargas", user:"@username" },
];

const solicitudes = [
  { name:"Ana López",     user:"@ana_lopez" },
  { name:"Sofía Torres",  user:"@sofia_t"  },
  { name:"Elena Sánchez", user:"@elena_s"  },
  { name:"Marcos Reyes",  user:"@marcos_r" },
];

let competencias = [
  { id:1, title:"Bucles en Python",           lang:"Python", date:"Oct 26, 2026, 10:00 AM", status:"Activa",     levels:[{name:"Nivel 1",min:10},{name:"Nivel 2",min:15},{name:"Nivel 3",min:20}] },
  { id:2, title:"Arreglos en C++",            lang:"C++",    date:"Nov 1, 2026, 2:00 PM",   status:"Programada", levels:[] },
  { id:3, title:"Funciones",                  lang:"Python", date:"Oct 20, 2026, 9:00 AM",  status:"Cerrada",    levels:[{name:"Nivel 1",min:15},{name:"Nivel 2",min:20}] },
  { id:4, title:"Recursividad",               lang:"C++",    date:"Nov 5, 2026, 11:30 AM",  status:"Activa",     levels:[{name:"Nivel 1",min:20}] },
  { id:5, title:"POO básica",                 lang:"Python", date:"Nov 10, 2026, 3:00 PM",  status:"Programada", levels:[{name:"Nivel 1",min:15},{name:"Nivel 2",min:25}] },
  { id:6, title:"Estructuras: Pilas y Colas", lang:"C++",    date:"Nov 15, 2026, 1:00 PM",  status:"Activa",     levels:[{name:"Nivel 1",min:10},{name:"Nivel 2",min:15},{name:"Nivel 3",min:20}] },
];

// Problemas mock por competencia+nivel
const problemasMock = {
  default: [
    { id:1, titulo:"Problema 1: Sumas",    guardado:true,  enunciado:"Desarrolla un programa que permita sumar dos números enteros ingresados por el usuario y muestre el resultado." },
    { id:2, titulo:"Problema 2: Arreglos", guardado:false, enunciado:"Implementa una función que reciba un arreglo de números enteros y devuelva el promedio de sus elementos." },
  ]
};

const langColor = { Python:"#818cf8", "C++":"#34d399", Java:"#fb923c", JavaScript:"#facc15" };
const statusColor = {
  Activa:     { bg:"#dcfce7", color:"#15803d" },
  Programada: { bg:"#fef9c3", color:"#a16207" },
  Cerrada:    { bg:"#f1f5f9", color:"#64748b"  },
};

/* ================================================
   ESTADO LOCAL
================================================ */
let nivelModalCount = 0;
let competenciaActual = null;
let nivelActivoIndex  = 0;

/* ================================================
   RENDER ALUMNOS
================================================ */
function renderAlumnos(filter="") {
  const grid = document.getElementById("alumnosGrid");
  const filtered = alumnos.filter(a => a.name.toLowerCase().includes(filter.toLowerCase()));
  grid.innerHTML = filtered.map(a => `
    <div class="alumno-card">
      <div class="avatar"></div>
      <div>
        <div class="alumno-name">${a.name}</div>
        <div class="alumno-user">${a.user}</div>
        <span class="status-badge activo">Activo</span>
      </div>
    </div>
  `).join("");
}

function renderSolicitudes() {
  document.getElementById("solicitudesList").innerHTML =
    solicitudes.slice(0,3).map(s => solicitudHTML(s)).join("");
  document.getElementById("solicitudesFull").innerHTML =
    solicitudes.map(s => solicitudHTML(s, true)).join("");
}

function solicitudHTML(s, full=false) {
  return `
    <div class="solicitud-item ${full ? 'full' : ''}">
      <div class="avatar sm"></div>
      <div class="sol-info">
        <div class="alumno-name">${s.name}</div>
        ${full ? `<div class="alumno-user">${s.user}</div>` : ""}
      </div>
      <div class="sol-actions">
        <button class="btn-aceptar">Aceptar</button>
        <button class="btn-rechazar">Rechazar</button>
      </div>
    </div>
  `;
}

/* ================================================
   RENDER COMPETENCIAS (grid)
================================================ */
function renderCompetencias() {
  document.getElementById("compGrid").innerHTML = competencias.map(c => {
    const sc = statusColor[c.status];
    const lc = langColor[c.lang] || "#a78bfa";
    const levelsHTML = c.levels.length
      ? c.levels.map(l => `<div class="nivel">${l.name}: ${l.min} min</div>`).join("")
      : `<div class="nivel muted">Sin niveles</div>`;
    return `
      <div class="card comp-card">
        <div class="comp-top">
          <span class="comp-title">${c.title}</span>
          <span class="lang-tag" style="background:${lc}22;color:${lc}">${c.lang}</span>
        </div>
        <div class="comp-date">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
          ${c.date}
        </div>
        <span class="status-pill" style="background:${sc.bg};color:${sc.color}">${c.status}</span>
        <div class="niveles">${levelsHTML}</div>
        <div class="comp-actions">
          <button class="btn-enter" onclick="abrirEditar(${c.id})">Entrar</button>
          <button class="btn-outline" onclick="abrirEditar(${c.id})">Ver niveles y crear niveles</button>
          <button class="btn-outline" onclick="abrirEditar(${c.id})">Editar</button>
          <button class="menu-btn">⋮</button>
        </div>
      </div>
    `;
  }).join("");
}

/* ================================================
   NAVEGACIÓN ENTRE VISTAS
================================================ */
function abrirEditar(id) {
  competenciaActual = competencias.find(c => c.id === id);
  if (!competenciaActual) return;

  document.getElementById("breadcrumbNombre").textContent = competenciaActual.title;
  document.getElementById("vista-grid").style.display = "none";
  document.getElementById("vista-editar").classList.add("visible");

  nivelActivoIndex = 0;
  renderNivelesPanel();
  renderProblemas();
}

function volverAGrid() {
  document.getElementById("vista-editar").classList.remove("visible");
  document.getElementById("vista-grid").style.display = "block";
  competenciaActual = null;
}

/* ================================================
   PANEL DE NIVELES (vista editar)
================================================ */
function renderNivelesPanel() {
  const cont = document.getElementById("nivelesPanel");
  if (!competenciaActual.levels.length) {
    cont.innerHTML = `<p style="font-size:13px;color:#6B6B80;text-align:center;padding:12px 0">Sin niveles. Añade el primero.</p>`;
    return;
  }
  cont.innerHTML = competenciaActual.levels.map((l, i) => `
    <div class="nivel-item ${i === nivelActivoIndex ? 'activo' : ''}" onclick="seleccionarNivel(${i})">
      <div class="nivel-item-left">
        <span class="nivel-nombre">${l.name}</span>
        <span class="nivel-tiempo-tag">${l.min} min</span>
      </div>
      <span class="nivel-problemas">Problemas: 3</span>
    </div>
  `).join("") + `
    <button class="btn-add-nivel-panel" onclick="agregarNivelPanel()">
      <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
      Añadir nivel
    </button>
  `;
}

function seleccionarNivel(i) {
  nivelActivoIndex = i;
  renderNivelesPanel();
  renderProblemas();
}

function agregarNivelPanel() {
  if (!competenciaActual) return;
  const n = competenciaActual.levels.length + 1;
  competenciaActual.levels.push({ name: `Nivel ${n}`, min: 10 });
  nivelActivoIndex = competenciaActual.levels.length - 1;
  renderNivelesPanel();
  renderProblemas();
}

/* ================================================
   PANEL DE PROBLEMAS (vista editar)
================================================ */
function renderProblemas() {
  if (!competenciaActual || !competenciaActual.levels.length) {
    document.getElementById("probPanelTitulo").textContent = "—";
    document.getElementById("probPanelTiempo").textContent = "—";
    document.getElementById("problemasContainer").innerHTML =
      `<p style="font-size:13px;color:#6B6B80">Selecciona o crea un nivel primero.</p>`;
    return;
  }

  const nivel = competenciaActual.levels[nivelActivoIndex];
  document.getElementById("probPanelTitulo").textContent = nivel.name;
  document.getElementById("probPanelTiempo").textContent = nivel.min + " min";

  const probs = problemasMock.default;
  document.getElementById("problemasContainer").innerHTML = probs.map((p, i) =>
    problemaCardHTML(p, i)
  ).join("");
}

function problemaCardHTML(p, i) {
  return `
    <div class="problema-card" id="prob-${i}">
      <div class="prob-header">
        <span class="prob-titulo">${p.titulo}</span>
        <span class="prob-badge ${p.guardado ? 'guardado' : 'sin-guardar'}">
          ${p.guardado ? 'Guardado' : 'Sin guardar'}
        </span>
      </div>
      <div class="prob-tabs">
        <button class="prob-tab active">Enunciado</button>
        <button class="prob-tab">Código de ejemplo</button>
        <button class="prob-tab">Casos de prueba</button>
        <button class="prob-tab">Captura</button>
      </div>
      <textarea class="prob-textarea">${p.enunciado}</textarea>
      <div class="prob-acciones">
        <button class="btn-prob-cancel">Cancelar</button>
        <button class="btn-prob-save">Guardar</button>
        <button class="btn-prob-edit">Editar</button>
      </div>
    </div>
  `;
}

function agregarProblema() {
  const n = document.querySelectorAll(".problema-card").length + 1;
  const nuevo = { id: n, titulo: `Problema ${n}: Nuevo`, guardado: false, enunciado: "" };
  const cont = document.getElementById("problemasContainer");
  const div = document.createElement("div");
  div.innerHTML = problemaCardHTML(nuevo, `new-${n}`);
  cont.appendChild(div.firstElementChild);
}

/* ================================================
   MODAL — Nueva competencia
================================================ */
let nivelesModal = [];

function abrirModal() {
  nivelesModal = [];
  nivelModalCount = 0;
  document.getElementById("mNivelesLista").innerHTML = "";
  document.getElementById("mNombre").value    = "";
  document.getElementById("mLenguaje").value  = "";
  document.getElementById("mFecha").value     = "";
  document.getElementById("mHora").value      = "";
  limpiarErrores();
  document.getElementById("modalNuevaComp").classList.add("visible");
}

function cerrarModal() {
  document.getElementById("modalNuevaComp").classList.remove("visible");
}

// Cierra al hacer click fuera de la tarjeta
document.getElementById("modalNuevaComp").addEventListener("click", function(e) {
  if (e.target === this) cerrarModal();
});

function agregarNivelModal() {
  nivelModalCount++;
  const id = nivelModalCount;
  nivelesModal.push({ id, name: `Nivel ${id}`, min: 10 });

  const fila = document.createElement("div");
  fila.className = "m-nivel-fila";
  fila.id = `mNivelFila-${id}`;
  fila.innerHTML = `
    <div>
      <div class="m-tiempo-label">Nombre</div>
      <input class="m-input" type="text" value="Nivel ${id}" placeholder="Nivel ${id}"
        oninput="nivelesModal.find(n=>n.id===${id}).name=this.value">
    </div>
    <div>
      <div class="m-tiempo-label">Tiempo (min)</div>
      <input class="m-input" type="number" value="10" min="1" style="width:100%"
        oninput="nivelesModal.find(n=>n.id===${id}).min=+this.value">
    </div>
    <button type="button" class="btn-del-nivel" onclick="eliminarNivelModal(${id})" title="Eliminar">
      <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
    </button>
  `;
  document.getElementById("mNivelesLista").appendChild(fila);
}

function eliminarNivelModal(id) {
  nivelesModal = nivelesModal.filter(n => n.id !== id);
  document.getElementById(`mNivelFila-${id}`)?.remove();
}

/* ── Validación y creación ── */
function limpiarErrores() {
  ["mCampoNombre","mCampoLenguaje","mCampoFecha"].forEach(id => {
    document.getElementById(id)?.classList.remove("tiene-error");
    document.getElementById(id)?.querySelector(".m-input, .m-select")?.classList.remove("error");
  });
}

function crearCompetencia() {
  limpiarErrores();
  const nombre   = document.getElementById("mNombre").value.trim();
  const lenguaje = document.getElementById("mLenguaje").value;
  const fecha    = document.getElementById("mFecha").value;
  const hora     = document.getElementById("mHora").value;
  let valido = true;

  if (!nombre) {
    marcarError("mCampoNombre", "mNombre");
    valido = false;
  }
  if (!lenguaje) {
    marcarError("mCampoLenguaje", "mLenguaje");
    valido = false;
  }
  if (!fecha || !hora) {
    marcarError("mCampoFecha");
    valido = false;
  }

  if (!valido) return;

  // Construir objeto y agregarlo al array
  const nueva = {
    id: competencias.length + 1,
    title: nombre,
    lang: lenguaje,
    date: formatearFechaHora(fecha, hora),
    status: "Programada",
    levels: nivelesModal.map((n, i) => ({ name: `Nivel ${i+1}`, min: n.min })),
  };

  competencias.push(nueva);
  cerrarModal();
  renderCompetencias();

  // Ir directo a la vista de editar de la nueva competencia
  abrirEditar(nueva.id);
}

function marcarError(campoId, inputId = null) {
  document.getElementById(campoId)?.classList.add("tiene-error");
  if (inputId) document.getElementById(inputId)?.classList.add("error");
}

function formatearFechaHora(fecha, hora) {
  const [y, m, d] = fecha.split("-");
  const meses = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
  const [h, min] = hora.split(":");
  const hNum = parseInt(h);
  const ampm = hNum >= 12 ? "PM" : "AM";
  const h12  = hNum % 12 || 12;
  return `${meses[+m-1]} ${+d}, ${y}, ${h12}:${min} ${ampm}`;
}

/* ================================================
   TABS PRINCIPALES
================================================ */
document.querySelectorAll(".tab").forEach(tab => {
  tab.addEventListener("click", () => {
    document.querySelectorAll(".tab").forEach(t => t.classList.remove("active"));
    document.querySelectorAll(".tab-panel").forEach(p => p.classList.add("hidden"));
    tab.classList.add("active");
    const panel = document.getElementById("tab-" + tab.dataset.tab);
    panel.classList.remove("hidden");
    // Al volver a competencias, siempre mostrar el grid
    if (tab.dataset.tab === "competencias") {
      volverAGrid();
    }
  });
});

document.getElementById("searchAlumno").addEventListener("input", e => renderAlumnos(e.target.value));
document.getElementById("btnNuevaComp").addEventListener("click", abrirModal);

function copyText(text, btn) {
  navigator.clipboard.writeText(text).catch(()=>{});
  btn.style.color = "#22c55e";
  setTimeout(() => btn.style.color = "", 1200);
}

/* ── INIT ── */
renderAlumnos();
renderSolicitudes();
renderCompetencias();