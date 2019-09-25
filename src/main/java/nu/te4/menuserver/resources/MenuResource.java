/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.menuserver.resources;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.te4.menuserver.beans.MenuBean;
import nu.te4.menuserver.entities.Menu;

/**
 *
 * @author T4User
 */
@Path("menu")
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {
    
    @EJB
    MenuBean menuBean;
    
    @GET
    @Path("")
    public Response getMenu(){
        Menu menu = menuBean.getMenu();
        if(menu == null)
            return Response.noContent().header("Access-Control-Allow-Origin", "*").build();
        
        return Response.ok(menu).header("Access-Control-Allow-Origin", "*").build();
    }
}
