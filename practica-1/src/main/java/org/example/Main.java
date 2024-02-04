package org.example;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Pedir al usuario que ingrese una URL válida
        System.out.print("Ingrese la URL: ");
        String url = scanner.nextLine();

        try {
            // Crear una instancia de HttpClient
            HttpClient httpClient = HttpClient.newHttpClient();

            // Crear una solicitud HTTP GET con la URL proporcionada
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            // Enviar la solicitud y recibir la respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Imprimir el código de estado de la respuesta HTTP
            System.out.println("Código de estado: " + response.statusCode());

            // Obtener el tipo de contenido del encabezado "Content-Type"
            String contentType = response.headers().firstValue("Content-Type").orElse("Desconocido");
            System.out.println("Tipo de contenido: " + contentType);

            // Determinar y mostrar el tipo de recurso según el tipo de contenido
            if (contentType.contains("text/html")) {
                Document document = Jsoup.parse(response.body());

                // Mostrar información sobre el documento HTML
                System.out.println("Tipo de recurso: Documento HTML");
                System.out.println("Cantidad de líneas del recurso HTML: " + response.body().split("\r\n|\r|\n").length);
                System.out.println("Cantidad de párrafos en el documento HTML: " + document.select("p").size());

                int imageCount = 0;
                Elements paragraphs = document.select("p");
                for (Element paragraph : paragraphs) {
                    imageCount += paragraph.select("img").size();
                }
                System.out.println("Cantidad de imágenes dentro de los párrafos: " + imageCount);

                // Mostrar información sobre los formularios
                Elements forms = document.select("form");
                for (int i = 0; i < forms.size(); i++) {
                    Element form = forms.get(i);
                    String method = form.attr("method").toUpperCase();
                    System.out.println("Formulario " + (i + 1) + ":");
                    System.out.println("  Método: " + method);
                    Elements inputFields = form.select("input");
                    for (Element inputField : inputFields) {
                        String inputType = inputField.attr("type");
                        System.out.println("  - Campo de tipo input: " + inputType);
                    }
                    System.out.println();

                }
                for (Element f : forms) {
                    String method = f.attr("method");
                    if (!method.equalsIgnoreCase("POST")) continue;

                    Document serverResponse = Jsoup.connect(String.valueOf(url))
                            .data("asignatura", "practica1")
                            .header("matricula-id", "10147069")
                            .post();
                    System.out.println("Respuesta del servidor: " + serverResponse.body().text());
                }
                System.out.println("Cantidad total de formularios: " + forms.size());
            }

        } catch (Exception e) {
            System.err.println("Error al procesar la solicitud: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}