/* ================================================
   resultados.js
================================================ */

const top5Data = [
  { pos:1, nombre:"Ana",   pts:260, yo:false },
  { pos:2, nombre:"Luis",  pts:240, yo:false },
  { pos:3, nombre:"Elaine",pts:180, yo:true  },
  { pos:4, nombre:"Sofía", pts:160, yo:false },
  { pos:5, nombre:"Diego", pts:150, yo:false },
];

const desglose = [
  {
    nivel: "Nivel 1: Fundamentos",
    pts: "60 / 120 pts",
    abierto: true,
    problemas: [
      { nombre:"Problema 1: Insertar al inicio", tipo:"Obligatorio", estado:"Correcto",   puntaje:"40/40", obs:'"Bien, O(n)"',         retro:true },
      { nombre:"Problema 2: Eliminar nodo",      tipo:"Obligatorio", estado:"Incorrecto", puntaje:"10/40", obs:'"Falla caso X=4"',     retro:true },
      { nombre:"Problema 3: Buscar elemento",    tipo:"Obligatorio", estado:"Correcto",   puntaje:"10/40", obs:'"OK, pero mejora nombres"', retro:true },
      { nombre:"Problema 4: Invertir lista",     tipo:"Opcional",    estado:"No enviado", puntaje:"—",     obs:'"—"',                 retro:false },
      { nombre:"Problema 5: Detectar ciclo",     tipo:"Opcional",    estado:"Correcto",   puntaje:"40/40", obs:'"Excelente"',          retro:true },
    ]
  },
  {
    nivel: "Nivel 2: Intermedio",
    pts: "No desbloqueado",
    abierto: false,
    problemas: []
  }
];

function estadoClass(e) {
  if (e === "Correcto")   return "tag-correcto";
  if (e === "Incorrecto") return "tag-incorrecto";
  if (e === "No enviado") return "tag-closed";
  return "tag-closed";
}

function renderTop5() {
  document.getElementById("top5Sidebar").innerHTML = top5Data.map(r => `
    <div class="top5s-row ${r.yo ? 'yo' : ''}">
      <span class="top5s-pos">${r.pos}.</span>
      <span class="top5s-nom">${r.nombre}${r.yo ? ' (tú)' : ''}</span>
      <span class="top5s-pts">${r.pts} pts</span>
    </div>
  `).join("");
}

function renderDesglose() {
  document.getElementById("desgloseContainer").innerHTML = desglose.map((n, ni) => `
    <div class="nivel-desglose">
      <div class="nivel-desglose-header" onclick="toggleNivel(${ni})">
        <div class="nivel-desglose-nombre">
          <span>${n.abierto ? "▾" : "▸"}</span>
          ${n.nivel}
        </div>
        <span class="nivel-desglose-pts">${n.pts}</span>
      </div>
      ${n.abierto && n.problemas.length ? `
        <table class="desglose-table">
          <thead>
            <tr>
              <th>Problema</th>
              <th>Tipo</th>
              <th>Estado</th>
              <th>Puntaje</th>
              <th>Observación</th>
            </tr>
          </thead>
          <tbody>
            ${n.problemas.map(p => `
              <tr>
                <td>${p.nombre}</td>
                <td><span class="tag ${p.tipo === 'Obligatorio' ? 'tag-oblig' : 'tag-opc'}" style="font-size:.72rem">${p.tipo}</span></td>
                <td><span class="tag ${estadoClass(p.estado)}" style="font-size:.72rem">${p.estado}</span></td>
                <td style="font-weight:700">${p.puntaje}</td>
                <td>
                  ${p.obs}
                  ${p.retro ? `<a class="obs-link">Ver retroalimentación</a>` : ""}
                </td>
              </tr>
            `).join("")}
          </tbody>
        </table>
      ` : (!n.abierto ? `
        <div style="padding:.7rem 1.2rem;font-size:.83rem;color:var(--color-texto-suave)">
          No desbloqueado &mdash; 0 / 120 pts
        </div>
      ` : "")}
    </div>
  `).join("");
}

function toggleNivel(i) {
  desglose[i].abierto = !desglose[i].abierto;
  renderDesglose();
}

renderTop5();
renderDesglose();