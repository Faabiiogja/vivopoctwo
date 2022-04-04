package com.vivopoc.aem.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vivopoc.aem.core.models.JsonModel;
import com.vivopoc.aem.core.interfaces.IJson;

@Component
public class JsonImpl implements IJson {

    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private Session session;

    //Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;

    public String getJsonData() {
        JsonModel jsonModel = null;
        List<JsonModel> jsonList = new ArrayList<JsonModel>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
        ResourceResolver resolver = null;

        try {
            //Invoke the adaptTo method to create a Session used to create a QueryManager
            ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
            Session session = resourceResolver.adaptTo(Session.class);

            log.info("Created session");


            //Obtain the query manager for the session ...
            javax.jcr.query.QueryManager queryManager = session.getWorkspace().getQueryManager();

            //Setup the quesry based on user input
            String sqlStatement="";

            //Setup the query to get all employee nodes
            sqlStatement = "SELECT * FROM [nt:unstructured] AS t WHERE ISDESCENDANTNODE('/content/commerce')";



            javax.jcr.query.Query query = queryManager.createQuery(sqlStatement,"JCR-SQL2");

            //Execute the query and get the results ...
            javax.jcr.query.QueryResult result = query.execute();

            log.info("executou  query");


            //Iterate over the nodes in the results ...
            javax.jcr.NodeIterator nodeIter = result.getNodes();

            log.info(String.valueOf(result.getRows().getSize()));
            while ( nodeIter.hasNext() ) {

                //For each node-- create an Employee instance
                jsonModel = new JsonModel();

                javax.jcr.Node node = nodeIter.nextNode();

                //Set all Employee object fields
                jsonModel.setCompleted(node.getProperty("completed").getBoolean());
                jsonModel.setId(node.getProperty("id").getLong());
                jsonModel.setTitle(node.getProperty("title").getString());
                jsonModel.setUserId(node.getProperty("userId").getLong());


                //Push the Employee Object to the list
                jsonList.add(jsonModel);
            }

            // Log out
            session.logout();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(jsonList);
        } catch(Exception e) {
            e.printStackTrace();
            log.error("CAIU NO CATH DO IMPL", e);
        }
        return null;
    }

}
