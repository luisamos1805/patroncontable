package com.rhem.seguridad.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class SecureView extends VerticalLayout implements View {
	private boolean initialized;
	private void build() {
		
		Logger.getLogger(getClass().getCanonicalName()).log(Level.INFO,	"Initializing secure view");
		Label label = new Label("Bienvenido");
		label.setStyleName(Reindeer.LABEL_H1);
		addComponent(label);
		
		Button button = new Button("Users");
		button.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo("user");
			}
		});
		
		addComponent(button);

		//BrowserFrame embedded = new BrowserFrame();
		//embedded.setSource(new ThemeResource("secret.pdf"));
		//embedded.setWidth("600px");
		//embedded.setHeight("400px");
		//addComponent(embedded);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		if (!initialized) {
			build();
			initialized = true;
		}
	}
}
