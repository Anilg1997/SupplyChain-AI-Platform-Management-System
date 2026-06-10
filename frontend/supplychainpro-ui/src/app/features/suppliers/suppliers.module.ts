import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { SuppliersRoutingModule } from './suppliers-routing.module';
import { SuppliersComponent } from './suppliers.component';


@NgModule({
  declarations: [
    SuppliersComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    SuppliersRoutingModule
  ]
})
export class SuppliersModule { }
