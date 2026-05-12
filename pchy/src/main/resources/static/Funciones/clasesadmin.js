
  // Búsqueda
  document.getElementById('buscador').addEventListener('input', function () {
    const q = this.value.toLowerCase();
    document.querySelectorAll('.card').forEach(card => {
      const nombre = card.dataset.nombre?.toLowerCase() || '';
      card.style.display = nombre.includes(q) ? '' : 'none';
    });
  });

  // Dropdown toggle
  document.addEventListener('click', e => {
    const btn = e.target.closest('.menu-btn');
    document.querySelectorAll('.dropdown').forEach(d => d.classList.remove('open'));
    if (btn) {
      e.stopPropagation();
      btn.nextElementSibling.classList.add('open');
    }
  });

  // Copiar texto
  function copyText(text, btn) {
    navigator.clipboard.writeText(text).catch(() => {});
    btn.style.color = '#22c55e';
    setTimeout(() => btn.style.color = '', 1200);
  }

  // Modal editar
function abrirEditar(id, nombre, descripcion) {
    document.getElementById('editNombre').value = nombre;
    document.getElementById('editDescripcion').value = descripcion || '';
    document.getElementById('formEditar').action = '/Administrador/Clase/' + id + '/Editar';
    document.getElementById('modalEditar').classList.add('visible');
}

  function cerrarModalEditar() {
    document.getElementById('modalEditar').classList.remove('visible');
  }