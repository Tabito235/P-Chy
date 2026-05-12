/* ================================================
   verProblema.js
================================================ */

/* ── TIMER ── */
let timerSecs2 = 8 * 60 + 12;

function tickTimer2() {
  if (timerSecs2 <= 0) return;
  timerSecs2--;
  const m = Math.floor(timerSecs2 / 60).toString().padStart(2,"0");
  const s = (timerSecs2 % 60).toString().padStart(2,"0");
  const el = document.getElementById("timer2");
  if (el) {
    el.textContent = `${m}:${s}`;
    el.style.color = timerSecs2 < 120 ? "#ef4444" : "var(--color-primario)";
  }
}

setInterval(tickTimer2, 1000);

/* ── UPLOAD SIMULADO ── */
function simularUpload(areaId, labelId) {
  const area  = document.getElementById(areaId);
  const label = document.getElementById(labelId);
  area.classList.add("done");
  label.classList.add("done");
  label.textContent = areaId === "uploadCaptura" ? "✓ captura_problema2.png" : "✓ problema2.txt";
}

/* ── ENVIAR SOLUCIÓN ── */
function enviarSolucion() {
  const toast = document.getElementById("toastEnvio");
  toast.classList.add("show");
  setTimeout(() => {
    toast.classList.remove("show");
    window.location.href = "/verNivel";
  }, 2200);
}