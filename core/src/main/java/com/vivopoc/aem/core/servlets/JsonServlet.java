package com.vivopoc.aem.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vivopoc.aem.core.interfaces.IJson;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=JsonData Demo Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/myCustData" })

public class JsonServlet extends SlingAllMethodsServlet {


    /**
     *
     */
    private static final long serialVersionUID = -4808964615598645735L;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private IJson jsonData;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
            throws ServletException, IOException {
        try {
            logger.info("About to call");

            String data = jsonData.getJsonData();
            resp.getWriter().write(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
