package com.dbms.bookstore.services;

import java.util.*;

import com.dbms.bookstore.model.Product;

public class ProductSearchDFS {
    private Map<Long, List<Long>> adjacencyList;

    public ProductSearchDFS() {
        this.adjacencyList = new HashMap<>();
    }

    public void addEdge(Long product1Id, Long product2Id) {
        adjacencyList.computeIfAbsent(product1Id, k -> new LinkedList<>()).add(product2Id);
    }

    public void DFSUtil(Long productId, boolean[] visited, List<Product> productList) {
        visited[productList.indexOf(new Product())] = true;
        System.out.print(productId + " ");

        // Replace the following lines with your actual search criteria
        Product product = productList.get(productList.indexOf(new Product()));
        if (product.getName().contains("STR")) {
            System.out.println(" - Match found: " + product.getName());
        }

        for (Long neighborId : adjacencyList.getOrDefault(productId, Collections.emptyList())) {
            if (!visited[productList.indexOf(new Product())]) {
                DFSUtil(neighborId, visited, productList);
            }
        }
    }

    public void DFS(Long startProductId, List<Product> productList) {
        boolean[] visited = new boolean[productList.size()];
        DFSUtil(startProductId, visited, productList);
    }

    // External invocation of DFS search
    public void performSearch(List<Product> productList, Long startProductId) {
        // Build the adjacency list based on relationships between products
        for (int i = 0; i < productList.size() - 1; i++) {
            addEdge(productList.get(i).getId(), productList.get(i + 1).getId());
        }

        System.out.println("DFS Product Search (starting from product " + startProductId + "):");
        DFS(startProductId, productList);
    }
}

