import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';

export interface PurchaseOrder {
  poNumber: string;
  supplier: string;
  status: 'Draft' | 'Pending Approval' | 'Approved' | 'Ordered' | 'Received' | 'Cancelled';
  totalAmount: number;
  orderDate: string;
  deliveryDate: string;
  items: number;
}

@Component({
  selector: 'app-po',
  standalone: false,
  templateUrl: './po.component.html',
  styleUrl: './po.component.scss'
})
export class PoComponent implements AfterViewInit {
  displayedColumns: string[] = ['poNumber', 'supplier', 'status', 'totalAmount', 'orderDate', 'deliveryDate', 'items', 'actions'];
  dataSource: MatTableDataSource<PurchaseOrder>;
  selection = new SelectionModel<PurchaseOrder>(false, []);

  statuses: string[] = ['Draft', 'Pending Approval', 'Approved', 'Ordered', 'Received', 'Cancelled'];
  selectedStatus: string = '';

  private poList: PurchaseOrder[] = [
    { poNumber: 'PO-2024-0001', supplier: 'TechSupply Corp', status: 'Approved', totalAmount: 45200.00, orderDate: '2024-01-15', deliveryDate: '2024-02-20', items: 12 },
    { poNumber: 'PO-2024-0002', supplier: 'GlobalParts Ltd', status: 'Ordered', totalAmount: 18750.50, orderDate: '2024-01-18', deliveryDate: '2024-02-28', items: 8 },
    { poNumber: 'PO-2024-0003', supplier: 'RawMaterials Inc', status: 'Received', totalAmount: 92000.00, orderDate: '2024-01-05', deliveryDate: '2024-02-01', items: 24 },
    { poNumber: 'PO-2024-0004', supplier: 'Pacific Traders', status: 'Draft', totalAmount: 15800.75, orderDate: '2024-02-01', deliveryDate: '2024-03-10', items: 5 },
    { poNumber: 'PO-2024-0005', supplier: 'Quality Goods Co', status: 'Pending Approval', totalAmount: 76300.00, orderDate: '2024-02-10', deliveryDate: '2024-03-25', items: 18 },
    { poNumber: 'PO-2024-0006', supplier: 'Northern Supply', status: 'Cancelled', totalAmount: 12400.00, orderDate: '2024-01-20', deliveryDate: '2024-02-15', items: 7 },
    { poNumber: 'PO-2024-0007', supplier: 'Metro Distributors', status: 'Approved', totalAmount: 33500.00, orderDate: '2024-02-14', deliveryDate: '2024-03-30', items: 10 },
    { poNumber: 'PO-2024-0008', supplier: 'GreenField Supplies', status: 'Ordered', totalAmount: 28900.25, orderDate: '2024-02-20', deliveryDate: '2024-04-05', items: 14 },
    { poNumber: 'PO-2024-0009', supplier: 'Apex Components', status: 'Pending Approval', totalAmount: 56700.00, orderDate: '2024-03-01', deliveryDate: '2024-04-15', items: 20 },
    { poNumber: 'PO-2024-0010', supplier: 'Coastal Logistics', status: 'Received', totalAmount: 44100.50, orderDate: '2024-01-25', deliveryDate: '2024-02-18', items: 9 },
    { poNumber: 'PO-2024-0011', supplier: 'Summit Materials', status: 'Draft', totalAmount: 21000.00, orderDate: '2024-03-05', deliveryDate: '2024-04-20', items: 6 },
    { poNumber: 'PO-2024-0012', supplier: 'Valley Supplies Inc', status: 'Approved', totalAmount: 88500.00, orderDate: '2024-03-10', deliveryDate: '2024-04-25', items: 22 }
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    this.dataSource = new MatTableDataSource(this.poList);
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = (data: PurchaseOrder, filter: string) => {
      const filterValue = filter.trim().toLowerCase();
      if (!filterValue) return true;
      if (this.selectedStatus && data.status.toLowerCase() !== filterValue) return false;
      return data.poNumber.toLowerCase().includes(filterValue) ||
             data.supplier.toLowerCase().includes(filterValue);
    };
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  applyStatusFilter(status: string): void {
    this.selectedStatus = status;
    this.dataSource.filter = status.toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Draft': return 'status-draft';
      case 'Pending Approval': return 'status-pending';
      case 'Approved': return 'status-approved';
      case 'Ordered': return 'status-ordered';
      case 'Received': return 'status-received';
      case 'Cancelled': return 'status-cancelled';
      default: return '';
    }
  }
}
