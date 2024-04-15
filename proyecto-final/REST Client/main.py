import requests
import json

base_url = "https://sh.stevenapp.me/api/"

def main():
    headers = {"Authorization" : ""}

    while(True):
        # Para obtener el token de autenticación
        if(headers["Authorization"] == ""):
            username = input("Ingrese su nombre de usuario: ")
            password = input("Ingrese su contraseña: ")
            
            response = requests.post(base_url + "generarApiKey/", json={"username": username, "password": password})
            
            if response.status_code != 200:
                print("\n\nError: Usuario o contraseña incorrectos. Reinicie el programa e intente de nuevo.")
                break

            headers["Authorization"] = "Bearer " + response.json()["token"]


        # Menú principal
        print("\n\n\n\n\n----------  REST API  ----------\n1. Acortar URL \n2. Listar URLs acortadas por un usuario \n3. Salir\n")
        
        try:
            option = int(input(": "))
            
            if option == 1:
                url = input("Ingrese la URL a acortar: ")
                titulo = input("Ingrese un título para la URL: ")

                response = requests.post(base_url + "acortar/", json={"urlOriginal": url, "titulo": titulo}, headers=headers)
                if response.status_code == 200:
                    print("\nUrl Original: " + response.json().get("urlOriginal") + "\n" + "Url Acortada: " + response.json().get("urlAcortada") + "\n" + "Fecha Creación: " + str(response.json().get("fechaCreacion")) + "\n" + "Estadísticas: ")
                    print(response.json().get("estadisticas"))
                    print("Imagen preview: ")
                    print(response.json().get("base64Image"))
                else:
                    print("\nError: No se pudo acortar la URL.\n   Posibles causas: 1. URL inválida. 2. Error de autenticación.")
                
                input("\nPresione ENTER para salir: ")
                pass
            
            elif option == 2:
                username_to_search = input("Ingrese el nombre de usuario a buscar: ")
                print("\n")

                response = requests.get(base_url + "urls/usuarios/" + username_to_search, headers=headers)
                if response.status_code == 200:
                    for json_element in response.json():
                        print (json.dumps(json_element, indent=4))
                else:
                    print("Error: No se encontraron URLs acortadas para el usuario especificado.")

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