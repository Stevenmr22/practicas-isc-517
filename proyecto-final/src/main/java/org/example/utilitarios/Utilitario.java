package org.example.utilitarios;

import com.github.f4b6a3.uuid.codec.base.Base62Codec;
import org.example.controladores.AcortadorControlador;
import org.example.entidades.Url;
import org.example.entidades.Usuario;
import org.example.servicios.ServiciosUsuario;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Utilitario {

    //Método para agregar el user en la navbar
    public static Map<String, Object> modeloBase(String username, ServiciosUsuario serviciosUsuario){
        Map<String, Object> model = new HashMap<>();
        Usuario usuario = serviciosUsuario.getUsuarioPorUsername(username);
        if(usuario == null) return model;

        model.put("username", usuario.getUsername());
        if(usuario.isAdmin()) model.put("admin", true);
        return model;
    }

    public static boolean parseCheckbox(String checkbox){
        if(checkbox != null) return checkbox.equals("on");
        return false;
    }

    public static boolean falseIfNull(Boolean bool){
        if(bool == null) return false;
        return bool;
    }

    public static String generateShortId(){
        return Base62Codec.INSTANCE.encode(UUID.randomUUID()).substring(0, Math.min(7, 22));
    }

    public static boolean urlIsValid(String url){
        try{
            new URL(url).toURI();
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public static String urlImageToBase64(String imageUrl){
        try {
            URL url = new URI(imageUrl).toURL();
            BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count;
            while ((count = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        }catch(Exception ex){
            return "";
        }
    }

    public static String extractImageUrl(String url) {
        String responsePreviewApi = AcortadorControlador.urlPreview(url);
        int startIndex = responsePreviewApi.indexOf("image\":\"") + 8;
        int endIndex = responsePreviewApi.indexOf("\",", startIndex);
        if (startIndex >= 8 && endIndex != -1 && startIndex < endIndex) {
            return responsePreviewApi.substring(startIndex, endIndex);
        } else {
            return "";
        }
    }
}
