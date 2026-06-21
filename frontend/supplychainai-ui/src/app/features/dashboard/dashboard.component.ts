import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  kpis = [
    { label: 'Total Suppliers', value: '156', icon: 'business', iconColor: '#1976d2', change: 12, changeLabel: 'vs last month' },
    { label: 'Inventory Items', value: '2,847', icon: 'inventory_2', iconColor: '#388e3c', change: 8, changeLabel: 'vs last month' },
    { label: 'Active Orders', value: '89', icon: 'shopping_cart', iconColor: '#f57c00', change: -3, changeLabel: 'vs last month' },
    { label: 'Shipments Today', value: '34', icon: 'local_shipping', iconColor: '#d32f2f', change: 5, changeLabel: 'vs yesterday' },
  ];

  recentOrders = [
    { id: 'ORD-001', customer: 'TechSupply Co.', status: 'Delivered', amount: '$45,200', date: '2024-12-15' },
    { id: 'ORD-002', customer: 'GlobalParts Ltd.', status: 'In Transit', amount: '$12,800', date: '2024-12-14' },
    { id: 'ORD-003', customer: 'RawMaterials Inc.', status: 'Pending', amount: '$89,500', date: '2024-12-13' },
    { id: 'ORD-004', customer: 'PackPro Solutions', status: 'Processing', amount: '$23,100', date: '2024-12-12' },
    { id: 'ORD-005', customer: 'ElectroSys Corp.', status: 'Delivered', amount: '$67,300', date: '2024-12-11' },
  ];

  lowStockItems = [
    { product: 'Titanium Bolts', sku: 'SKU-TITAN-082', quantity: 12, reorderPoint: 50 },
    { product: 'Aluminum Sheets', sku: 'SKU-ALUM-044', quantity: 8, reorderPoint: 40 },
    { product: 'Copper Wire Spool', sku: 'SKU-COPR-121', quantity: 3, reorderPoint: 30 },
    { product: 'Steel Rods 12mm', sku: 'SKU-STEL-067', quantity: 25, reorderPoint: 60 },
    { product: 'Rubber Gaskets', sku: 'SKU-RUBR-033', quantity: 18, reorderPoint: 45 },
  ];

  recentOrderColumns = ['id', 'customer', 'status', 'amount', 'date'];
  lowStockColumns = ['product', 'sku', 'quantity', 'reorderPoint'];
}
