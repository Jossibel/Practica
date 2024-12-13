package com.example.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import controller.Dao.servicies.FamiliaServices;
import controller.tda.list.LinkedList;
import models.Familia;
import models.HistorialTransacciones;
import models.Transacciones;

@Path("familia")
public class FamiliaApi {
    private HistorialTransacciones historialTransacciones = new HistorialTransacciones(100);
    private FamiliaServices familiaServices = new FamiliaServices();

    // Método de lista general con opciones de filtrado y ordenamiento
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFamilias(
            @QueryParam("canton") String canton,
            @QueryParam("apellidoPaterno") String apellidoPaterno,
            @QueryParam("apellidoMaterno") String apellidoMaterno,
            @QueryParam("integrantes") Integer integrantes,
            @QueryParam("tieneGenerador") Boolean tieneGenerador,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("orderDirection") String orderDirection) {
        HashMap<String, Object> map = new HashMap<>();

        LinkedList<Familia> familias = familiaServices.getAllFamilies();

        // Aplicar filtros
        if (canton != null) {
            familias = familiaServices.searchByDistrict(canton);
        }
        if (apellidoPaterno != null) {
            familias = familiaServices.searchByPaternalLastName(apellidoPaterno);
        }
        if (apellidoMaterno != null) {
            familias = familiaServices.searchByMaternalLastName(apellidoMaterno);
        }
        if (integrantes != null) {
            familias = familiaServices.searchByMemberCount(integrantes);
        }
        if (tieneGenerador != null) {
            familias = familiaServices.searchByGenerator(tieneGenerador);
        }

        // Aplicar ordenamiento
        boolean ascendente = orderDirection == null || orderDirection.equalsIgnoreCase("asc");
        if (orderBy != null) {
            switch (orderBy.toLowerCase()) {
                case "apellidopaterno":
                    familias = familiaServices.sortByPaternalLastName(ascendente);
                    break;
                case "apellidomaterno":
                    familias = familiaServices.sortByMaternalLastName(ascendente);
                    break;
                case "canton":
                    familias = familiaServices.sortByDistrict(ascendente);
                    break;
                case "integrantes":
                    familias = familiaServices.sortByMemberCount(ascendente);
                    break;
                case "generador":
                    familias = familiaServices.sortByGenerator(ascendente);
                    break;
            }
        }

        map.put("msg", "Ok");
        map.put("data", familias.toArray());
        map.put("total", familias.getSize());

        return Response.ok(map).build();
    }

    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            Familia familia = familiaServices.getFamilyById(id);
            map.put("msg", "Ok");
            map.put("data", familia);
            
