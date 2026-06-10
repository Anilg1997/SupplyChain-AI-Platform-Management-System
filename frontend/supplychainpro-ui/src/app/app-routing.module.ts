import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: '/auth/login', pathMatch: 'full' },
  { path: 'dashboard', loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule) },
  { path: 'auth', loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule) },
  { path: 'suppliers', loadChildren: () => import('./features/suppliers/suppliers.module').then(m => m.SuppliersModule) },
  { path: 'products', loadChildren: () => import('./features/products/products.module').then(m => m.ProductsModule) },
  { path: 'inventory', loadChildren: () => import('./features/inventory/inventory.module').then(m => m.InventoryModule) },
  { path: 'orders', loadChildren: () => import('./features/orders/orders.module').then(m => m.OrdersModule) },
  { path: 'po', loadChildren: () => import('./features/po/po.module').then(m => m.PoModule) },
  { path: 'pr', loadChildren: () => import('./features/pr/pr.module').then(m => m.PrModule) },
  { path: 'rfq', loadChildren: () => import('./features/rfq/rfq.module').then(m => m.RfqModule) },
  { path: 'contracts', loadChildren: () => import('./features/contracts/contracts.module').then(m => m.ContractsModule) },
  { path: 'warehouses', loadChildren: () => import('./features/warehouses/warehouses.module').then(m => m.WarehousesModule) },
  { path: 'shipments', loadChildren: () => import('./features/shipments/shipments.module').then(m => m.ShipmentsModule) },
  { path: 'returns', loadChildren: () => import('./features/returns/returns.module').then(m => m.ReturnsModule) },
  { path: 'tracking', loadChildren: () => import('./features/tracking/tracking.module').then(m => m.TrackingModule) },
  { path: 'quality', loadChildren: () => import('./features/quality/quality.module').then(m => m.QualityModule) },
  { path: 'invoices', loadChildren: () => import('./features/invoices/invoices.module').then(m => m.InvoicesModule) },
  { path: 'payments', loadChildren: () => import('./features/payments/payments.module').then(m => m.PaymentsModule) },
  { path: 'reports', loadChildren: () => import('./features/reports/reports.module').then(m => m.ReportsModule) },
  { path: 'analytics', loadChildren: () => import('./features/analytics/analytics.module').then(m => m.AnalyticsModule) },
  { path: 'notifications', loadChildren: () => import('./features/notifications/notifications.module').then(m => m.NotificationsModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
