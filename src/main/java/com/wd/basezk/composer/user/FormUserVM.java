package com.wd.basezk.composer.user;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

import org.springframework.stereotype.Controller;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import com.wd.basezk.model.Cuser;
import com.wd.basezk.service.CuserService;

/**
 * @author (ariv.wd@gmail.com)
 * <created-on-20140328-stabilized-vm>
 *
 */

/*Anotasi supaya STS bisa mendeteksi VM ini sebagai sebuah Bean*/
@Controller
/*Anotasi supaya semua Spring-Bean bisa dideteksi oleh ZK*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)

public class FormUserVM {
    // Default Variables untuk VM-Component
    @Wire("#dialogWindow")
    private Window dialogWindow;
    @Wire("#txtCpass")
    private Textbox txtCpass;

    // Default Variables untuk VM-Model
    //--------------------------> [TidakAda]

    // Untuk WireComponentSelector
    private Component wComSel;
    private ListUserVM wObjList;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    @WireVariable
    private CuserService cuserService;

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    private String confirmPassword = "";
    private Cuser selected = new Cuser();
    private Map<String, Integer> txtMaxLength;

    // Untuk Wiring Renderer (butuh: Setter Getter)
    //--------------------------> [TidakAda]

/*************************************************************************************
 * Initialize
 **************************************************************************************/
    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("objListCtrl") ListUserVM arg, @ExecutionArgParam("selected") Cuser arg2) {
        Selectors.wireComponents(view, this, false);
        setwComSel(view);
        if (arg != null) { setwObjList(arg); }
        if (arg2 != null) { setSelected(arg2); }
        initComponent();
        prepareAll();
    }

/*************************************************************************************
 * Preparation (Load Variables Value)
 **************************************************************************************/
    private void prepareAll() {
        txtMaxLength = new HashMap<String, Integer>();
        try {
            setMaxLength4All();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (selected.getCuserId() != null) {
            confirmPassword = selected.getCuserPassword();
        }
    }

/*************************************************************************************
 * Do's (Berisi kumpulan Command yang dipanggil dari ZUL, diawali dengan kata "do")
 **************************************************************************************/
    @Command
    @NotifyChange("selected")
    public void doSave() throws InterruptedException {
        if (selected.getCuserId() == null) {
            manualValidateForm();
            getCuserService().insertData(selected);
            getwObjList().doRefresh();
        } else {
            manualValidateForm();
            doEdit();
        }
        dialogWindow.setPosition("nocenter");
    }

    private void doEdit() {
        getCuserService().updateData(selected);
        getwObjList().doRefresh();
        BindUtils.postNotifyChange(null, null, this, "selected");
        dialogWindow.setPosition("nocenter");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void doDelete() {
        final Map<String, Cuser> objsToDel = new HashMap<String, Cuser>();
        final Window windowNya = dialogWindow;
        objsToDel.put(selected.getCuserId(), selected);

        // ----------------------------------------------------------
        // Show a confirm box
        // ----------------------------------------------------------
        //TODO: Labeling!
        Messagebox.show("XXXXXXXXXXXX", "Confirmation", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if(((Integer)event.getData()).intValue()==Messagebox.YES){
                    getwObjList().getDeletingData(objsToDel);
                    Events.postEvent(Events.ON_CLOSE, windowNya, null);
                }
            }
        });
        // ----------------------------------------------------------
        dialogWindow.setPosition("nocenter");
    }

    @Command
    @NotifyChange("selected")
    public void doReload() { }

    @Command
    public void doClose(@BindingParam("eventNya") Event eventNya) throws InterruptedException {
        dialogWindow.onClose();
    }

/*************************************************************************************
 * Event dan Listener (Diawali dengan "on" / Fungsinya sama dengan Do's, yaitu Command)
 **************************************************************************************/


/*************************************************************************************
 * Custom Methods (Untuk method-method private)
 **************************************************************************************/
    private void setMaxLength4All() throws NoSuchFieldException, SecurityException {
        Field[] fields = selected.getClass().getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            if (fields[i].getType().toString().toLowerCase().contains("string")) {
                txtMaxLength.put(fields[i].getName(), selected.getClass().getDeclaredField(fields[i].getName()).getAnnotation(Column.class).length());
            }
        }
    }

/*************************************************************************************
 * Validator
 **************************************************************************************/
    public Validator getValidateTextboxNotNull() {
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                InputElement componentNya = (InputElement)ctx.getBindContext().getValidatorArg("component");
                String text = (String)ctx.getBindContext().getValidatorArg("text");
                Clients.clearWrongValue(componentNya);
                if(text.trim().equals("") || text == null) {
                    componentNya.setFocus(true);
                    throw new WrongValueException(componentNya, "Required Field!");
                }
            }
        };
    }

    private void manualValidateForm() {
        //Cek Password
        if (selected.getCuserPassword() != null) {
            if (selected.getCuserPassword().equals(confirmPassword) == false) {
                txtCpass.setFocus(true);
                throw new WrongValueException(txtCpass, "Doesnt match!");
            } else {
                selected.setCuserPassword(confirmPassword);
            }
        }

        //Set boolean status aktif.
        if (selected.getCuserActive() == null) {
            selected.setCuserActive(false);
        }
    }

/*************************************************************************************
 * Renderer
 **************************************************************************************/
    private void initComponent() {

    }

/*************************************************************************************
 * Set dan Get
 **************************************************************************************/
    public Component getwComSel() {
        return wComSel;
    }
    public void setwComSel(Component wComSel) {
        this.wComSel = wComSel;
    }

    public ListUserVM getwObjList() {
        return wObjList;
    }
    public void setwObjList(ListUserVM wObjList) {
        this.wObjList = wObjList;
    }

    public Cuser getSelected() {
        return selected;
    }
    public void setSelected(Cuser selected) {
        this.selected = selected;
    }

    public Map<String, Integer> getTxtMaxLength() {
        return txtMaxLength;
    }
    public void setTxtMaxLength(Map<String, Integer> txtMaxLength) {
        this.txtMaxLength = txtMaxLength;
    }

    public CuserService getCuserService() {
        return cuserService;
    }
    public void setCuserService(CuserService cuserService) {
        this.cuserService = cuserService;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
