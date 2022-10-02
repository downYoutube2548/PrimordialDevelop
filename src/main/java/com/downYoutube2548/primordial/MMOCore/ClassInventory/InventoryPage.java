package com.downYoutube2548.primordial.MMOCore.ClassInventory;

public class InventoryPage {
    public static Boolean isPageValid(int amount, int page, int size) {
        if (page <= 0) {
            return false;
        } else {
            int upperBound = page * size;
            int lowerBound = upperBound - size;
            double a = Math.ceil(Double.parseDouble(String.valueOf(amount))/Double.parseDouble(String.valueOf(size)));
            int c = page+2;

            return (amount > lowerBound && c < a);
        }
    }
}