            if (familia.getId() == null) {
                map.put("data", "No existe la familia con ese identificador");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }

            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error");
            map.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }
    
    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();

        try {
            Familia familia = familiaServices.getCurrentFamily();
            
            familia.setCanton(map.get("canton").toString());
            familia.setApellidoPaterno(map.get("apellidoPaterno").toString());
            familia.setApellidoMaterno(map.get("apellidoMaterno").toString());
            familia.setIntegrantes(Integer.parseInt(map.get("integrantes").toString()));
            familia.setTieneGenerador(Boolean.parseBoolean(map.get("tieneGenerador").toString()));

            familiaServices.setCurrentFamily(familia);
            familiaServices.saveFamily();

            Transacciones transaccion = new Transacciones("GUARDAR FAMILIA Y GENERADOR", familia.getId());
            historialTransacciones.agregarTransaccion(transaccion);

            res.put("msg", "Ok");
            res.put("data", "Guardado correctamente");
            return Response.ok(res).build();
           
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();

        try {
            int id = Integer.parseInt(map.get("id").toString());
            Familia familia = familiaServices.getFamilyById(id);
            
            familia.setCanton(map.get("canton").toString());
            familia.setApellidoPaterno(map.get("apellidoPaterno").toString());
            familia.setApellidoMaterno(map.get("apellidoMaterno").toString());
            familia.setIntegrantes(Integer.parseInt(map.get("integrantes").toString()));
            familia.setTieneGenerador(Boolean.parseBoolean(map.get("tieneGenerador").toString()));

            familiaServices.setCurrentFamily(familia);
            familiaServices.updateFamily();

            Transacciones transaccion = new Transacciones("ACTUALIZAR FAMILIA Y GENERADOR", familia.getId());
            historialTransacciones.agregarTransaccion(transaccion);

            res.put("msg", "Ok");
            res.put("data", "Actualizado correctamente");
            return Response.ok(res).build();
           
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }
    
    @Path("/listType")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("msg", "Ok");
        map.put("data", familiaServices.getCurrentFamily());
        return Response.ok(map).build();
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFamilia(@PathParam("id") int id) {
        HashMap<String, Object> res = new HashMap<>();
    
        try {
            boolean familiaDeleted = familiaServices.deleteFamily(id - 1);

            if (familiaDeleted) {
                Transacciones transaccion = new Transacciones("ELIMINAR FAMILIA, GENERADOR", id);
                historialTransacciones.agregarTransaccion(transaccion);

                res.put("message", "Familia y Generador eliminados exitosamente");
                return Response.ok(res).build();
            } else {
                res.put("message", "Familia no encontrada o no eliminada");
                return Response.status(Response.Status.NOT_FOUND).entity(res).build();
            }
        } catch (Exception e) {
            res.put("message", "Error al intentar eliminar la familia");
            res.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/contadorGeneradores")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response contarFamiliasConGenerador() {
        int totalFamiliasConGenerador = familiaServices.countFamiliesWithGenerator();

        HashMap<String, Object> response = new HashMap<>();
        response.put("msg", "Ok");
        response.put("familiasConGenerador", totalFamiliasConGenerador);
        
        return Response.ok(response).build();
    }

    @Path("/consultar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response filtrarFamilias(
            @QueryParam("campo") String campo,
            @QueryParam("valor") String valor,
            @QueryParam("metodo") String metodo) {

        Map<String, Object> respuesta = new HashMap<>();

        try {
            LinkedList<Familia> familias = familiaServices.getAllFamilies();
            LinkedList<Familia> resultados = new LinkedList<>();

            if (campo == null || valor == null || metodo == null) {
                respuesta.put("mensaje", "Error");
                respuesta.put("detalle", "Debe especificar campo, valor y método de búsqueda");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
            }

            // Convertir valores según el campo
            switch (campo.toLowerCase()) {
                case "canton":
                    resultados = metodo.equalsIgnoreCase("binaria") ? familiaServices.searchBinary("canton", valor)
                            : familiaServices.searchLinear("canton", valor);
                    break;
                case "apellidopaterno":
                    resultados = metodo.equalsIgnoreCase("binaria")
                            ? familiaServices.searchBinary("apellidoPaterno", valor)
                            : familiaServices.searchLinear("apellidoPaterno", valor);
                    break;
                case "apellidomaterno":
                    resultados = metodo.equalsIgnoreCase("binaria")
                            ? familiaServices.searchBinary("apellidoMaterno", valor)
                            : familiaServices.searchLinear("apellidoMaterno", valor);
                    break;
                case "integrantes":
                    resultados = metodo.equalsIgnoreCase("binaria") ? familiaServices.searchBinary("integrantes", valor)
                            : familiaServices.searchLinear("integrantes", valor);
                    break;
                case "generador":
                    resultados = metodo.equalsIgnoreCase("binaria") ? familiaServices.searchBinary("generador", valor)
                            : familiaServices.searchLinear("generador", valor);
                    break;
                default:
                    respuesta.put("mensaje", "Error");
                    respuesta.put("detalle", "Campo de búsqueda no válido");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
            }

            respuesta.put("mensaje", "Éxito");
            respuesta.put("resultados", resultados.toArray());
            respuesta.put("cantidadTotal", resultados.getSize());
            respuesta.put("metodoBusqueda", metodo);

        } catch (Exception e) {
            respuesta.put("mensaje", "Error");
            respuesta.put("detalle", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }

        return Response.ok(respuesta).build();
    }

    @Path("/clasificar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response organizarFamilias(
            @QueryParam("criterio") String criterio,
            @QueryParam("orden") String orden,
            @QueryParam("algoritmo") String algoritmo) {

        Map<String, Object> respuesta = new HashMap<>();

        try {
            if (criterio == null || orden == null || algoritmo == null) {
                respuesta.put("mensaje", "Error");
                respuesta.put("detalle", "Debe especificar criterio, orden y algoritmo de ordenamiento");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
            }

            boolean ascendente = orden.equalsIgnoreCase("ascendente");
            LinkedList<Familia> familiasProcesadas = null;

            // Seleccionar algoritmo de ordenamiento
            switch (algoritmo.toLowerCase()) {
                case "shellsort":
                    familiasProcesadas = familiaServices.sortWithShellSort(criterio, ascendente);
                    break;
                case "mergesort":
                    familiasProcesadas = familiaServices.sortWithMergeSort(criterio, ascendente);
                    break;
                case "quicksort":
                    familiasProcesadas = familiaServices.sortWithQuickSort(criterio, ascendente);
                    break;
                default:
                    respuesta.put("mensaje", "Error");
                    respuesta.put("detalle", "Algoritmo de ordenamiento no válido");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
            }

            respuesta.put("mensaje", "Éxito");
            respuesta.put("resultados", familiasProcesadas.toArray());
            respuesta.put("cantidadTotal", familiasProcesadas.getSize());
            respuesta.put("algoritmoUsado", algoritmo);
            respuesta.put("criterioOrdenamiento", criterio);
            respuesta.put("ordenamiento", orden);

        } catch (Exception e) {
            respuesta.put("mensaje", "Error");
            respuesta.put("detalle", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }

        return Response.ok(respuesta).build();
    }
}
