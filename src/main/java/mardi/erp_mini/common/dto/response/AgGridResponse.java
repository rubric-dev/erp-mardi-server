package mardi.erp_mini.common.dto.response;

import java.util.List;

public class AgGridResponse<T> {
    private List<T> rows;
    private Integer lastRow;

    public AgGridResponse(List<T> rows, Integer lastRow) {
        this.rows = rows;
        this.lastRow = lastRow;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getLastRow() {
        return lastRow;
    }

    public void setLastRow(Integer lastRow) {
        this.lastRow = lastRow;
    }
}
