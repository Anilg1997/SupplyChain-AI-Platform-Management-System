import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { WarehousesRoutingModule } from './warehouses-routing.module';
import { WarehousesComponent } from './warehouses.component';


@NgModule({
  declarations: [
    WarehousesComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    WarehousesRoutingModule
  ]
})
export class WarehousesModule { }
