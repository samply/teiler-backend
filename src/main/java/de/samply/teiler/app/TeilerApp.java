package de.samply.teiler.app;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TeilerApp {

    private String name;
    private String title;
    private String description;
    private String routerLink;
    private String singleSpaLink;
    private String sourceLink;
    private Boolean isExternLink = false;
    private TeilerAppRole[] roles;

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

    public String getSingleSpaLink() {
        return singleSpaLink;
    }

    public void setSingleSpaLink(String singleSpaLink) {
        this.singleSpaLink = singleSpaLink;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public boolean isExternLink() {
        return isExternLink;
    }

    public void setExternLink(Boolean externLink) {
        isExternLink = externLink;
    }

    public void setExternLink(String externLink) {
        isExternLink = Boolean.valueOf(externLink);
    }

    public TeilerAppRole[] getRoles() {
        return roles;
    }

    public void setRoles(TeilerAppRole[] roles) {
        this.roles = roles;
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
                .append(singleSpaLink, teilerApp.singleSpaLink)
                .append(sourceLink, teilerApp.sourceLink).isEquals();

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
        return new HashCodeBuilder(17, 37).append(name).append(title).append(description).append(routerLink).append(singleSpaLink).append(sourceLink).append(isExternLink).append(roles).toHashCode();
    }
}
