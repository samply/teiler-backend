package de.samply.teiler.app;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TeilerApp implements Serializable {

    private String name;
    private String title;
    private String description;
    private String routerLink;
    private String routerLinkExtension;
    private String singleSpaLink;
    private String sourceUrl;
    private String sourceCheckUrl;

    private String singleSpaMainJs;
    private Boolean isExternLink;
    private Boolean isActivated;
    private Boolean isLocal;
    private TeilerAppRole[] roles;
    private String iconClass;
    private String iconSourceUrl;
    private String backendUrl;
    private String backendCheckUrl;
    private Integer order;
    private Boolean inMenu;

    private Boolean isFrontendReachable;
    private Boolean isBackendReachable;

    private String subroutes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRouterLink() {
        return routerLink;
    }

    public void setRouterLink(String routerLink) {
        this.routerLink = routerLink;
    }

    public String getRouterLinkExtension() {
        return routerLinkExtension;
    }

    public void setRouterLinkExtension(String routerLinkExtension) {
        this.routerLinkExtension = routerLinkExtension;
    }

    public String getSingleSpaLink() {
        return singleSpaLink;
    }

    public void setSingleSpaLink(String singleSpaLink) {
        this.singleSpaLink = singleSpaLink;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Boolean getExternLink() {
        return isExternLink;
    }

    public void setExternLink(Boolean externLink) {
        isExternLink = externLink;
    }

    public void setExternLink(String externLink) {
        if (externLink != null){
            isExternLink = Boolean.valueOf(externLink);
        }
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public Boolean getLocal() {
        return isLocal;
    }

    public void setLocal(Boolean local) {
        isLocal = local;
    }

    public void setLocal(String local) {
        if (local != null){
            isLocal = Boolean.valueOf(local);
        }
    }

    public void setActivated(String activated) {
        if (activated != null){
            isActivated = Boolean.valueOf(activated);
        }
    }

    public TeilerAppRole[] getRoles() {
        return roles;
    }

    public void setRoles(TeilerAppRole[] roles) {
        this.roles = roles;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getIconSourceUrl() {
        return iconSourceUrl;
    }

    public void setIconSourceUrl(String iconSourceUrl) {
        this.iconSourceUrl = iconSourceUrl;
    }

    public String getBackendUrl() {
        return backendUrl;
    }

    public void setBackendUrl(String backendUrl) {
        this.backendUrl = backendUrl;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public void setOrder(String order) {
        if (order != null){
            this.order = Integer.valueOf(order);
        }
    }

    public Boolean getInMenu() {
        return inMenu;
    }

    public void setInMenu(Boolean inMenu) {
        this.inMenu = inMenu;
    }

    public void setInMenu(String inMenu) {
        if (inMenu != null){
            this.inMenu = Boolean.valueOf(inMenu);
        }
    }

    public String getSingleSpaMainJs() {
        return singleSpaMainJs;
    }

    public void setSingleSpaMainJs(String singleSpaMainJs) {
        this.singleSpaMainJs = singleSpaMainJs;
    }

    public Boolean getFrontendReachable() {
        return isFrontendReachable;
    }

    public void setFrontendReachable(Boolean frontendReachable) {
        isFrontendReachable = frontendReachable;
    }

    public Boolean getBackendReachable() {
        return isBackendReachable;
    }

    public void setBackendReachable(Boolean backendReachable) {
        isBackendReachable = backendReachable;
    }

    public String getSubroutes() {
        return subroutes;
    }

    public void setSubroutes(String subroutes) {
        this.subroutes = subroutes;
    }

    public String getSourceCheckUrl() {
        return sourceCheckUrl;
    }

    public void setSourceCheckUrl(String sourceCheckUrl) {
        this.sourceCheckUrl = sourceCheckUrl;
    }

    public String getBackendCheckUrl() {
        return backendCheckUrl;
    }

    public void setBackendCheckUrl(String backendCheckUrl) {
        this.backendCheckUrl = backendCheckUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TeilerApp teilerApp = (TeilerApp) o;

        boolean isEquals = new EqualsBuilder().append(isExternLink, teilerApp.isExternLink)
                .append(name, teilerApp.name)
                .append(title, teilerApp.title)
                .append(description, teilerApp.description)
                .append(routerLink, teilerApp.routerLink)
                .append(routerLinkExtension, teilerApp.routerLinkExtension)
                .append(singleSpaLink, teilerApp.singleSpaLink)
                .append(sourceUrl, teilerApp.sourceUrl)
                .append(sourceCheckUrl, teilerApp.sourceCheckUrl)
                .append(isExternLink, teilerApp.isExternLink)
                .append(isActivated, teilerApp.isActivated)
                .append(isLocal, teilerApp.isLocal)
                .append(isFrontendReachable, teilerApp.isFrontendReachable)
                .append(isBackendReachable, teilerApp.isBackendReachable)
                .append(iconClass, teilerApp.iconClass)
                .append(iconSourceUrl, teilerApp.iconSourceUrl)
                .append(backendUrl, teilerApp.backendUrl)
                .append(backendCheckUrl, teilerApp.backendCheckUrl)
                .append(order, teilerApp.order)
                .append(inMenu, teilerApp.inMenu)
                .append(singleSpaMainJs, teilerApp.singleSpaMainJs)
                .append(subroutes, teilerApp.subroutes)
                .isEquals();

        if (isEquals) {
            if (roles == teilerApp.roles) return true;
            if (roles == null || roles.getClass() != teilerApp.roles.getClass() || roles.length != teilerApp.roles.length)
                return false;
            Set<TeilerAppRole> rolesSet = new HashSet<>(Arrays.stream(roles).toList());
            return Arrays.stream(teilerApp.roles).filter(role -> !rolesSet.contains(role)).toList().size() == 0;
        }

        return isEquals;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(title)
                .append(description)
                .append(routerLink)
                .append(routerLinkExtension)
                .append(singleSpaLink)
                .append(sourceUrl)
                .append(sourceCheckUrl)
                .append(isExternLink)
                .append(isActivated)
                .append(isLocal)
                .append(isFrontendReachable)
                .append(isBackendReachable)
                .append(roles)
                .append(iconClass)
                .append(iconSourceUrl)
                .append(backendUrl)
                .append(backendCheckUrl)
                .append(order)
                .append(inMenu)
                .append(singleSpaMainJs)
                .append(subroutes)
                .toHashCode();
    }

    @Override
    public TeilerApp clone() {
        return SerializationUtils.clone(this);
    }

}
