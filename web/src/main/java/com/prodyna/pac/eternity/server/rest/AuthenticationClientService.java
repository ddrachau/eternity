package com.prodyna.pac.eternity.server.rest;

import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Path("/auth")
public class AuthenticationClientService extends Application {

    @Inject
    private UserService userService;

    private ArrayList<String> sessions = new ArrayList<>();

    @POST
    public void login(@Context SecurityContext sc) {

    }

    @POST
    public void logout() {

    }

    @GET
    @Path("/hello")
    @Produces()
    public Response sayHello() {

        String response = "";
        List<User> all = userService.findAll();
        for (User user : all) {
            response += user.toString();
            response += "\n";
        }

        return Response.status(200).entity(response).build();
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
