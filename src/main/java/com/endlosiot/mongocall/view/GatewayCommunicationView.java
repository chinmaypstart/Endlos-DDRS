package com.endlosiot.mongocall.view;

import com.endlosiot.common.view.View;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GatewayCommunicationView implements View {

    public Id _id;
    public String key;
    public Value value;

    public static class Id {
        public String $oid;
    }

    public static class Value {
        @JsonProperty("TDS")
        public int tDS;
        @JsonProperty("PH")
        public int pH;
        public int flow;
        public int time;
        public String deviceId;
    }
}
