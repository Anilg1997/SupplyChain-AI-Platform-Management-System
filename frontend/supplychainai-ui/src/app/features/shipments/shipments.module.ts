import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { MatStepperModule } from '@angular/material/stepper';

import { ShipmentsRoutingModule } from './shipments-routing.module';
import { ShipmentsComponent } from './shipments.component';

@NgModule({
  declarations: [
    ShipmentsComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    MatStepperModule,
    ShipmentsRoutingModule
  ]
})
export class ShipmentsModule { }
