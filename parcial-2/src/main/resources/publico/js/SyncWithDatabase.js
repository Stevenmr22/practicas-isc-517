let worker = new Worker('/js/Worker.js');
const db = new Dexie('FormApp');
db.version(1).stores({
    form: '++id, nombre, sector, nivel_escolar, latitud, longitud, username'
});

$(document).ready(function(){
    $('#sincronizar').click(function(){
        $('#sincronizar').prop('disabled', true);
        $('#listar').prop('disabled', true);
        $('#capturar').prop('disabled', true);
        try {
            db.form.each(form => {
                worker.postMessage(form);
            });
            db.form.clear();
            alert('Los datos se han sincronizado con éxito.');
        } catch (error) {
            alert('Ocurrió un error:', error);
        } finally {
            $('#sincronizar').prop('disabled', false);
            $('#listar').prop('disabled', false);
            $('#capturar').prop('disabled', false);
        }
    });
});