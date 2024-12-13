from flask import Blueprint, abort, request, render_template, redirect
import json
import requests
from flask import flash
from flask import Blueprint, jsonify
import datetime  # Add this import

router = Blueprint('router', __name__)

def registrar_historial(operacion, tipo, mensaje):
    try:
        with open("historial.json", "r") as archivo:
            historial = json.load(archivo)
    except FileNotFoundError:
        historial = []

    nueva_entrada = {
        "fecha": datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
        "operacion": operacion,
        "tipo": tipo,
        "mensaje": mensaje
    }
    historial.append(nueva_entrada)

    with open("historial.json", "w") as archivo:
        json.dump(historial, archivo, indent=4)

@router.route('/')
def home():
    registrar_historial("acceso", "login", "Acceso a página de login")
    return render_template('fragmento/inicial/login.html')

@router.route('/admin')
def admin():
    registrar_historial("acceso", "admin", "Acceso a panel de administración")
    return render_template('fragmento/inicial/admin.html')

@router.route('/admin/familia/register')
def view_register_familia():
    try:
        r_familia = requests.get("http://localhost:8000/api/familia/list")
        data_familia = r_familia.json()
        r_generador = requests.get("http://localhost:8000/api/generador/list")
        data_generador = r_generador.json()
        registrar_historial("consulta", "familia", "Acceso a registro de familia")
        return render_template('fragmento/familia/registro.html', lista_familia=data_familia["data"],lista_generador=data_generador["data"])
    except Exception as e:
        registrar_historial("error", "familia", f"Error al cargar registro de familia: {str(e)}")
        abort(500)

@router.route('/admin/familia/list')
def list_person(msg=''):
    try:
        r_familia = requests.get("http://localhost:8000/api/familia/list")
        data_familia = r_familia.json()
        r_generador = requests.get("http://localhost:8000/api/generador/list")
        data_generador = r_generador.json()
        registrar_historial("consulta", "familia", "Listado de familias consultado")
        return render_template('fragmento/familia/lista.html', lista_familia=data_familia["data"],lista_generador=data_generador["data"])
    except Exception as e:
        registrar_historial("error", "familia", f"Error al listar familias: {str(e)}")
        abort(500)

@router.route('/admin/familia/edit/<id>', methods=['GET'])
def view_edit_person(id):

    r = requests.get("http://localhost:8000/api/familia/listType")
    lista_tipos = r.json() 


    r1 = requests.get(f"http://localhost:8000/api/familia/get/{id}")    

    if r1.status_code == 200:
        data_familia = r1.json()

        if "data" in data_familia and data_familia["data"]:     
            familia = data_familia["data"]


            if familia["tieneGenerador"]:     
                r_generador = requests.get(f"http://localhost:8000/api/generador/get/{familia['id']}")
                if r_generador.status_code == 200:
                    data_generador = r_generador.json()
                    generador = data_generador["data"] if "data" in data_generador else None
                else:
                    generador = None  

            else:
                generador = None 

            return render_template('fragmento/familia/editar.html', lista=lista_tipos["data"], familia=familia, generador=generador)
        else:
            flash("No se encontraron datos para la familia.", category='error')
            return redirect("/admin/familia/list")
    else:
        flash("Error al obtener la familia", category='error')
        return redirect("/admin/familia/list")
    


@router.route('/admin/familia/save', methods=['POST'])
def save_person():
    headers = {'Content-Type': 'application/json'}
    form = request.form

    data_familia = { 
        "canton": form["can"],
        "apellidoPaterno": form["ape"],
        "apellidoMaterno": form["apem"],
        "integrantes": form["inte"],
        "tieneGenerador": form["tieneg"] == 'true' 
    }

    if form["tieneg"] == 'true':  
        data_generador = {
            "costo": form["cost"],
            "consumoXHora": form["conxh"],
            "energiaGenerada": form["energen"],
            "uso": form["uso"],
        }
    else:
        data_generador = { 
            "costo": 0,  
            "consumoXHora": 0,  
            "energiaGenerada": 0, 
            "uso": 'ninguno', 
        }

    r_familia = requests.post("http://localhost:8000/api/familia/save", data=json.dumps(data_familia), headers=headers)     # Hacer la petición para guardar la familia
    
    requests.post("http://localhost:8000/api/generador/save", data=json.dumps(data_generador), headers=headers)    # Hacer la petición para guardar el generador


    if r_familia.status_code == 200:

        flash("Registro guardado correctamente", category='info')
        return redirect('/admin/familia/list')
    else:
        flash(r_familia.json().get("data", "Error al guardar la familia"), category='error')
        return redirect('/admin/familia/list')

        
