package com.rhem.seguridad.event;

import java.io.Serializable;

import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class SaveUserEvent extends Component.Event {

	public interface SaveUserListener extends Serializable {
        String method = "saveUser";
		public void saveUser(SaveUserEvent event);
    }
	
	public SaveUserEvent(Component source) {
		super(source);
	}

}
