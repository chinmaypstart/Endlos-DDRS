package com.endlosiot.screen.view;

import com.endlosiot.common.view.IdentifierView;
import com.endlosiot.common.view.View;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class ScreenDataRequestView extends IdentifierView {

    private static final long serialVersionUID = 5678365620021239583L;
    private String screenName;
    private String titleText;
    private String screenDesc;
    private int rowNumber;
    private int colNumber;
    private List<String> columns;
    private List<Row> rows;
    public static class Row {
        private String row;
        private List<Column> columns;

        public String getRow() {
            return row;
        }

        public void setRow(String row) {
            this.row = row;
        }

        public List<Column> getColumns() {
            return columns;
        }

        public void setColumns(List<Column> columns) {
            this.columns = columns;
        }
    }

    public static class Column implements View {
        private String column;
        private String value;
        private boolean isReadOnly;
        private Long id;
        private String address;
        private String decimal;
        private Long min;
        private Long max;
        private String unit;
        private String function;

        private String zeroButtonText;

        private String oneButtonText;

        private Boolean showValueMessage;

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isReadOnly() {
            return isReadOnly;
        }

        public void setReadOnly(boolean readOnly) {
            isReadOnly = readOnly;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDecimal() {
            return decimal;
        }

        public void setDecimal(String decimal) {
            this.decimal = decimal;
        }

        public Long getMin() {
            return min;
        }

        public void setMin(Long min) {
            this.min = min;
        }

        public Long getMax() {
            return max;
        }

        public void setMax(Long max) {
            this.max = max;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getFunction() {
            return function;
        }

        public void setFunction(String function) {
            this.function = function;
        }

        public String getZeroButtonText() {
            return zeroButtonText;
        }

        public void setZeroButtonText(String zeroButtonText) {
            this.zeroButtonText = zeroButtonText;
        }

        public String getOneButtonText() {
            return oneButtonText;
        }

        public void setOneButtonText(String oneButtonText) {
            this.oneButtonText = oneButtonText;
        }

        public Boolean getShowValueMessage() {
            return showValueMessage;
        }

        public void setShowValueMessage(Boolean showValueMessage) {
            this.showValueMessage = showValueMessage;
        }
    }
}