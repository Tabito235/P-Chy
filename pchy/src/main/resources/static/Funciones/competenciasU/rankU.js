/* ================================================
   rankings.js
================================================ */

const rankData = [
  { pos:1,  nombre:"Ana",      emoji:"🥇", pts:260,  comps:9, racha:5, act:"3 días",  yo:false },
  { pos:2,  nombre:"Luis",     emoji:"🥈", pts:240,  comps:8, racha:5, act:"4 días",  yo:false },
  { pos:3,  nombre:"Sofía",    emoji:"🥉", pts:180,  comps:8, racha:5, act:"3 días",  yo:false },
  { pos:4,  nombre:"Diego",    emoji:"",   pts:160,  comps:7, racha:5, act:"4 días",  yo:false },
  { pos:5,  nombre:"Diego",    emoji:"",   pts:150,  comps:7, racha:5, act:"2 días",  yo:false },
  { pos:6,  nombre:"Elaine",   emoji:"",   pts:1150, comps:8, racha:5, act:"5 días",  yo:false },
  { pos:7,  nombre:"Pichy",    emoji:"",   pts:960,  comps:6, racha:4, act:"6 días",  yo:true  },
  { pos:8,  nombre:"Alexandra",emoji:"",   pts:900,  comps:4, racha:4, act:"4 días",  yo:false },
  { pos:9,  nombre:"Riana",    emoji:"",   pts:630,  comps:4, racha:5, act:"4 días",  yo:false },
  { pos:10, nombre:"Sofía",    emoji:"",   pts:690,  comps:3, racha:5, act:"5 días",  yo:false },
  { pos:11, nombre:"Diego",    emoji:"",   pts:500,  comps:3, racha:4, act:"3 días",  yo:false },
  { pos:12, nombre:"Pichy",    emoji:"",   pts:460,  comps:3, racha:3, act:"3 días",  yo:false },
  { pos:13, nombre:"Riana",    emoji:"",   pts:430,  comps:3, racha:3, act:"3 días",  yo:false },
];

let filteredData = [...rankData];

function posClass(pos) {
  if (pos === 1) return "rank-pos top1";
  if (pos === 2) return "rank-pos top2";
  if (pos === 3) return "rank-pos top3";
  return "rank-pos";
}

function renderRanking(data) {
  document.getElementById("rankBody").innerHTML = data.map(r => `
    <tr class="${r.yo ? 'yo' : ''}">
      <td><span class="${posClass(r.pos)}">${r.pos}</span></td>
      <td>
        <div class="rank-nombre">
          <div class="rank-avatar-sm"></div>
          ${r.nombre} ${r.emoji}
        </div>
      </td>
      <td class="rank-pts">${r.pts.toLocaleString()}</td>
      <td class="rank-comp">${r.comps}</td>
      <td class="rank-racha">🔥 ${r.racha}</td>
      <td class="rank-act">${r.act}</td>
    </tr>
  `).join("");
}

/* ── BÚSQUEDA ── */
document.getElementById("searchRank").addEventListener("input", function() {
  const q = this.value.toLowerCase();
  const filtered = rankData.filter(r => r.nombre.toLowerCase().includes(q));
  renderRanking(filtered);
});

/* ── FILTROS ── */
document.getElementById("filtroClase").addEventListener("change", () => renderRanking(rankData));
document.getElementById("filtroPeriodo").addEventListener("change", () => renderRanking(rankData));

renderRanking(rankData);