// This Script is used to store locally the form data

$(document).ready(function () {
    const db = new Dexie('FormApp');
    db.version(1).stores({
        form: '++id, nombre, sector, nivel_escolar, latitud, longitud, username'
    });
    let editId = -1;

    window.addEventListener('online', function() {
        $('#sincronizar').prop('disabled', false);
    });

    window.addEventListener('offline', function() {
        $('#sincronizar').prop('disabled', true);
    });

    $(document).on('submit', '#formulario', function(e){
        e.preventDefault();
        const formData = $(this).serializeArray();
        $('#formulario').trigger('reset');

        //Para editar en indexedDB
        if(editId !== -1){
            db.form.update(parseInt(editId), {
                nombre: formData[0].value,
                sector: formData[1].value,
                nivel_escolar: formData[2].value
            });
            alert('Respuesta actualizada correctamente');
            $('#capturar').text('Capturar respuesta');
            editId = -1;
            return;
        }

        //Para subir a indexedDB
        getPosicion().then(posicionActual =>{
            db.form.put({
                nombre: formData[0].value,
                sector: formData[1].value,
                nivel_escolar: formData[2].value,
                latitud: posicionActual[0],
                longitud: posicionActual[1],
                username: localStorage.getItem('username')
            });
        }).then(() => alert('Respuesta guardada correctamente')).catch(e => alert(e));
    });

    $(document).on('click', '#listar', function(){
        const tabla = $('#cuerpo-tabla');
        tabla.empty();

        db.form.each(form => {
            tabla.append(
                `<tr>
                    <td class="d-none form-id">${form.id}</td>
                    <td class="text-truncate small-font-size" style="max-width: 150px;">${form.nombre}</td>
                    <td class="text-truncate small-font-size" style="max-width: 150px;">${form.sector}</td>
                    <td class="small-font-size">${form.nivel_escolar}</td>
                    <td class="dropdown dropstart">
                        <button class="btn btn-sm btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                        </button>
                        <ul class="dropdown-menu dropdown-menu-dark" style="min-width: 100px;">
                            <li><btn class="dropdown-item editar">Editar</btn></li>
                            <li><btn class="dropdown-item eliminar" >Eliminar</btn></li>
                        </ul>
                    </td>
                </tr>`
            );
        });
    });

    $(document).on('click', '.eliminar', function(){
        const id = $(this).closest('tr').find('.form-id').text();
        db.form.delete(parseInt(id));
        $(this).closest('tr').remove();
    })

    $(document).on('click', '.editar', function(){
        const id = $(this).closest('tr').find('.form-id').text();
        editId = id;
        db.form.get(parseInt(id), form => {
            $('#nombre').val(form.nombre);
            $('#sector').val(form.sector);
            $('#nivel_escolar').val(form.nivel_escolar);
            $('#id').val(form.id);
        });
        $('#capturar').text('Editar respuesta');
        $('#listar').click();
    });
});

getPosicion = function(){
    return new Promise((resolve, reject) => {
        if(!navigator.geolocation) {
            reject('Tu navegador no soporta geolocalización');
        }
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const {latitude, longitude} = position.coords;
                resolve([latitude, longitude]);
            },
            (error) => {
                if (error.code !== error.PERMISSION_DENIED) {
                    reject('Error al obtener la posición, intenta nuevamente.');
                    return [];
                }
                reject('Debes activar la geolocalización para poder enviar el formulario.');
            }
        );
    });
}