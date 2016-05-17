package com.example.honnold_oh.myruns.backend;

import com.example.honnold_oh.myruns.backend.data.Entry;
import com.example.honnold_oh.myruns.backend.data.EntryDatastore;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String name = req.getParameter("name");
		String addr = req.getParameter("addr");
		String phone = req.getParameter("phone");

		if (name == null || name.equals("")) {
			req.setAttribute("_retStr", "invalid input");
			getServletContext().getRequestDispatcher("/query_result.jsp")
					.forward(req, resp);
			return;
		}

		Entry entry = new Entry(name, addr, phone);
		boolean ret = EntryDatastore.add(entry);
		if (ret) {
			req.setAttribute("_retStr", "Add entry " + name + " succ");

			ArrayList<Entry> result = new ArrayList<Entry>();
			result.add(entry);
			req.setAttribute("result", result);
		} else {
			req.setAttribute("_retStr", name + " exists");
		}

		getServletContext().getRequestDispatcher("/query_result.jsp").forward(
				req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}

}
