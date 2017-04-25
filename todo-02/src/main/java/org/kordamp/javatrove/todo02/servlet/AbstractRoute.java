/*
 * Copyright 2016-2017 Andres Almiray
 *
 * This file is part of Java Trove Examples
 *
 * Java Trove Examples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java Trove Examples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Java Trove Examples. If not, see <http://www.gnu.org/licenses/>.
 */
package org.kordamp.javatrove.todo02.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.kordamp.javatrove.todo02.routes.Route;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andres Almiray
 */
public abstract class AbstractRoute implements Route {
    @Inject private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ModelAndView modelAndView = doHandle(req, resp);
        resp.setStatus(200);
        if (shouldReturnHtml(req)) {
            resp.setContentType("text/html");
            MustacheFactory mf = new DefaultMustacheFactory("templates");
            Mustache mustache = mf.compile(modelAndView.getViewName());
            mustache.execute(resp.getWriter(), modelAndView.getModel()).flush();
        } else {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(modelAndView.getModel()));
        }
    }

    protected abstract ModelAndView doHandle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    private boolean shouldReturnHtml(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("text/html");
    }
}
