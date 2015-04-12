//package com.prodyna.pac.eternity.server;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.prodyna.pac.eternity.server.model.Project;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//
///**
// * Hello world!
// */
//public class App {
//    public static void main(String[] args) {
//
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("p", "{id=P01140, description=Liferay Unterstützung}");
//
//
//        Gson gson = new GsonBuilder()..disableHtmlEscaping().create();
//
//        Project a = new Project();
//        a.setDescription("desc");
//        a.setId("pID");
//
//        System.out.println(gson.fromJson(gson.toJson(a),Project.class));
//
////        System.out.println(json);
////        Project p = gson.fromJson(json, Project.class);
////
////        System.out.println(p);
//
//
//    }
//}
