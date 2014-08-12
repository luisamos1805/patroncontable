package com.rhem.seguridad;

import javax.servlet.annotation.WebServlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.rhem.seguridad.view.ErrorView;
import com.rhem.seguridad.view.LoginView;
import com.rhem.seguridad.view.SecureView;
import com.rhem.seguridad.view.UserView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Widgetset("com.rhem.seguridad.widgetset.SeguridadWidgetset")
@Theme("seguridad")
public class SeguridadUI extends UI implements ViewChangeListener{

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SeguridadUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		Navigator navigator = new Navigator(this, this);
		navigator.addViewChangeListener(this);
		navigator.addView("", LoginView.class);
		if (SecurityUtils.getSubject().isAuthenticated()) {
			getUI().getNavigator().addView("secure", SecureView.class);
			getUI().getNavigator().addView("user", UserView.class);
		}
		navigator.setErrorView(ErrorView.class);
		
	}
	
	@Override
	public boolean beforeViewChange(ViewChangeEvent event) {
		Subject currentUser = SecurityUtils.getSubject();
		
		if (currentUser.isAuthenticated() && event.getViewName().equals("")) {
		event.getNavigator().navigateTo("secure");
		return false;
		}
		
		if (!currentUser.isAuthenticated() && !event.getViewName().equals("")) {
		event.getNavigator().navigateTo("");
		return false;
		}
		
		/*if (currentUser.isAuthenticated() && event.getViewName().equals("user")) {
			event.getNavigator().navigateTo("user");
			return false;
		}*/
		
		return true;
	}
	
	@Override
	public void afterViewChange(ViewChangeEvent event) {
	// TODO Auto-generated method stub
	}

}