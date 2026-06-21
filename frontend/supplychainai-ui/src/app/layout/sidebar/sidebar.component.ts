import { Component, EventEmitter, Output } from '@angular/core';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  badge?: number;
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  @Output() navigate = new EventEmitter<void>();

  allNavItems: NavItem[] = [
    { label: 'Dashboard', icon: 'dashboard', route: '/dashboard' },
    { label: 'Suppliers', icon: 'business', route: '/suppliers' },
    { label: 'Products', icon: 'inventory_2', route: '/products' },
    { label: 'Purchase Requisitions', icon: 'description', route: '/pr' },
    { label: 'Purchase Orders', icon: 'shopping_cart', route: '/po' },
    { label: 'RFQs', icon: 'request_quote', route: '/rfq' },
    { label: 'Contracts', icon: 'article', route: '/contracts' },
    { label: 'Inventory', icon: 'warehouse', route: '/inventory' },
    { label: 'Warehouses', icon: 'store', route: '/warehouses' },
    { label: 'Orders', icon: 'receipt_long', route: '/orders' },
    { label: 'Returns', icon: 'undo', route: '/returns' },
    { label: 'Shipments', icon: 'local_shipping', route: '/shipments' },
    { label: 'Tracking', icon: 'pin_drop', route: '/tracking' },
    { label: 'Quality', icon: 'verified', route: '/quality' },
    { label: 'Invoices', icon: 'receipt', route: '/invoices' },
    { label: 'Payments', icon: 'payments', route: '/payments' },
    { label: 'Reports', icon: 'assessment', route: '/reports' },
    { label: 'Analytics', icon: 'insights', route: '/analytics' },
    { label: 'Notifications', icon: 'notifications', route: '/notifications' },
    { label: 'AI RAG Search', icon: 'psychology', route: '/ai-rag' },
    { label: 'Admin Panel', icon: 'admin_panel_settings', route: '/admin' },
  ];

  filteredItems: NavItem[] = [...this.allNavItems];

  filterItems(event: Event): void {
    const query = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredItems = this.allNavItems.filter(item =>
      item.label.toLowerCase().includes(query)
    );
  }

  trackByRoute(_index: number, item: NavItem): string {
    return item.route;
  }
}
