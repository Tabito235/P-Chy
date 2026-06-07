const langBtns = document.querySelectorAll('.toggle-btn');
let lenguajeActivo = 'python';

langBtns.forEach(btn => {
  btn.addEventListener('click', () => {
    // Quitar el active
    langBtns.forEach(b => b.classList.remove('active'));
    // poner el active
    btn.classList.add('active');
    
    // Guardar lenguaje seleccionado
    lenguajeActivo = btn.dataset.lang;
    
    // Actualizar el tag de lenguaje
    const langTag = document.querySelector('.tag-python');
    if (langTag) {
      langTag.textContent = lenguajeActivo === 'python' ? 'Python' : 'C++';
    }
    
    console.log('Lenguaje seleccionado:', lenguajeActivo);

  });
});

// Para subir los archivos

// Crear área de upload
function setupUploadArea(areaId, inputId, filenameId) {
  const area = document.getElementById(areaId);
  const input = document.getElementById(inputId);
  const filenameSpan = document.getElementById(filenameId);
  
  if (!area || !input) return;
  
  // Click en el área
  area.addEventListener('click', () => {
    input.click();
  });
  
  // Cuando se selecciona un archivo
  input.addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (file && filenameSpan) {
      filenameSpan.textContent = `📎 ${file.name}`;
      filenameSpan.style.color = 'var(--color-primario)';
    }
  });
  
  area.addEventListener('dragover', (e) => {
    e.preventDefault();
    area.classList.add('drag-over');
  });
  
  area.addEventListener('dragleave', () => {
    area.classList.remove('drag-over');
  });
  
  area.addEventListener('drop', (e) => {
    e.preventDefault();
    area.classList.remove('drag-over');
    const file = e.dataTransfer.files[0];
    if (file) {
      input.files = e.dataTransfer.files;
      if (filenameSpan) {
        filenameSpan.textContent = `📎 ${file.name}`;
        filenameSpan.style.color = 'var(--color-primario)';
      }
    }
  });
}

// Areas de upload
setupUploadArea('uploadCapturaArea', 'inputCaptura', 'nombreCaptura');
setupUploadArea('uploadTxtArea', 'inputTxt', 'nombreSugerido');

// Boton de envio y guardado

document.getElementById('btnEnviar')?.addEventListener('click', () => {
  const comentario = document.getElementById('comentario')?.value || '';
  alert(`Desafío enviado${comentario ? ' con comentario: ' + comentario : ''}`);
});

document.getElementById('btnGuardar')?.addEventListener('click', () => {
  alert('Desafío guardado para después');
});