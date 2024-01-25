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
            determineResourceType(contentType);

            // Contar y mostrar la cantidad de líneas del recurso HTML
            if (contentType.contains("text/html")) {
                int lineCount = countLines(response.body());
                System.out.println("Cantidad de líneas del recurso HTML: " + lineCount);

                // Contar y mostrar la cantidad de párrafos en el documento HTML
                int paragraphCount = countParagraphs(response.body());
                System.out.println("Cantidad de párrafos en el documento HTML: " + paragraphCount);

                // Contar y mostrar la cantidad de imágenes dentro de los párrafos
                int imageCount = countImagesInParagraphs(response.body());
                System.out.println("Cantidad de imágenes dentro de los párrafos: " + imageCount);

                // Mostrar la cantidad de formularios y detalles de cada formulario
                countForms(response.body());
            }

        } catch (Exception e) {
            System.err.println("Error al procesar la solicitud: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void determineResourceType(String contentType) {
        // Lógica para determinar el tipo de recurso según el tipo de contenido
        if (contentType.contains("text/html")) {
            System.out.println("Tipo de recurso: Documento HTML");
        } else if (contentType.contains("application/pdf")) {
            System.out.println("Tipo de recurso: Documento PDF");
        } else if (contentType.contains("image")) {
            System.out.println("Tipo de recurso: Imagen");
        } else {
            System.out.println("Tipo de recurso: Desconocido");
        }
    }

    private static int countLines(String text) {
        // Contar las líneas en el texto proporcionado
        return text.split("\r\n|\r|\n").length;
    }

    private static int countParagraphs(String html) {
        // Utilizar JSoup para analizar el documento HTML y contar los párrafos
        Document document = Jsoup.parse(html);
        Elements paragraphs = document.select("p");
        return paragraphs.size();
    }

    private static int countImagesInParagraphs(String html) {
        // Utilizar JSoup para analizar el documento HTML y contar las imágenes dentro de los párrafos
        Document document = Jsoup.parse(html);
        Elements paragraphs = document.select("p");
        int imageCount = 0;

        for (Element paragraph : paragraphs) {
            // Contar las imágenes dentro de cada párrafo
            Elements images = paragraph.select("img");
            imageCount += images.size();
        }

        return imageCount;
    }

    private static void countForms(String html) {
        // Utilizar JSoup para analizar el documento HTML y contar los formularios
        Document document = Jsoup.parse(html);
        Elements forms = document.select("form");

        for (int i = 0; i < forms.size(); i++) {
            Element form = forms.get(i);

            // Obtener el valor del atributo "method" del formulario
            String method = form.attr("method").toUpperCase();

            // Mostrar información sobre el formulario
            System.out.println("Formulario " + (i + 1) + ":");
            System.out.println("  Método: " + method);

            // Obtener y mostrar campos de tipo input y sus tipos dentro del formulario
            Elements inputFields = form.select("input");
            for (Element inputField : inputFields) {
                String inputType = inputField.attr("type");
                System.out.println("  - Campo de tipo input: " + inputType);
            }

            System.out.println(); // Separador entre formularios
        }

        System.out.println("Cantidad total de formularios: " + forms.size());
    }
}
