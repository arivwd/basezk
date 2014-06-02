package com.wd.basezk.composer.usergroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Grid;

import com.wd.basezk.model.CuserGrp;
import com.wd.basezk.service.CuserGrpService;

/**
 * @author (ariv.wd@gmail.com)
 * <created-on-20140328-stabilized-vm>
 *
 */

/*Anotasi supaya STS bisa mendeteksi VM ini sebagai sebuah Bean*/
@Controller
/*Anotasi supaya semua Spring-Bean bisa dideteksi oleh ZK*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)

public class ListUserGroupVM {
    // Default Variables untuk VM-Component
    @Wire("#gridList")
    private Grid gridList;

    // Default Variables untuk VM-Model
    //--------------------------> [TidakAda]

    // Untuk WireComponentSelector
    private Component wComSel;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    @WireVariable
    private CuserGrpService cuserGrpService;

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    private List<CuserGrp> allUserGrps;

    // Untuk Wiring Renderer (butuh: Setter Getter)
    //--------------------------> [TidakAda]

    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        setwComSel(view);
        wiringComponent();
        loadData();
    }

/*************************************************************************************
 * Do's (Berisi kumpulan Command yang dipanggil dari ZUL, diawali dengan kata "do")
 **************************************************************************************/
    @Command
    public void doNew() throws InterruptedException {
        this.executeDetail( new CuserGrp() );
    }

    @Command
    public void doEdit() {

    }

    @Command
    public void doDelete() {

    }

    @Command
    public void doSearch() {

    }

    @Command
    public void doPrintPreview() {

    }

    @Command
    public void doRefresh() {

    }

/*************************************************************************************
 * Event dan Listener (Diawali dengan "on" / Fungsinya sama dengan Do's, yaitu Command)
 **************************************************************************************/


/*************************************************************************************
 * Custom Methods (Untuk method-method private)
 **************************************************************************************/
    public void executeDetail(CuserGrp cusergrpNya) throws InterruptedException {
        try {
            Executions.createComponents("/frontend/core/usergroup/vFormUserGroup.zul", null, null);
        } catch (Exception e) {
            //LOGGING
        }
    }

    private void loadData() {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("null", "null");
        allUserGrps = getCuserGrpService().getByRequest(requestMap, false, null);
    }

/*************************************************************************************
 * Renderer
 **************************************************************************************/
    // Untuk Wiring Component yang ada di ZUL apabila diperlukan,
    // Persiapan Wiring Component via wComSel - (DEFAULT)
    private void wiringComponent() {

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

    public List<CuserGrp> getAllUserGrps() {
        return allUserGrps;
    }
    public void setAllUserGrps(List<CuserGrp> allUserGrps) {
        this.allUserGrps = allUserGrps;
    }

    public CuserGrpService getCuserGrpService() {
        return cuserGrpService;
    }
    public void setCuserGrpService(CuserGrpService cuserGrpService) {
        this.cuserGrpService = cuserGrpService;
    }

}
