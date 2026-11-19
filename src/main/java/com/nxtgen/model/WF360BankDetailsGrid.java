package com.WF360.model;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data @AllArgsConstructor
public class WF360BankDetailsGrid { private Map<String, String> metadata; private List<WF360DataGridColumn> columns; private List<WF360BankDetail> rows; }
