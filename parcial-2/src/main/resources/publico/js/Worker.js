let webSocket;
const tiempoReconectar = 1000;

conectarWebSocket();
setInterval(verificarConexion, tiempoReconectar);

self.onmessage = function(e) {
    let form = e.data;
    webSocket.send(JSON.stringify(form));
};


function verificarConexion() {
    if(!webSocket || webSocket.readyState === 3)
        conectarWebSocket();
}

function conectarWebSocket(){
    location.protocol === 'https:' ? webSocket = new WebSocket('wss://'+location.hostname+':'+location.port+'/ws')
                                   : webSocket = new WebSocket('ws://'+location.hostname+':'+location.port+'/ws');
    webSocket.onopen = (e) => console.log('Conexión establecida - status:'+this.readyState);
    webSocket.onclose = (e) => console.log('Desconectado - status:'+this.readyState);
}