package com.endlosiot.common.user.model;

import com.endlosiot.common.model.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RoleModuleRightsKey implements Model {

    private static final long serialVersionUID = -6790118058849723368L;

    @Column(name = "fkmoduleid", nullable = false)
    private Long moduleId;

    @Column(name = "fkroleid", nullable = false)
    private Long roleId;

    @Column(name = "fkrightsid", nullable = false)
    private Long rightsId;

    public RoleModuleRightsKey() {
    }

    public RoleModuleRightsKey(Long roleId, Long moduleId, Long rightsId) {
        super();
        this.roleId = roleId;
        this.moduleId = moduleId;
        this.rightsId = rightsId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRightsId() {
        return rightsId;
    }

    public void setRightsId(Long rightsId) {
        this.rightsId = rightsId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((moduleId == null) ? 0 : moduleId.hashCode());
        result = prime * result + ((rightsId == null) ? 0 : rightsId.hashCode());
        result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoleModuleRightsKey other = (RoleModuleRightsKey) obj;
        if (moduleId == null) {
            if (other.moduleId != null)
                return false;
        } else if (!moduleId.equals(other.moduleId))
            return false;
        if (rightsId == null) {
            if (other.rightsId != null)
                return false;
        } else if (!rightsId.equals(other.rightsId))
            return false;
        if (roleId == null) {
            if (other.roleId != null)
                return false;
        } else if (!roleId.equals(other.roleId))
            return false;
        return true;
    }
}
