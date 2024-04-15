/**
 * Ejemplo de un script de service workers.
 */

//Cache activo.
var CACHE_NAME = 'mi-app-cache-v2';
//listado de
var urlsToCache = [
    'js/dexie.js',
    '/',
    '/error',
    '/capturar-respuesta',
    '/imgs/logo.png',
    '/js/SaveForm.js',
    '/offline.html'
];

urlsToExcludeFromCache = [
    '/login',
    '/logout',
    '/crear-usuario',
    '/listar-usuario'
];

//ruta para fallback.
var fallback = "/offline.html"

//representa el evento cuando se esta instalando el services workers.
self.addEventListener('install', function(event) {
    console.log('Instalando el Services Worker');
    // Utilizando las promesas para agregar los elementos definidos
    event.waitUntil(
        caches.open(CACHE_NAME) //Utilizando el api Cache definido en la variable.
            .then(function(cache) {
                console.log('Cache abierto');
                return cache.addAll(urlsToCache); //agregando todos los elementos del cache.
            })
    );
});

/**
 * Los Service Workers se actualizan pero no se activan hasta que la quede una site activo
 * que utilice la versión anterior. Para eliminar el problema, una vez activado borramos los cache no utilizado.
 */
self.addEventListener('activate', evt => {
    console.log('Activando el services worker -  Limpiando el cache no utilizado');
    evt.waitUntil(
        caches.keys().then(function(keyList) { //Recupera todos los cache activos.
            return Promise.all(keyList.map(function(key) {
                if (CACHE_NAME.indexOf(key) === -1) { //si no es el cache por defecto borramos los elementos.
                    return caches.delete(key); //borramos los elementos guardados.
                }
            }));
        })
    );
});

/**
 * Representa el evento que se dispara cuando realizamos una solicitud desde la pagina al servidor.
 * Interceptamos el mensaje y podemos verificar si lo tenemos en el cache o no.
 */
self.addEventListener('fetch', event => {
    const requestUrl = new URL(event.request.url);
    event.respondWith(
        fetchDataFromNetwork()
            .then(response => {
                if (response) return response;
                else return fetchDataFromCache();
            })
            .catch(error => {
                console.error('Error fetching data from network:', error);
                return fetchDataFromCache();
            })
    );

    function fetchDataFromNetwork() {
        return fetch(event.request)
            .then(response => {
                if (response && response.status === 200) {
                    const clonedResponse = response.clone();
                    caches.open(CACHE_NAME)
                        .then(cache => {
                            cache.put(event.request, clonedResponse);
                        });
                }
                return response;
            })
            .catch(error => {
                throw error;
            });
    }

    function fetchDataFromCache() {
        return caches.open(CACHE_NAME)
            .then(cache => {
                return cache.match(event.request)
                    .then(response => {
                        if (urlsToExcludeFromCache.includes(requestUrl.pathname)) {
                            event.respondWith(fetch(fallback));
                            return;
                        }
                        return response || fetch(fallback);
                    })
                    .catch(error => {
                        return fetch(fallback);
                    });
            });
    }
});

