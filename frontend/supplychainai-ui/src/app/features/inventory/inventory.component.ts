import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

interface InventoryItem {
  product: string;
  sku: string;
  warehouse: string;
  onHand: number;
  reserved: number;
  available: number;
  reorderPoint: number;
  status: string;
}

@Component({
  selector: 'app-inventory',
  standalone: false,
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.scss'
})
export class InventoryComponent implements AfterViewInit {
  displayedColumns: string[] = ['product', 'sku', 'warehouse', 'onHand', 'reserved', 'available', 'reorderPoint', 'status', 'actions'];
  dataSource: MatTableDataSource<InventoryItem>;
  warehouses: string[] = ['All Warehouses', 'Warehouse A', 'Warehouse B', 'Warehouse C'];
  selectedWarehouse: string = 'All Warehouses';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    const items: InventoryItem[] = [
      { product: 'Steel Rods', sku: 'SKU-001', warehouse: 'Warehouse A', onHand: 500, reserved: 50, reorderPoint: 100, available: 0, status: '' },
      { product: 'Copper Wire', sku: 'SKU-002', warehouse: 'Warehouse A', onHand: 200, reserved: 30, reorderPoint: 50, available: 0, status: '' },
      { product: 'Aluminum Sheets', sku: 'SKU-003', warehouse: 'Warehouse B', onHand: 45, reserved: 5, reorderPoint: 50, available: 0, status: '' },
      { product: 'Plastic Granules', sku: 'SKU-004', warehouse: 'Warehouse B', onHand: 300, reserved: 100, reorderPoint: 80, available: 0, status: '' },
      { product: 'Rubber Gaskets', sku: 'SKU-005', warehouse: 'Warehouse C', onHand: 500, reserved: 20, reorderPoint: 100, available: 0, status: '' },
      { product: 'Finished Widget A', sku: 'SKU-006', warehouse: 'Warehouse A', onHand: 60, reserved: 10, reorderPoint: 30, available: 0, status: '' },
      { product: 'Finished Widget B', sku: 'SKU-007', warehouse: 'Warehouse B', onHand: 8, reserved: 2, reorderPoint: 15, available: 0, status: '' },
      { product: 'Assembly Kit X', sku: 'SKU-008', warehouse: 'Warehouse C', onHand: 25, reserved: 5, reorderPoint: 20, available: 0, status: '' },
      { product: 'Premium Gadget', sku: 'SKU-009', warehouse: 'Warehouse A', onHand: 12, reserved: 3, reorderPoint: 10, available: 0, status: '' },
      { product: 'Basic Tool Set', sku: 'SKU-010', warehouse: 'Warehouse B', onHand: 0, reserved: 0, reorderPoint: 5, available: 0, status: '' },
      { product: 'Corrugated Boxes', sku: 'SKU-011', warehouse: 'Warehouse C', onHand: 1000, reserved: 200, reorderPoint: 500, available: 0, status: '' },
      { product: 'Bubble Wrap Roll', sku: 'SKU-012', warehouse: 'Warehouse A', onHand: 250, reserved: 40, reorderPoint: 100, available: 0, status: '' },
      { product: 'Shipping Labels', sku: 'SKU-013', warehouse: 'Warehouse B', onHand: 5000, reserved: 500, reorderPoint: 1000, available: 0, status: '' },
      { product: 'Pallet Wraps', sku: 'SKU-014', warehouse: 'Warehouse C', onHand: 0, reserved: 0, reorderPoint: 20, available: 0, status: '' },
      { product: 'Desiccant Packets', sku: 'SKU-015', warehouse: 'Warehouse A', onHand: 1500, reserved: 100, reorderPoint: 500, available: 0, status: '' }
    ];

    for (const item of items) {
      item.available = item.onHand - item.reserved;
      if (item.available <= 0) {
        item.status = 'Out of Stock';
      } else if (item.available <= item.reorderPoint) {
        item.status = 'Low Stock';
      } else {
        item.status = 'In Stock';
      }
    }

    this.dataSource = new MatTableDataSource(items);
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

  applyWarehouseFilter(): void {
    this.dataSource.filter = this.selectedWarehouse === 'All Warehouses' ? '' : this.selectedWarehouse.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getStockBarWidth(item: InventoryItem): number {
    if (item.onHand === 0) return 0;
    return Math.round((item.available / item.onHand) * 100);
  }

  getStockBarColor(item: InventoryItem): string {
    if (item.available <= 0) return 'warn';
    if (item.available <= item.reorderPoint) return 'accent';
    return 'primary';
  }

  isLowStock(available: number, reorderPoint: number): boolean {
    return available > 0 && available <= reorderPoint;
  }
}
