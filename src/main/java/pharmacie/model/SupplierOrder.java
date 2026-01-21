package pharmacie.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SupplierOrder {
    private int id;
    private Timestamp orderDate;
    private int supplierId;
    private String status; // PENDING, RECEIVED, CANCELLED
    private List<SupplierOrderItem> items = new ArrayList<>();

    // For display
    private String supplierName;

    public SupplierOrder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SupplierOrderItem> getItems() {
        return items;
    }

    public void setItems(List<SupplierOrderItem> items) {
        this.items = items;
    }

    public void addItem(SupplierOrderItem item) {
        this.items.add(item);
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
