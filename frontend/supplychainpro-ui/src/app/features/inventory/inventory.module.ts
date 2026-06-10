import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { InventoryRoutingModule } from './inventory-routing.module';
import { InventoryComponent } from './inventory.component';


@NgModule({
  declarations: [
    InventoryComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    InventoryRoutingModule
  ]
})
export class InventoryModule { }
