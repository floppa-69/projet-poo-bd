package pharmacie.model;

public class SupplierPerformance {
    private String supplierName;
    private double totalSpent;

    public SupplierPerformance(String supplierName, double totalSpent) {
        this.supplierName = supplierName;
        this.totalSpent = totalSpent;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public double getTotalSpent() {
        return totalSpent;
    }
}
