package com.agentpay.service;

import com.agentpay.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final List<Product> catalog = new ArrayList<>();
    // Stores cross-platform prices: Product ID -> Map of Platform Name to Price
    private final Map<String, Map<String, Double>> competitorPrices = new HashMap<>();

    public CatalogService() {
        catalog.add(new Product("p1", "Boat Nirvana ANC Earbuds", "audio", 2499.00, 
            "Active Noise Cancellation, 40H Playtime, Quad Mics", 
            "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=500&q=80"));

        catalog.add(new Product("p2", "Sony WH-1000XM4 Headphones", "audio", 19999.00, 
            "Industry leading noise cancellation, 30H battery", 
            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&q=80"));

        catalog.add(new Product("p3", "Nike Air Zoom Running Shoes", "footwear", 4999.00, 
            "Lightweight breathable mesh, responsive Zoom Air cushioning", 
            "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500&q=80"));

        catalog.add(new Product("p4", "Adidas Ultraboost Sneakers", "footwear", 6999.00, 
            "Continental Rubber outsole, high energy return Boost sole", 
            "https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=500&q=80"));

        catalog.add(new Product("p5", "Redragon K552 RGB Mechanical Keyboard", "electronics", 2899.00, 
            "Compact 87-key RGB backlit, clicky Outemu Blue switches", 
            "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500&q=80"));

        catalog.add(new Product("p6", "Logitech MX Master 3S Wireless Mouse", "electronics", 7495.00, 
            "Quiet clicks, 8K DPI any-surface sensor, ergonomic design", 
            "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=500&q=80"));

        catalog.add(new Product("p7", "Noise ColorFit Ultra Smartwatch", "wearables", 1799.00, 
            "1.8 inch TruView display, 60 sports modes, SpO2 & Heart Rate", 
            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500&q=80"));

        catalog.add(new Product("p8", "Apple AirPods Pro (2nd Gen)", "audio", 24900.00, 
            "H2 chip, Adaptive Audio, USB-C MagSafe charging case", 
            "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=500&q=80"));

        // Setup competitor prices across top platforms
        setupCompetitorPrices();
    }

    private void setupCompetitorPrices() {
        competitorPrices.put("p1", Map.of("Amazon", 2699.0, "Flipkart", 2599.0, "Croma", 2799.0));
        competitorPrices.put("p2", Map.of("Amazon", 21990.0, "Flipkart", 22490.0, "Reliance Digital", 22990.0));
        competitorPrices.put("p3", Map.of("Amazon", 5399.0, "Flipkart", 5199.0, "Myntra", 5099.0));
        competitorPrices.put("p4", Map.of("Amazon", 7499.0, "Flipkart", 7299.0, "Myntra", 7199.0));
        competitorPrices.put("p5", Map.of("Amazon", 3199.0, "Flipkart", 3099.0, "MD Computers", 2999.0));
        competitorPrices.put("p6", Map.of("Amazon", 7995.0, "Flipkart", 8199.0, "Croma", 8499.0));
        competitorPrices.put("p7", Map.of("Amazon", 1999.0, "Flipkart", 1899.0, "Croma", 2099.0));
        competitorPrices.put("p8", Map.of("Amazon", 25499.0, "Flipkart", 25900.0, "Apple Store", 26900.0));
    }

    public Map<String, Double> getCompetitorPrices(String productId) {
        return competitorPrices.getOrDefault(productId, Map.of("Amazon", 0.0, "Flipkart", 0.0));
    }

    public List<Product> searchProducts(String keyword, Double maxPrice) {
        return catalog.stream()
                .filter(p -> (keyword == null || keyword.isEmpty() ||
                              p.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                              p.getCategory().toLowerCase().contains(keyword.toLowerCase())))
                .filter(p -> (maxPrice == null || p.getPrice() <= maxPrice))
                .collect(Collectors.toList());
    }

    public Optional<Product> getProductById(String id) {
        return catalog.stream().filter(p -> p.getId().equalsIgnoreCase(id)).findFirst();
    }

    public double calculateDiscountedPrice(Product product, String couponCode) {
        double originalPrice = product.getPrice();
        if (couponCode == null || couponCode.trim().isEmpty()) {
            return originalPrice;
        }

        switch (couponCode.toUpperCase().trim()) {
            case "HDFC10":
                return Math.round(originalPrice * 0.90 * 100.0) / 100.0;
            case "UPI200":
                return Math.max(0, originalPrice - 200.0);
            case "STUDENT":
                return Math.round(originalPrice * 0.85 * 100.0) / 100.0;
            default:
                return originalPrice;
        }
    }
}