@router.route('/admin/familia/update', methods=['POST'])
def update_person():
    headers = {'Content-Type': 'application/json'}
    form = request.form

    data_familia = {
        "id": form["id"],
        "canton": form["can"],
        "apellidoPaterno": form["ape"],
        "apellidoMaterno": form["apem"],
        "integrantes": form["inte"],
        "tieneGenerador": form["tieneg"] == 'true'
    }

    if form["tieneg"] == 'true':  
        data_generador = {
            "id": form["id"], 
            "costo": form["cost"],
            "consumoXHora": form["conxh"],
            "energiaGenerada": form["energen"],
            "uso": form["uso"],
        }
    else:

        data_generador = {  
            "id": form["id"],  
            "costo": 0,  
            "consumoXHora": 0,  
            "energiaGenerada": 0,  
            "uso": 'ninguno',  
        }

    r_generador = requests.post("http://localhost:8000/api/generador/update", data=json.dumps(data_generador), headers=headers)

    if r_generador.status_code != 200:
        flash("Error al actualizar el generador: " + r_generador.json().get("data", ""), category='error')
   
    r_familia = requests.post("http://localhost:8000/api/familia/update", data=json.dumps(data_familia), headers=headers)
    
    if r_familia.status_code == 200:
        flash("Registro de familia guardado correctamente", category='info')
        return redirect('/admin/familia/list')
    else:
        flash(r_familia.json().get("data", "Error al actualizar la familia"), category='error')
        return redirect('/admin/familia/list')

@router.route('/admin/familia/delete/<int:id>', methods=['POST'])
def delete_familia(id):
   
    requests.delete(f"http://localhost:8000/api/familia/delete/{id}") 
    requests.delete(f"http://localhost:8000/api/generador/delete/{id}")

       
    return redirect('/admin/familia/list')    


def load_data(file_path):
    with open(file_path, 'r') as file:
        return json.load(file)

@router.route('/admin/familia_generador', methods=['GET'])
def get_familia_generador_data():
  
    familias = load_data('/home/ariel/Documents/Taller_Domingo_2JSON/Taller_Domingo_2json/src/main/java/Data/Familia.json')
    generadores = load_data('/home/ariel/Documents/Taller_Domingo_2JSON/Taller_Domingo_2json/src/main/java/Data/Generador.json')


    familias_generadores = []      
    for familia in familias:
        generador = next((g for g in generadores if g['id'] == familia['id']), None)
        familias_generadores.append({
            "familia": familia,
            "generador": generador
        })

    response = requests.get('http://localhost:8000/api/familia/contadorGeneradores')    
    data = response.json()
    familias_con_generador = data['familiasConGenerador']

    total_familias = len(familias)    

    return render_template(
        'fragmento/familia/familia_generador.html',
        familias_generadores=familias_generadores,
        familias_con_generador=familias_con_generador,
        total_familias=total_familias
    )

# Aqui buscar y ordenar######

@router.route('/admin/familia/buscar')
def buscar_familias():
    params = {}
    for param in ['canton', 'apellidoPaterno', 'apellidoMaterno', 'integrantes', 'tieneGenerador']:
        value = request.args.get(param)
        if value:
            params[param] = value

    try:
        response = requests.get('http://localhost:8000/api/familia/list', params=params)
        return jsonify(response.json())
    except Exception as e:
        return jsonify({'msg': 'Error', 'data': str(e)}), 500

@router.route('/admin/familia/ordenar')
def ordenar_familias():
    criterio = request.args.get('criterio')
    orden = request.args.get('orden')
    algoritmo = request.args.get('algoritmo')

    try:
        response = requests.get(
            'http://localhost:8000/api/familia/clasificar',
            params={
                'criterio': criterio,
                'orden': orden,
                'algoritmo': algoritmo
            }
        )
        return jsonify(response.json())
    except Exception as e:
        return jsonify({'msg': 'Error', 'data': str(e)}), 500
