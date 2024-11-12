package com.endlosiot.common.user.enums;

import com.endlosiot.common.enums.EnumType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is Module enum which represent module of system.
 *
 * @author Nirav.Shah
 * @since 08/02/2018
 */
public enum ModuleEnum implements EnumType {

    USER(1, "User") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.DELETE);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.DOWNLOAD);
            rightsEnums.add(RightsEnum.FILE_UPLOAD);
            rightsEnums.add(RightsEnum.ACTIVATION);
            return rightsEnums;
        }
    },
    ROLE(2, "Role") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.DELETE);
            rightsEnums.add(RightsEnum.LIST);
            return rightsEnums;
        }
    },
    DEVICE(3, "Device") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.DELETE);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.DOWNLOAD);
            rightsEnums.add(RightsEnum.FILE_UPLOAD);
            rightsEnums.add(RightsEnum.ACTIVATION);
            return rightsEnums;
        }
    },
    SCREEN(4, "Screen") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.DELETE);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.DOWNLOAD);
            rightsEnums.add(RightsEnum.FILE_UPLOAD);
            rightsEnums.add(RightsEnum.ACTIVATION);
            return rightsEnums;
        }
    },
    RESPONSE_MESSAGE(6, "Response Message") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.LIST);
            return rightsEnums;
        }
    },
    DEVICE_DIAGNOSIS(5, "Device Diagnosis") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.DOWNLOAD);
            return rightsEnums;
        }
    },
    DASHBOARD(7, "Dashboard") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.DOWNLOAD);
            return rightsEnums;
        }
    },
    REPORT(8, "Report") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.DOWNLOAD);
            return rightsEnums;
        }
    },
    SYSTEM_SETTING(9, "System Setting") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.LIST);
            return rightsEnums;
        }
    },
    ALARM_HISTORY(10, "Alarm History") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.LIST);
            return rightsEnums;
        }
    };
    /*NOTIFICATION(4, "Notifcation") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.DELETE);
            rightsEnums.add(RightsEnum.ADD);
            return rightsEnums;
        }
    },
    EMAIL_ACCOUNT(5, "Email Account") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.DELETE);
            rightsEnums.add(RightsEnum.LIST);
            return rightsEnums;
        }
    },

    RESPONSE_MESSAGE(6, "Response Message") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.LIST);
            return rightsEnums;
        }
    },
    SMS_ACCOUNT(7, "Sms Account") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.DELETE);
            return rightsEnums;
        }
    },
    BANNER(8, "Bannner") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.DELETE);
            return rightsEnums;
        }
    },
    LOCATION(9, "Location") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.DELETE);
            rightsEnums.add(RightsEnum.ACTIVATION);
            return rightsEnums;
        }
    },
    CLIENT(10, "Client") {
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.ADD);
            rightsEnums.add(RightsEnum.VIEW);
            rightsEnums.add(RightsEnum.LIST);
            rightsEnums.add(RightsEnum.UPDATE);
            rightsEnums.add(RightsEnum.DELETE);
            rightsEnums.add(RightsEnum.ACTIVATION);
            return rightsEnums;
        }
    },
    ALARAM(13,"Alarm"){
        @Override
        public List<RightsEnum> getAssignedRights() {
            List<RightsEnum> rightsEnums = new ArrayList<>();
            rightsEnums.add(RightsEnum.VIEW);
            return rightsEnums;
        }
    };*/

    private final int id;
    private final String name;

    public static final Map<Integer, ModuleEnum> MAP = new HashMap<>();

    static {
        for (ModuleEnum moduleEnums : values()) {
            MAP.put(moduleEnums.getId(), moduleEnums);
        }
    }

    ModuleEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * This methods is used to fetch Enum base on given id.
     *
     * @param id enum key
     * @return rightsEnums enum
     */
    public static ModuleEnum fromId(Integer id) {
        return MAP.get(id);
    }

    /**
     * Return the list of rights.
     *
     * @return
     */
    public abstract List<RightsEnum> getAssignedRights();
}
