package com.wd.basezk.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the cmodule database table.
 *
 */
@Entity
@Table(name="cmodule", schema="core")
public class Cmodule implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="cmodule_id", unique=true, nullable=false, length=15)
    private String cmoduleId;

    @Column(name="cmodule_deleteable", nullable=false)
    private Boolean cmoduleDeleteable;

    @Column(name="cmodule_deleteby", length=15)
    private String cmoduleDeleteby;

    @Column(name="cmodule_deleteon")
    private Timestamp cmoduleDeleteon;

    @Column(name="cmodule_desc", length=100)
    private String cmoduleDesc;

    @Column(name="cmodule_inputby", length=15)
    private String cmoduleInputby;

    @Column(name="cmodule_inputon")
    private Timestamp cmoduleInputon;

    @Column(name="cmodule_list_class", nullable=false, length=255)
    private String cmoduleListClass;

    @Column(name="cmodule_list_ui", nullable=false, length=255)
    private String cmoduleListUi;

    @Column(name="cmodule_name", nullable=false, length=30)
    private String cmoduleName;

    @Column(name="cmodule_subid", nullable=false, length=15)
    private String cmoduleSubid;

    @Column(name="cmodule_subid_req", nullable=false, length=15)
    private String cmoduleSubidReq;

    @Column(name="cmodule_subname", nullable=false, length=30)
    private String cmoduleSubname;

    @Column(name="cmodule_subreleaseon")
    private Timestamp cmoduleSubreleaseon;

    @Column(name="cmodule_subrevision", nullable=false, length=30)
    private String cmoduleSubrevision;

    @Column(name="cmodule_subversion", nullable=false, length=30)
    private String cmoduleSubversion;

    @Column(name="cmodule_updateby", length=15)
    private String cmoduleUpdateby;

    @Column(name="cmodule_updateon")
    private Timestamp cmoduleUpdateon;

    @Column(name="cmodule_version", nullable=false, length=30)
    private String cmoduleVersion;

    //bi-directional many-to-one association to Cpriv
    @OneToMany(mappedBy="cmodule")
    private Set<Cpriv> cprivs;

    public Cmodule() {
    }

    public String getCmoduleId() {
        return this.cmoduleId;
    }

    public void setCmoduleId(String cmoduleId) {
        this.cmoduleId = cmoduleId;
    }

    public Boolean getCmoduleDeleteable() {
        return this.cmoduleDeleteable;
    }

    public void setCmoduleDeleteable(Boolean cmoduleDeleteable) {
        this.cmoduleDeleteable = cmoduleDeleteable;
    }

    public String getCmoduleDeleteby() {
        return this.cmoduleDeleteby;
    }

    public void setCmoduleDeleteby(String cmoduleDeleteby) {
        this.cmoduleDeleteby = cmoduleDeleteby;
    }

    public Timestamp getCmoduleDeleteon() {
        return this.cmoduleDeleteon;
    }

    public void setCmoduleDeleteon(Timestamp cmoduleDeleteon) {
        this.cmoduleDeleteon = cmoduleDeleteon;
    }

    public String getCmoduleDesc() {
        return this.cmoduleDesc;
    }

    public void setCmoduleDesc(String cmoduleDesc) {
        this.cmoduleDesc = cmoduleDesc;
    }

    public String getCmoduleInputby() {
        return this.cmoduleInputby;
    }

    public void setCmoduleInputby(String cmoduleInputby) {
        this.cmoduleInputby = cmoduleInputby;
    }

    public Timestamp getCmoduleInputon() {
        return this.cmoduleInputon;
    }

    public void setCmoduleInputon(Timestamp cmoduleInputon) {
        this.cmoduleInputon = cmoduleInputon;
    }

    public String getCmoduleListClass() {
        return this.cmoduleListClass;
    }

    public void setCmoduleListClass(String cmoduleListClass) {
        this.cmoduleListClass = cmoduleListClass;
    }

    public String getCmoduleListUi() {
        return this.cmoduleListUi;
    }

    public void setCmoduleListUi(String cmoduleListUi) {
        this.cmoduleListUi = cmoduleListUi;
    }

    public String getCmoduleName() {
        return this.cmoduleName;
    }

    public void setCmoduleName(String cmoduleName) {
        this.cmoduleName = cmoduleName;
    }

    public String getCmoduleSubid() {
        return this.cmoduleSubid;
    }

    public void setCmoduleSubid(String cmoduleSubid) {
        this.cmoduleSubid = cmoduleSubid;
    }

    public String getCmoduleSubidReq() {
        return this.cmoduleSubidReq;
    }

    public void setCmoduleSubidReq(String cmoduleSubidReq) {
        this.cmoduleSubidReq = cmoduleSubidReq;
    }

    public String getCmoduleSubname() {
        return this.cmoduleSubname;
    }

    public void setCmoduleSubname(String cmoduleSubname) {
        this.cmoduleSubname = cmoduleSubname;
    }

    public Timestamp getCmoduleSubreleaseon() {
        return this.cmoduleSubreleaseon;
    }

    public void setCmoduleSubreleaseon(Timestamp cmoduleSubreleaseon) {
        this.cmoduleSubreleaseon = cmoduleSubreleaseon;
    }

    public String getCmoduleSubrevision() {
        return this.cmoduleSubrevision;
    }

    public void setCmoduleSubrevision(String cmoduleSubrevision) {
        this.cmoduleSubrevision = cmoduleSubrevision;
    }

    public String getCmoduleSubversion() {
        return this.cmoduleSubversion;
    }

    public void setCmoduleSubversion(String cmoduleSubversion) {
        this.cmoduleSubversion = cmoduleSubversion;
    }

    public String getCmoduleUpdateby() {
        return this.cmoduleUpdateby;
    }

    public void setCmoduleUpdateby(String cmoduleUpdateby) {
        this.cmoduleUpdateby = cmoduleUpdateby;
    }

    public Timestamp getCmoduleUpdateon() {
        return this.cmoduleUpdateon;
    }

    public void setCmoduleUpdateon(Timestamp cmoduleUpdateon) {
        this.cmoduleUpdateon = cmoduleUpdateon;
    }

    public String getCmoduleVersion() {
        return this.cmoduleVersion;
    }

    public void setCmoduleVersion(String cmoduleVersion) {
        this.cmoduleVersion = cmoduleVersion;
    }

}
