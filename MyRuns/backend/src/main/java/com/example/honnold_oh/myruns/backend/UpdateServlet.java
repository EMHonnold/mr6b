package com.example.honnold_oh.myruns.backend;

import com.example.honnold_oh.myruns.backend.data.Entry;
import com.example.honnold_oh.myruns.backend.data.EntryDatastore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String name = req.getParameter("name");
		String addr = req.getParameter("addr");
		String phone = req.getParameter("phone");

		if (name != null && !name.equals("")) {
			Entry contact = new Entry(name, addr, phone);
			EntryDatastore.update(contact);
		}

		resp.sendRedirect("/query.do");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}

}
