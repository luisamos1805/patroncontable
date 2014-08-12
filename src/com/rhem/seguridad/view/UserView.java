package com.rhem.seguridad.view;

import java.util.Iterator;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.tepi.filtertable.FilterTable;
import org.vaadin.dialogs.ConfirmDialog;

import com.rhem.seguridad.Config;
import com.rhem.seguridad.converter.MyStringToBooleanConverter;
import com.rhem.seguridad.domain.User;
import com.rhem.seguridad.event.SaveUserEvent;
import com.rhem.seguridad.event.SaveUserEvent.SaveUserListener;
import com.rhem.seguridad.util.MyFilterDecorador;
import com.rhem.seguridad.window.UserWindow;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserView extends VerticalLayout implements View {

	private boolean initialized;
	
	private FilterTable table;
	//@RequiresAction("AGREGAR")
	private Button addButton;
	//@RequiresAction("EDITAR")
	private Button editButton;
	//@RequiresAction("ELIMINAR")
	private Button deleteButton;
	//@RequiresAction("CAMBIAR CONTRASEÑA")
	private Button keyButton;
	//@RequiresAction("ACTIVAR/DESACTIVAR")
	private Button enableButton;
	private Button clearFiltersButton;
	private Label empty;
	
	private JPAContainer<User> container;
	
	private BeanItem<User> item;
	
	private void build() {
		container = JPAContainerFactory.make(User.class, Config.PERSISTENCE_UNIT);
		
		table = new FilterTable(null);
		table.setContainerDataSource(container);
		table.setFilterBarVisible(true);
		
		table.setSelectable(true);
		table.setImmediate(true);
		table.setMultiSelect(true);
		table.setMultiSelectMode(MultiSelectMode.DEFAULT);
		table.setColumnCollapsingAllowed(true);
		
		table.setSizeFull();
		
		table.setColumnHeader("username", "Usuario");
		table.setColumnHeader("enabled","Estado" );

		table.setConverter("enabled",new MyStringToBooleanConverter("INACTIVO", "ACTIVO"));
		
		table.setVisibleColumns(new Object[] {"username", "enabled"});
		table.setFilterDecorator(new MyFilterDecorador());
		
		Label title = new Label("Usuarios Autorizados");
		
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSpacing(true);
		addButton = new Button("Agregar");
		editButton = new Button("Editar");
		deleteButton = new Button("Eliminar");
		keyButton = new Button("Cambiar Contraseña");
		enableButton = new Button("Activar/Desactivar");
		clearFiltersButton = new Button();
		empty = new Label("");
		
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
		
		addButton.setIcon(new ThemeResource("icons/add.png"));
		editButton.setIcon(new ThemeResource("icons/edit.png"));
		deleteButton.setIcon(new ThemeResource("icons/delete.png"));
		keyButton.setIcon(new ThemeResource("icons/key.png"));
		enableButton.setIcon(new ThemeResource("icons/disable.png"));
		clearFiltersButton.setIcon(new ThemeResource("icons/filter_delete.gif"));
		
		clearFiltersButton.setDescription("Limpiar Filtros");
		
		addButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				User user = new User();
				item = new BeanItem<User>(user);
				final UserWindow window = new UserWindow();
				window.bind(item);
				window.setCaption("Agregar Usuario");
				window.beforeAdd();
				UI.getCurrent().addWindow(window);
				
				window.addListener(new SaveUserListener() {
					
					@Override
					public void saveUser(SaveUserEvent event) {
						try{
							container.addEntity(item.getBean());
							window.close();
						} catch (PersistenceException e) {
							Throwable t = null;
							for(t = e.getCause(); t.getCause() != null; t = t.getCause());
							Notification.show(t.getMessage(), Type.ERROR_MESSAGE);
						}
					}
				});
				
			}
		});
		
		editButton.addClickListener(new ClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				Set<Integer> list = (Set<Integer>) table.getValue();
				final JPAContainerItem<User> item = (JPAContainerItem<User>) table.getItem(list.iterator().next());
				final UserWindow window = new UserWindow();
				window.bind(item);
				window.setCaption("Editar Usuario");
				window.beforeEdit();
				UI.getCurrent().addWindow(window);
				
				window.addListener(new SaveUserListener() {
					
					@Override
					public void saveUser(SaveUserEvent event) {
//						clearCacheEvent.fire(new ClearCacheEvent(item.getEntity().getUsername()));
						/*Collection<Realm> c = ((RealmSecurityManager)SecurityUtils.getSecurityManager()).getRealms();
						for (Iterator<Realm> iter = c.iterator(); iter.hasNext();) {
							Realm realm = iter.next();
							if("com.rhemsolutions.app.security.shiro.MyRealm_0".equals(realm.getName())){
								MyRealm myRealm = (MyRealm)realm;
								myRealm.clearCachedAuthorizationInfo(item.getEntity().getUsername());
							}
						}*/
						
						window.close();
					}
				});
			}
		});
		
		deleteButton.addClickListener(new ClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				final Set<Integer> list = (Set<Integer>) table.getValue();
				ConfirmDialog.show(UI.getCurrent(),"Confirmar","Esta Seguro que desea eliminar?","Si deseo Eliminar","Cancelar", new ConfirmDialog.Listener() {
					
					@Override
					public void onClose(ConfirmDialog dialog) {
						if(dialog.isConfirmed()){
							Iterator<Integer> iter = list.iterator();
							while(iter.hasNext()){
								container.removeItem(iter.next());
							}
							table.setValue(null);
						}
					}
				});

			}
		});
		
		keyButton.addClickListener(new ClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				Set<Integer> list = (Set<Integer>) table.getValue();
				Item item = table.getItem(list.iterator().next());
				final UserWindow window = new UserWindow();
				window.bind(item);
				window.setCaption("Cambiar Contraseña");
				window.beforeChangePassword();
				UI.getCurrent().addWindow(window);
				
				window.addListener(new SaveUserListener() {
					
					@Override
					public void saveUser(SaveUserEvent event) {
						window.close();
					}
				});
			}
		});
		
		enableButton.addClickListener(new ClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				final Set<Integer> list = (Set<Integer>) table.getValue();
				Iterator<Integer> iter = list.iterator();
				while(iter.hasNext()){
					JPAContainerItem<User> item = (JPAContainerItem<User>) table.getItem(iter.next());
					User entity = item.getEntity();
					entity.setEnabled(!entity.getEnabled());
					container.addEntity(entity);
				}
				container.commit();
			}
		});
		
		clearFiltersButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				table.resetFilters();			
			}
		});
		
		table.addValueChangeListener(new ValueChangeListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Set<Integer> list = (Set<Integer>) table.getValue();
				if(list.size() == 0){
					editButton.setEnabled(false);
					deleteButton.setEnabled(false);
					keyButton.setEnabled(false);
					enableButton.setEnabled(false);
				}else if(list.size() == 1){
					editButton.setEnabled(true);
					deleteButton.setEnabled(true);
					keyButton.setEnabled(true);
					enableButton.setEnabled(true);
				}else if(list.size() > 1){
					editButton.setEnabled(false);
					deleteButton.setEnabled(true);
					keyButton.setEnabled(false);
					enableButton.setEnabled(true);
				}
			}
		});
		
		toolbar.setWidth("100%");
		setSizeFull();
		
		toolbar.addComponents(addButton,  editButton, deleteButton, keyButton, enableButton, empty, clearFiltersButton);
		addComponents(title, toolbar, table);
		
		toolbar.setExpandRatio(empty, 1);
		setExpandRatio(table, 1);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		if (!initialized) {
			build();
			initialized = true;
		}
	}
}
