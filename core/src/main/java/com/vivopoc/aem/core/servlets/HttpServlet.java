package com.vivopoc.aem.core.servlets;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;


import javax.servlet.Servlet;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vivopoc.aem.core.interfaces.HttpService;
import com.vivopoc.aem.core.models.JsonModel;


/**
 * @author Anirudh Sharma
 *
 * This method makes an HTTP call and read the value from the JSON webservice via an OSGi configuration
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=HTTP servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/httpcall" })




public class HttpServlet extends SlingSafeMethodsServlet {




    private static final long serialVersionUID = -2014397651676211439L;

    private static final Logger log = LoggerFactory.getLogger(HttpServlet.class);

    @Reference
    private HttpService httpService;

    @Reference
    private ResourceResolverFactory resolverFactory;


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        try {


            String jsonResponse = httpService.makeHttpCall();

            /**
             * Printing the json response on the browser
             */
            response.getWriter().println(jsonResponse);
            Type tipoArray = TypeToken.getParameterized(List.class, JsonModel.class).getType();
            Gson conversor = new Gson();
            List<JsonModel> lista = conversor.fromJson(jsonResponse, tipoArray);
            Map<String, Object> param = new HashMap<String, Object>();

            param.put("admin", "admin");
            //ResourceResolver resourceResolver = resolverFactory.getResourceResolver(param);
            ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
            Session session = resourceResolver.adaptTo(Session.class);
            Node root = session.getRootNode().getNode("content");
            Node node = root.addNode("commerce", "sling:Folder");
            session.save();
            Node folder =  session.getRootNode().getNode("content/commerce") ;

            for (JsonModel objeto : lista) {

                Node day = folder.addNode(String.valueOf(objeto.getId()), "nt:unstructured");
                day.setProperty("title", objeto.getTitle());
                day.setProperty("completed", objeto.isCompleted());
                day.setProperty("userId", objeto.getUserId());
                day.setProperty("id", objeto.getId());
                session.save();
            }


            // Store content



            session.save();
            session.logout();




        } catch (Exception e) {

            log.error(e.getMessage(), e);
        }


    }



}


