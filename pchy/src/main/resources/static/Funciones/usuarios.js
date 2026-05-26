
  // Abrir modal con datos del usuario
  function abrirModal(btn) {
    const id     = btn.dataset.id;
    const nombre = btn.dataset.nombre;
    const rolAct = btn.dataset.rol;

    document.getElementById('modalNombre').textContent = nombre;
    document.getElementById('selectRol').value = rolAct;
    document.getElementById('formRol').action =
      '/Administrador/Usuario/' + id + '/Rol';
    document.getElementById('modalRol').classList.add('visible');
  }

  function cerrarModal() {
    document.getElementById('modalRol').classList.remove('visible');
  }

  document.getElementById('modalRol').addEventListener('click', function(e) {
    if (e.target === this) cerrarModal();
  });

  // Buscador
  document.getElementById('buscador').addEventListener('input', function() {
    const q = this.value.toLowerCase();
    document.querySelectorAll('#tablaBody tr').forEach(tr => {
      const nombre = tr.dataset.nombre?.toLowerCase() || '';
      const correo = tr.dataset.correo?.toLowerCase() || '';
      tr.style.display = (nombre.includes(q) || correo.includes(q)) ? '' : 'none';
    });
  });