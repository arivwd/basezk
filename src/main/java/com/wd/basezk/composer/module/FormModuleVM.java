package com.wd.basezk.composer.module;

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
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import com.wd.basezk.model.Cmodule;
import com.wd.basezk.service.CmoduleService;

/**
 * @author (ariv.wd@gmail.com)
 * <created-on-20140328-stabilized-vm>
 *
 */

/*Anotasi supaya STS bisa mendeteksi VM ini sebagai sebuah Bean*/
@Controller
/*Anotasi supaya semua Spring-Bean bisa dideteksi oleh ZK*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)

public class FormModuleVM {
    // Default Variables untuk VM-Component
    @Wire("#dialogWindow")
    private Window dialogWindow;

    // Default Variables untuk VM-Model
    private static final int FILE_SIZE = 10000;// 100k
    private static final String SAVE_PATH = "\\home\\arif\\Downloads\\";

    // Untuk WireComponentSelector
    private Component wComSel;
    private ListModuleVM wObjList;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    @WireVariable
    private CmoduleService cmoduleService;

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    private Cmodule selected = new Cmodule();
    private Map<String, Integer> txtMaxLength;
    private Media fileUploadNya;

    // Untuk Wiring Renderer (butuh: Setter Getter)
    //--------------------------> [TidakAda]

/*************************************************************************************
 * Initialize
 **************************************************************************************/
    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("objListCtrl") ListModuleVM arg, @ExecutionArgParam("selected") Cmodule arg2) {
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
    }

/*************************************************************************************
 * Do's (Berisi kumpulan Command yang dipanggil dari ZUL, diawali dengan kata "do")
 **************************************************************************************/
    @Command
    @NotifyChange("selected")
    public void doSave() throws InterruptedException {
        if (selected.getCmoduleId() == null) {
            getCmoduleService().insertData(selected);
            getwObjList().doRefresh();
        } else {
            doEdit();
        }
        dialogWindow.setPosition("nocenter");
    }

    private void doEdit() {
        getCmoduleService().updateData(selected);
        getwObjList().doRefresh();
        BindUtils.postNotifyChange(null, null, this, "selected");
        dialogWindow.setPosition("nocenter");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void doDelete() {
        final Map<String, Cmodule> objsToDel = new HashMap<String, Cmodule>();
        final Window windowNya = dialogWindow;
        objsToDel.put(selected.getCmoduleId(), selected);

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

    @NotifyChange({"selected","fileUploadNya"})
    @Command
    public void doUploadFile(@BindingParam("eventNya") UploadEvent eventNya) {
        selected.setCmoduleName("");
        selected.setCmoduleDesc("");
        selected.setCmoduleListClass("");
        selected.setCmoduleListUi("");

        fileUploadNya = eventNya.getMedia();

        try {

            if(fileUploadNya == null){
                // ----------------------------------------------------------
                // Show a confirm box
                // ----------------------------------------------------------
                //TODO: Labeling!
                Messagebox.show("XXXXXXXXXXXX", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
                // ----------------------------------------------------------
            }

            String type = fileUploadNya.getContentType().split("/")[0];
            System.out.println( "type = " + type );

        } catch (Exception e) {
            // TODO: handle exception
        }
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

    public ListModuleVM getwObjList() {
        return wObjList;
    }
    public void setwObjList(ListModuleVM wObjList) {
        this.wObjList = wObjList;
    }

    public Cmodule getSelected() {
        return selected;
    }
    public void setSelected(Cmodule selected) {
        this.selected = selected;
    }

    public Map<String, Integer> getTxtMaxLength() {
        return txtMaxLength;
    }
    public void setTxtMaxLength(Map<String, Integer> txtMaxLength) {
        this.txtMaxLength = txtMaxLength;
    }

    public Media getFileUploadNya() {
        return fileUploadNya;
    }
    public void setFileUploadNya(Media fileUploadNya) {
        this.fileUploadNya = fileUploadNya;
    }

    public CmoduleService getCmoduleService() {
        return cmoduleService;
    }
    public void setCmoduleService(CmoduleService cmoduleService) {
        this.cmoduleService = cmoduleService;
    }

}
