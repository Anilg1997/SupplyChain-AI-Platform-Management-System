import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

export interface Shipment {
  trackingId: string;
  origin: string;
  destination: string;
  status: 'Pending' | 'In Transit' | 'Delivered' | 'Delayed';
  carrier: string;
  estimatedDelivery: string;
  progress: number;
}

@Component({
  selector: 'app-shipments',
  standalone: false,
  templateUrl: './shipments.component.html',
  styleUrl: './shipments.component.scss'
})
export class ShipmentsComponent implements AfterViewInit {
  displayedColumns: string[] = ['trackingId', 'origin', 'destination', 'status', 'carrier', 'estimatedDelivery', 'progress', 'actions'];
  dataSource: MatTableDataSource<Shipment>;

  carriers: string[] = ['FedEx', 'UPS', 'DHL'];
  selectedCarrier: string = '';

  private shipmentsList: Shipment[] = [
    { trackingId: 'SHP-0001', origin: 'Shanghai, China', destination: 'Los Angeles, USA', status: 'In Transit', carrier: 'FedEx', estimatedDelivery: '2024-03-20', progress: 45 },
    { trackingId: 'SHP-0002', origin: 'Hamburg, Germany', destination: 'New York, USA', status: 'Delivered', carrier: 'UPS', estimatedDelivery: '2024-02-28', progress: 100 },
    { trackingId: 'SHP-0003', origin: 'Mumbai, India', destination: 'London, UK', status: 'Pending', carrier: 'DHL', estimatedDelivery: '2024-04-05', progress: 0 },
    { trackingId: 'SHP-0004', origin: 'Tokyo, Japan', destination: 'San Francisco, USA', status: 'In Transit', carrier: 'FedEx', estimatedDelivery: '2024-03-25', progress: 60 },
    { trackingId: 'SHP-0005', origin: 'Rotterdam, Netherlands', destination: 'Chicago, USA', status: 'Delayed', carrier: 'UPS', estimatedDelivery: '2024-03-15', progress: 30 },
    { trackingId: 'SHP-0006', origin: 'Singapore', destination: 'Sydney, Australia', status: 'In Transit', carrier: 'DHL', estimatedDelivery: '2024-03-22', progress: 75 },
    { trackingId: 'SHP-0007', origin: 'Dubai, UAE', destination: 'Mumbai, India', status: 'Delivered', carrier: 'FedEx', estimatedDelivery: '2024-02-20', progress: 100 },
    { trackingId: 'SHP-0008', origin: 'Shenzhen, China', destination: 'Berlin, Germany', status: 'Pending', carrier: 'UPS', estimatedDelivery: '2024-04-10', progress: 0 },
    { trackingId: 'SHP-0009', origin: 'Milan, Italy', destination: 'Boston, USA', status: 'In Transit', carrier: 'DHL', estimatedDelivery: '2024-03-28', progress: 35 },
    { trackingId: 'SHP-0010', origin: 'Bangkok, Thailand', destination: 'Toronto, Canada', status: 'Delayed', carrier: 'FedEx', estimatedDelivery: '2024-03-18', progress: 20 },
    { trackingId: 'SHP-0011', origin: 'Mexico City, Mexico', destination: 'Houston, USA', status: 'Delivered', carrier: 'UPS', estimatedDelivery: '2024-02-25', progress: 100 },
    { trackingId: 'SHP-0012', origin: 'Seoul, South Korea', destination: 'Seattle, USA', status: 'In Transit', carrier: 'DHL', estimatedDelivery: '2024-04-01', progress: 50 }
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    this.dataSource = new MatTableDataSource(this.shipmentsList);
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = (data: Shipment, filter: string) => {
      const filterValue = filter.trim().toLowerCase();
      if (!filterValue) return true;
      if (this.selectedCarrier && data.carrier.toLowerCase() !== filterValue) return false;
      return data.trackingId.toLowerCase().includes(filterValue) ||
             data.origin.toLowerCase().includes(filterValue) ||
             data.destination.toLowerCase().includes(filterValue);
    };
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  applyCarrierFilter(carrier: string): void {
    this.selectedCarrier = carrier;
    this.dataSource.filter = carrier.toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Pending': return 'status-pending';
      case 'In Transit': return 'status-transit';
      case 'Delivered': return 'status-delivered';
      case 'Delayed': return 'status-delayed';
      default: return '';
    }
  }

  getProgressBarClass(progress: number): string {
    if (progress >= 100) return 'progress-complete';
    if (progress >= 50) return 'progress-mid';
    return 'progress-low';
  }
}
