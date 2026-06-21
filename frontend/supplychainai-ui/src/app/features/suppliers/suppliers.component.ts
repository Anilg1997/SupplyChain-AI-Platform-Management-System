import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-suppliers',
  standalone: false,
  templateUrl: './suppliers.component.html',
  styleUrl: './suppliers.component.scss'
})
export class SuppliersComponent implements AfterViewInit {
  displayedColumns = ['name', 'code', 'email', 'phone', 'status', 'type', 'actions'];

  suppliers = [
    { name: 'Acme Corp', code: 'SUP-001', email: 'contact@acmecorp.com', phone: '+1-555-0101', status: 'Active', type: 'Manufacturer' },
    { name: 'GlobalParts Ltd.', code: 'SUP-002', email: 'info@globalparts.com', phone: '+1-555-0102', status: 'Active', type: 'Distributor' },
    { name: 'RawMaterials Inc.', code: 'SUP-003', email: 'sales@rawmaterials.com', phone: '+1-555-0103', status: 'Active', type: 'Manufacturer' },
    { name: 'PackPro Solutions', code: 'SUP-004', email: 'hello@packpro.com', phone: '+1-555-0104', status: 'Inactive', type: 'Service Provider' },
    { name: 'ElectroSys Corp.', code: 'SUP-005', email: 'orders@electrosys.com', phone: '+1-555-0105', status: 'Active', type: 'Manufacturer' },
    { name: 'SteelWorks Co.', code: 'SUP-006', email: 'info@steelworks.com', phone: '+1-555-0106', status: 'Active', type: 'Distributor' },
    { name: 'ChemSupply Group', code: 'SUP-007', email: 'contact@chemsupply.com', phone: '+1-555-0107', status: 'Inactive', type: 'Manufacturer' },
    { name: 'Precision Tools Ltd.', code: 'SUP-008', email: 'sales@precisiontools.com', phone: '+1-555-0108', status: 'Active', type: 'Service Provider' },
    { name: 'Hydraulics Plus', code: 'SUP-009', email: 'info@hydraulicsplus.com', phone: '+1-555-0109', status: 'Active', type: 'Distributor' },
    { name: 'EcoMaterials Inc.', code: 'SUP-010', email: 'hello@ecomaterials.com', phone: '+1-555-0110', status: 'Active', type: 'Manufacturer' },
    { name: 'FastLogistics Co.', code: 'SUP-011', email: 'dispatch@fastlogistics.com', phone: '+1-555-0111', status: 'Inactive', type: 'Service Provider' },
    { name: 'QualityParts GmbH', code: 'SUP-012', email: 'info@qualityparts.de', phone: '+1-555-0112', status: 'Active', type: 'Manufacturer' },
    { name: 'Northwest Supply', code: 'SUP-013', email: 'orders@northwest.com', phone: '+1-555-0113', status: 'Active', type: 'Distributor' },
    { name: 'PrimeComponents Ltd.', code: 'SUP-014', email: 'sales@primecomponents.com', phone: '+1-555-0114', status: 'Active', type: 'Manufacturer' },
    { name: 'GreenEnergy Parts', code: 'SUP-015', email: 'info@greenenergyparts.com', phone: '+1-555-0115', status: 'Inactive', type: 'Service Provider' },
  ];

  dataSource = new MatTableDataSource(this.suppliers);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  openDialog(supplier?: any) {
  }

  deleteSupplier(supplier: any) {
  }
}
