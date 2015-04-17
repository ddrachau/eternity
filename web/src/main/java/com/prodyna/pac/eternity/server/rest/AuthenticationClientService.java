package com.prodyna.pac.eternity.server.rest;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;

@Stateless
@Path("/auth")
public class AuthenticationClientService extends Application{

    private ArrayList<String> sessions = new ArrayList<>();

    @POST
    public void login(@Context SecurityContext sc){

    }

    @POST
    public void logout() {

    }

    @GET
    @Path("/hello")
    @Produces()
    public Response sayHello(){
        return Response.status(200).entity("Hello").build();
    }


//    @Inject
//    HelloService helloService;
//
//    /**
//     * Retrieves a JSON hello world message.
//     * The {@link javax.ws.rs.Path} method annotation value is related to the one defined at the class level.
//     * @return
//     */
//    @GET
//    @Path("json")
//    @Produces({ "application/json" })
//    public JsonObject getHelloWorldJSON() {
//        return Json.createObjectBuilder()
//                .add("result", helloService.createHelloMessage("World"))
//                .build();
//    }
//
//    /**
//     * Retrieves a XML hello world message.
//     * The {@link javax.ws.rs.Path} method annotation value is related to the one defined at the class level.
//     * @return
//     */
//    @GET
//    @Path("xml")
//    @Produces({ "application/xml" })
//    public String getHelloWorldXML() {
//        return "<xml><result>" + helloService.createHelloMessage("World") + "</result></xml>";
//    }
}
