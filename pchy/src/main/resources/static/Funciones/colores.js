
    const opciones = document.querySelectorAll('.color-opcion');
    const inputColor = document.getElementById('colorElegido');

    opciones.forEach(btn => {
      btn.addEventListener('click', () => {
        opciones.forEach(b => b.classList.remove('seleccionado'));
        btn.classList.add('seleccionado');
        inputColor.value = btn.dataset.color;
      });
    });


    const toast = document.getElementById('toastExito');
    if (toast) {
      setTimeout(() => toast.classList.add('oculto'), 3500);
    }