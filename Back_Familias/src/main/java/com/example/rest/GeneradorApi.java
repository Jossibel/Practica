package com.example.rest;

import controller.Dao.servicies.GeneradorServicies;
import models.Generador;

import java.util.HashMap;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

@Path("generador")
public class GeneradorApi {
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        HashMap map = new HashMap<>();
        GeneradorServicies ps = new GeneradorServicies();
        map.put("msg", "Ok");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[]{});
           
        }
        return Response.ok(map).build();
    }

    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(@PathParam("id") Integer id) {
        HashMap map = new HashMap<>();
        GeneradorServicies ps = new GeneradorServicies();
        try {
            ps.setGenerador(ps.get(id));
        } catch (Exception e) {
            // TODO: handle exception
        }
        map.put("msg", "Ok");
        map.put("data", ps.getGenerador());
        if (ps.getGenerador().getId() == null) {
            map.put("data", "No existe la generador con ese identificador");
           return Response.status(Status.BAD_REQUEST).entity(map).build();
        }


        return Response.ok(map).build();
    }
    
    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) {
        //todo
        //Validation

        HashMap res = new HashMap<>();

        try {

            GeneradorServicies ps = new GeneradorServicies();
            ps.getGenerador().setCosto(Float.parseFloat(map.get("costo").toString()));
            ps.getGenerador().setConsumoXHora(Float.parseFloat(map.get("consumoXHora").toString()));
            ps.getGenerador().setEnergiaGenerada(Float.parseFloat(map.get("energiaGenerada").toString()));
            ps.getGenerador().setUso(map.get("uso").toString());

            ps.save();
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
    public Response update(HashMap map) {
        //todo
        //Validation

        HashMap res = new HashMap<>();

        try {

            GeneradorServicies ps = new GeneradorServicies();
            ps.setGenerador(ps.get(Integer.parseInt(map.get("id").toString())));
            ps.getGenerador().setCosto(Float.parseFloat(map.get("costo").toString()));
            ps.getGenerador().setConsumoXHora(Float.parseFloat(map.get("consumoXHora").toString()));
            ps.getGenerador().setEnergiaGenerada(Float.parseFloat(map.get("energiaGenerada").toString()));
            ps.getGenerador().setUso(map.get("uso").toString());

            ps.update();
            res.put("msg", "Ok");
            res.put("data", "Guardado correctamente");
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
        HashMap map = new HashMap<>();
        GeneradorServicies ps = new GeneradorServicies();
        map.put("msg", "Ok");
        map.put("data", ps.getGenerador());
        return Response.ok(map).build();
    }

    
    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFamilia(@PathParam("id") int id) {
        HashMap<String, Object> res = new HashMap<>();
    
        try {
            GeneradorServicies gs = new GeneradorServicies();
            
            boolean generadorDeleted = gs.delete(id - 1);         // Intentar eliminar la familia
    
            if (generadorDeleted) {
                res.put("message", "Familia y Generador eliminados exitosamente");
                return Response.ok(res).build();
            } else {

                res.put("message", "Familia no encontrada o no eliminada");          // Si no se eliminó, enviar un error 404
                return Response.status(Response.Status.NOT_FOUND).entity(res).build();

            }
        } catch (Exception e) {

            res.put("message", "Error al intentar eliminar la familia");         // En caso de error, devolver una respuesta de error interno
            res.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
            
        }
    }

}
