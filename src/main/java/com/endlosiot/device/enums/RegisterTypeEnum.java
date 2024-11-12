package com.endlosiot.device.enums;

import com.endlosiot.common.enums.EnumType;

import java.util.HashMap;
import java.util.Map;

public enum RegisterTypeEnum implements EnumType {

	HOLDING_REGISTER(1, "Holding Register"), HOLDING_COIL(2, "Holding Coil");

	private final int id;
	private final String name;
	public static final Map<Integer, RegisterTypeEnum> MAP = new HashMap<>();

	static {
		for (RegisterTypeEnum registerTypeEnum : values()) {
			MAP.put(registerTypeEnum.getId(), registerTypeEnum);
		}
	}

	RegisterTypeEnum(int id, String name) {
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
	 * @return machineStatusEnum
	 */
	public static RegisterTypeEnum fromId(Integer id) {
		return MAP.get(id);
	}
}
