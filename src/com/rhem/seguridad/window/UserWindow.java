package com.rhem.seguridad.window;

import java.lang.reflect.Method;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import com.rhem.seguridad.event.SaveUserEvent;
import com.rhem.seguridad.event.SaveUserEvent.SaveUserListener;
import com.rhem.seguridad.exception.PasswordException;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class UserWindow extends Window {
	
	private FormLayout layout;
	private HorizontalLayout buttonBar;
	private Button save;
	private Button cancel;	
	private TextField username;
	private PasswordField typepassword;
	private PasswordField retypepassword;
	private FieldGroup binder;
	private boolean calculatePassword;
	
	public UserWindow(){
		layout = new FormLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		
		username = new TextField();
		username.setCaption("Usuario");
		username.setNullRepresentation("");
		
		typepassword = new PasswordField();
		typepassword.setCaption("Contraseña");
		
		retypepassword = new PasswordField();
		retypepassword.setCaption("Repita Contraseña");
		
		//rolenames = new SecroleTokenField();
		//rolenames.setCaption("Roles");
		//userLayout.addComponent(rolenames);
		
		// buttonBar
		buttonBar = new HorizontalLayout();
		buttonBar.setMargin(false);
		buttonBar.setSpacing(true);
		
		// cancel
		cancel = new Button();
		cancel.setCaption("Cancelar");
		cancel.setIcon(new ThemeResource("icons/cancel.png"));
		cancel.setImmediate(true);
		
		// save
		save = new Button();
		save.setCaption("Guardar");
		save.setIcon(new ThemeResource("icons/save.png"));
		save.setImmediate(true);
		
		buttonBar.addComponents(cancel, save);
		
		layout.addComponents(username, typepassword, retypepassword, buttonBar);
		
		setContent(layout);
		
		setModal(false);
		center();
		
		cancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				binder.discard();
				close();
			}
		});
		
		save.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					
					if(calculatePassword){
						setPassword();
					}
					
					binder.commit();
					fireEvent(new SaveUserEvent(UserWindow.this));
				} catch (CommitException e) {
					Throwable t = null;
					for(t = e.getCause(); t.getCause() != null; t = t.getCause());
					Notification.show(t.getMessage(), Type.ERROR_MESSAGE);
					e.printStackTrace();
				} catch (PasswordException e) {
					Notification.show(e.getMessage());
				}
			}
		});

	}
	
	@SuppressWarnings("unchecked")
	private void setPassword() throws PasswordException{
		if(typepassword.getValue() == null){
			throw new PasswordException("La contraseña es obligatorio");
		}
		
		if("".equals(typepassword.getValue().trim()))
			throw new PasswordException("La contraseña es obligatorio");
		
		if(!typepassword.getValue().equals(retypepassword.getValue())){
			throw new PasswordException("Las contraseñas no coinciden");
		}
		
		PasswordService svc = new DefaultPasswordService();
		String encrypted = svc.encryptPassword(typepassword.getValue());
		binder.getItemDataSource().getItemProperty("password").setValue(encrypted);
		
	}
	
	public void bind(Item item){
		username.focus();
		binder = new FieldGroup(item);
		binder.bindMemberFields(this);
	}
	
	public void beforeAdd(){
		calculatePassword = true;
		username.setEnabled(true);
		typepassword.setVisible(true);
		retypepassword.setVisible(true);
		//rolenames.setVisible(true);
	}
	
	public void beforeEdit(){
		calculatePassword = false;
		username.setEnabled(true);
		typepassword.setVisible(false);
		retypepassword.setVisible(false);
		//rolenames.setVisible(true);
	}
	public void beforeChangePassword(){
		calculatePassword = true;
		username.setEnabled(false);
		//rolenames.setVisible(false);
		typepassword.setVisible(true);
		retypepassword.setVisible(true);
	}
	
	public void addListener(SaveUserListener listener) {
        try {
            Method method = SaveUserListener.class.getDeclaredMethod(
            		SaveUserListener.method, new Class[] { SaveUserEvent.class });
            addListener(SaveUserEvent.class, listener, method);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, "+SaveUserListener.method+" method not found");
        }
    }

    public void removeListener(SaveUserListener listener) {
        removeListener(SaveUserEvent.class, listener);
    }

}
