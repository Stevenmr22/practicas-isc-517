import grpc
import Url_pb2
import Url_pb2_grpc

channel = grpc.insecure_channel('74.249.111.75:50051')
stub = Url_pb2_grpc.UrlServiceStub(channel)

def main():

    while(True):
        # Menú principal
        print("\n\n\n\n\n----------  gRPC Service  ----------\n1. Acortar URL \n2. Listar URLs acortadas por un usuario \n3. Salir\n")
        
        try:
            option = int(input(": "))
            
            if option == 1:
                username_creador = input("Ingrese su nombre de usuario: ")
                url = input("Ingrese la URL a acortar: ")
                titulo = input("Ingrese un título para la URL: ")

                # Crea la solicitud
                request = Url_pb2.urlResponse(
                    usernameCreador=username_creador,
                    urlOriginal=url,
                    titulo=titulo
                )

                # Llama al método gRPC y obtiene la respuesta
                try:
                    response = stub.crearUrl(request)
                    print(response)

                except grpc.RpcError as e:
                    print("Error al crear la URL acortada: ", e.details())

                input("\nPresione ENTER para salir: ")
                pass
            
            elif option == 2:
                username_to_search = input("Ingrese el nombre de usuario a buscar: ")
                print("\n")

                url_request = Url_pb2.urlRequest(username=username_to_search)
                response = stub.obtenerUrls(url_request)

                if(response.urls == []):
                    print("Error: No se encontraron URLs acortadas para el usuario especificado.")
                    input("\nPresione ENTER para salir: ")
                    pass

                for url in response.urls:
                    print(f"URL Acortada: {url.id}")
                    print(f"Original URL: {url.urlOriginal}")
                    print(f"Title: {url.titulo}")
                    print(f"Username creador: {url.usernameCreador}")
                    print(f"Fecha de creación: {url.fechaCreacion.ToDatetime()}")
                    print("\n")

                input("\nPresione ENTER para salir: ")
                pass
            
            elif option == 3:
                break
            
            else:
                print("Opción no válida.")

        except ValueError:
            print("Error: Debe ingresar un número entero como opción.")
            

if __name__ == '__main__':
    main()