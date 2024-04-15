package org.example.grpc;

import dev.morphia.query.filters.Filters;
import io.grpc.stub.StreamObserver;
import org.example.entidades.Usuario;
import org.example.servicios.ServiciosUrl;
import org.example.servicios.ServiciosUsuario;
import urlrn.Url;
import urlrn.UrlServiceGrpc;
import com.google.protobuf.util.Timestamps;

import java.util.List;

import static org.example.utilitarios.Utilitario.generateShortId;
import static org.example.utilitarios.Utilitario.urlIsValid;

public class UrlServiceImpl extends UrlServiceGrpc.UrlServiceImplBase  {
    private final ServiciosUrl serviciosUrl = ServiciosUrl.getInstancia();
    private final ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();

    @Override
    public void obtenerUrls(Url.urlRequest request, StreamObserver<Url.listadoUrls> responseObserver){
        String username = request.getUsername();
        List<org.example.entidades.Url> urlsEntidades = serviciosUrl.getUrlByUsername(username);

        Url.listadoUrls.Builder listadoUrlsBuilder = Url.listadoUrls.newBuilder();
        for (org.example.entidades.Url urlEntidad : urlsEntidades) {
            Url.urlResponse urlResponse = Url.urlResponse.newBuilder()
                    .setId("https://www.sh.stevenapp.me/r/"+urlEntidad.getId()) // TODO: Cambiar en todo el programa por esta URL
                    .setUrlOriginal(urlEntidad.getUrlOriginal())
                    .setTitulo(urlEntidad.getTitulo())
                    .setUsernameCreador(urlEntidad.getUsernameCreador())
                    .setFechaCreacion(Timestamps.fromMillis(urlEntidad.getFechaCreacion().getTime()))
                    .build();

            listadoUrlsBuilder.addUrls(urlResponse);
        }

        Url.listadoUrls listadoUrls = listadoUrlsBuilder.build();

        responseObserver.onNext(listadoUrls);
        responseObserver.onCompleted();
    }

    @Override
    public void crearUrl(Url.urlResponse request, StreamObserver<Url.urlResponse> responseObserver) {
        Usuario usuario = serviciosUsuario.getUsuarioPorUsername(request.getUsernameCreador());
        if(usuario == null){
            responseObserver.onError(new Exception("Usuario no encontrado"));
            return;
        }
        if(!urlIsValid(request.getUrlOriginal())){
            responseObserver.onError(new Exception("URL no aceptada"));
            return;
        }

        String id = generateShortId();
        while(serviciosUrl.find().filter(Filters.eq("id", id)).first() != null) {
            id = generateShortId();
        }

        org.example.entidades.Url url = new org.example.entidades.Url(request.getUrlOriginal(), id, usuario.getUsername(), request.getTitulo());
        serviciosUrl.crear(url);

        Url.urlResponse response = Url.urlResponse.newBuilder()
                .setId("https://www.sh.stevenapp.me/r/"+id)
                .setUrlOriginal(request.getUrlOriginal())
                .setTitulo(request.getTitulo())
                .setUsernameCreador(request.getUsernameCreador())
                .setFechaCreacion(Timestamps.fromMillis(url.getFechaCreacion().getTime()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
