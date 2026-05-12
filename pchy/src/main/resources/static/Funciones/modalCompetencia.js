// Tabs
  document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', () => {
      document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
      document.querySelectorAll('.tab-panel').forEach(p => p.classList.add('hidden'));
      tab.classList.add('active');
      document.getElementById('tab-' + tab.dataset.tab).classList.remove('hidden');
    });
  });

  // Modal nueva competencia
  function cerrarModal() {
    document.getElementById('modalNuevaComp').classList.remove('visible');
  }

  document.getElementById('btnNuevaComp').addEventListener('click', () => {
    document.getElementById('modalNuevaComp').classList.add('visible');
  });

  document.getElementById('modalNuevaComp').addEventListener('click', function(e) {
    if (e.target === this) cerrarModal();
  });

  // Modal editar clase
  function abrirEditarClase(id, nombre, descripcion) {
    document.getElementById('editClaseNombre').value = nombre;
    document.getElementById('editClaseDesc').value   = descripcion;
    document.getElementById('formEditarClase').action =
      '/Administrador/Clase/' + id + '/Editar';
    document.getElementById('modalEditarClase').classList.add('visible');
  }

  function cerrarEditarClase() {
    document.getElementById('modalEditarClase').classList.remove('visible');
  }

  document.getElementById('modalEditarClase').addEventListener('click', function(e) {
    if (e.target === this) cerrarEditarClase();
  });

  // Copiar texto
  function copyText(text, btn) {
    navigator.clipboard.writeText(text).catch(() => {});
    btn.style.color = '#22c55e';
    setTimeout(() => btn.style.color = '', 1200);
  }