import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

interface Product {
  name: string;
  sku: string;
  category: string;
  price: number;
  stock: number;
  status: string;
}

@Component({
  selector: 'app-products',
  standalone: false,
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss'
})
export class ProductsComponent implements AfterViewInit {
  displayedColumns: string[] = ['name', 'sku', 'category', 'price', 'stock', 'status', 'actions'];
  dataSource: MatTableDataSource<Product>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    const products: Product[] = [
      { name: 'Steel Rods', sku: 'SKU-001', category: 'Raw Materials', price: 45.00, stock: 120, status: 'Active' },
      { name: 'Copper Wire', sku: 'SKU-002', category: 'Raw Materials', price: 32.50, stock: 200, status: 'Active' },
      { name: 'Aluminum Sheets', sku: 'SKU-003', category: 'Raw Materials', price: 78.00, stock: 45, status: 'Active' },
      { name: 'Plastic Granules', sku: 'SKU-004', category: 'Raw Materials', price: 22.00, stock: 300, status: 'Active' },
      { name: 'Rubber Gaskets', sku: 'SKU-005', category: 'Raw Materials', price: 5.50, stock: 500, status: 'Active' },
      { name: 'Finished Widget A', sku: 'SKU-006', category: 'Finished Goods', price: 150.00, stock: 60, status: 'Active' },
      { name: 'Finished Widget B', sku: 'SKU-007', category: 'Finished Goods', price: 220.00, stock: 8, status: 'Active' },
      { name: 'Assembly Kit X', sku: 'SKU-008', category: 'Finished Goods', price: 350.00, stock: 25, status: 'Active' },
      { name: 'Premium Gadget', sku: 'SKU-009', category: 'Finished Goods', price: 599.99, stock: 12, status: 'Active' },
      { name: 'Basic Tool Set', sku: 'SKU-010', category: 'Finished Goods', price: 89.99, stock: 0, status: 'Discontinued' },
      { name: 'Corrugated Boxes', sku: 'SKU-011', category: 'Packaging', price: 3.75, stock: 1000, status: 'Active' },
      { name: 'Bubble Wrap Roll', sku: 'SKU-012', category: 'Packaging', price: 12.00, stock: 250, status: 'Active' },
      { name: 'Shipping Labels', sku: 'SKU-013', category: 'Packaging', price: 0.50, stock: 5000, status: 'Active' },
      { name: 'Pallet Wraps', sku: 'SKU-014', category: 'Packaging', price: 8.25, stock: 0, status: 'Discontinued' },
      { name: 'Desiccant Packets', sku: 'SKU-015', category: 'Packaging', price: 1.20, stock: 1500, status: 'Active' }
    ];
    this.dataSource = new MatTableDataSource(products);
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  deleteProduct(product: Product): void {
    const index = this.dataSource.data.indexOf(product);
    if (index >= 0) {
      this.dataSource.data.splice(index, 1);
      this.dataSource._updateChangeSubscription();
    }
  }

  getStockColor(stock: number): string {
    if (stock > 50) return 'stock-high';
    if (stock > 10) return 'stock-medium';
    return 'stock-low';
  }
}
