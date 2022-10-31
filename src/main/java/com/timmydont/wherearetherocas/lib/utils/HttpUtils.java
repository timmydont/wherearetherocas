package com.timmydont.wherearetherocas.lib.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpUtils {

    /* Error Messages (JSON format) */
    public static String HTTP_ERROR_PARSE_REQUEST = "{\"error\":\"failed to parse request data\"}";
    public static String HTTP_ERROR_GRAPHQL_INTERNAL = "{\"error\":\"graphql internal error\"}";

    /**
     * Write the response of the request
     *
     * @param response the response object
     * @param status   the response status
     * @param data     the response data
     * @throws IOException if something goes wrong while writing response
     */
    public static void setResponse(HttpServletResponse response, int status, String data) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter writer = response.getWriter();
        writer.write(data);
        writer.close();
    }
}
